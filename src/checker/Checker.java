package checker;

import java.util.ArrayList;

import parser.Parser;
import util.AST.*;
import util.AST.Number;
import util.symbolsTable.IdentificationTable;

/**
 * Checker class
 * 
 * @version 2016-maio-26
 * @course Compiladores
 * @authors 
 * 			Alexandre D S Silva
 * 			B�rbara M B de Carvalho
 * 			Tha�s M de Almeida
 * @emails 
 * 			adss@ecomp.poli.br
 * 			bmbc@ecomp.poli.br
 * 			tma@ecomp.poli.br
 */

public class Checker implements Visitor {
	IdentificationTable idTable = null;
	Parser parser = null;

	public Checker() {
		this.parser = new Parser();
		this.idTable = new IdentificationTable();
	}

	public AST check(Program p) {
		try {
			return (AST) p.visit(this, new ArrayList<AST>());
		} catch (SemanticException e) {
			System.err.println(e);
		}
		return null;
	}

	public Object visitArguments(Arguments argList, ArrayList<AST> tree) throws SemanticException {
		String functionId = null;
		FunctionDeclaration funcDec = null;

		// Recover the function call of the tree 
		for (AST ast : tree) {
			if (ast instanceof FunctionCall) {
				functionId = ((FunctionCall) ast).getId().getSpelling();
				break;
			}
		}
		funcDec = (FunctionDeclaration) idTable.retrieve(functionId);

		ArrayList<VariableDeclaration> varDecList = funcDec.getParameters();
		ArrayList<Expression> expList = argList.getExpressions();

		// The number of arguments should be the same of the parameters
		if (expList.size() != funcDec.getNumberOfParameters()) {
			throw new SemanticException("Function \"" + functionId + "\" has wrong argument number.");
		}

		// The type of the arguments should be the same of the parameters (REGRA 3)
		int k = 0, i = 0;
		for (int j = 0; j < expList.size(); j++, k++) {
			if(k == varDecList.get(i).getlId().size()){
				i++;
				k = 0;
			}
			if (!varDecList.get(i).getType().getSpelling().equals(expList.get(j).visit(this, tree))) {
				throw new SemanticException("Function \"" + functionId + "\" has wrong argument types.");
			}
		}
		return null;
	}

	public Object visitArithmeticExpression(ArithmeticExpression aExp, ArrayList<AST> tree) throws SemanticException {
		String term1, resultTerm;
		term1 = resultTerm = (String) aExp.getT().visit(this, tree);
		if (aExp.getTerms().size() != 0) {
			// Verify if the first term is boolean (REGRA 7)
			if (!term1.equals("INT")) {
				throw new SemanticException("Operator not defined for type [BOOL].");
			}
			String term2 = null;
			ArrayList<Operator> operators = aExp.getOperators(); 
			ArrayList<Term> terms = aExp.getTerms();
			if (terms != null) {
				for (int i = 0; i < terms.size(); i++) {
					operators.get(i).visit(this, tree);
					term2 = (String) terms.get(i).visit(this, tree);
					if (!term2.equals("INT")) {
						throw new SemanticException("Operator not defined for type [BOOL].");
					}
				}
			}
			// Any operation performed up to this point must return INT (REGRA 7 - ( + | - ))
			resultTerm = "INT";
		}
		return resultTerm;
	}

	public Object visitAssign(AssignCommand asg, ArrayList<AST> tree) throws SemanticException {
		String type = (String) asg.getId().visit(this, tree);
		String id = asg.getId().getSpelling();
		
		// Assign only for variables (REGRA 8)
		if(!((idTable.retrieve(id) instanceof VariableDeclaration) ||
				(idTable.retrieve(id) instanceof LocalVariableDeclaration) ||
					(idTable.retrieve(id) instanceof GlobalVariableDeclaration)))
			throw new SemanticException("Assign error: \"" + id + "\" isn't a variable.");
		// If the type of the variable is different of the expression (REGRA 8)
		if (!type.equals(asg.getExp().visit(this, tree))) {
			throw new SemanticException("Assign error: the type of \"" + id + "\" is " + type + ".");
		}
		return null;
	}

	public Object visitBreak(BreakCommand brk, ArrayList<AST> tree) throws SemanticException {
		for (AST ast : tree) {
			if (ast instanceof WhileCommand) {
				return null;
			}
		}
		// Break command only can be used in a while scope. (REGRA 6)
		throw new SemanticException("Command Break should be inside a loop.");
	}

	public Object visitContinue(ContinueCommand cont, ArrayList<AST> tree) throws SemanticException {
		for (AST ast : tree) {
			if (ast instanceof WhileCommand) {
				return null;
			}
		}
		// Continue command only can be used in a while scope. (REGRA 6)
		throw new SemanticException("Command Break should be inside a loop.");

	}

	public Object visitExpression(Expression exp, ArrayList<AST> tree) throws SemanticException {
		String exp1, resultExp;
		exp1 = resultExp = (String) exp.getAe1().visit(this, tree);

		if (exp.getAe2() != null) {
			String op = (String) exp.getOp().visit(this, tree);
			String exp2 = (String) exp.getAe2().visit(this, tree);

			// If different types (REGRA 7)
			if (!exp1.equals(exp2)) {
				throw new SemanticException("Cannot operate between different types.");
			}
			// >, <, >= e <= Only for integer (REGRA 7)
			if ((op.equals(">") || op.equals("<") || op.equals(">=") || op.equals("<=")) && exp1.equals("BOOL")) {
				throw new SemanticException("Operator not defined for type [BOOL].");
			}
			// Any operation performed up to this point must return BOOL (REGRA 7 - ( = | /= | > | < | >= | <= ))
			resultExp = "BOOL";
		}
		// Decorate the AST with the type
		exp.setType(resultExp);
		return exp.getType();
	}

	public Object visitFacExpression(FactorExpression facExp, ArrayList<AST> tree) throws SemanticException {
		return facExp.getExp().visit(this, tree);
	}

	public Object visitFacFunctionCall(FactorFunctionCall facFuncCall, ArrayList<AST> tree) throws SemanticException {
		return facFuncCall.getFc().visit(this, tree);
	}

	public Object visitFacIdentifier(FactorIdentifier facId, ArrayList<AST> tree) throws SemanticException {
		return facId.getId().visit(this, tree);
	}

	public Object visitFacLiteral(FactorLiteral facLit, ArrayList<AST> tree) throws SemanticException {
		return facLit.getL().visit(this, tree);
	}

	public Object visitFunctionCall(FunctionCall funcCall, ArrayList<AST> tree) throws SemanticException {
		tree.add(funcCall);
		funcCall.getId().visit(this, tree);
		String id = funcCall.getId().getSpelling();

		if (funcCall.getAl() != null) {
			funcCall.getAl().visit(this, tree);
		} else {
			FunctionDeclaration funcDec = (FunctionDeclaration) idTable.retrieve(id);
			ArrayList<VariableDeclaration> varDecList = funcDec.getParameters();
			if (varDecList.size() != 0) {
				// The number of arguments must be the same of the number of parameters
				throw new SemanticException("Function \"" + id + "\" has wrong argument number.");
			}
		}
		// Recover the declaration of the objects
		FunctionDeclaration funcDec = null;

		funcDec = (FunctionDeclaration) idTable.retrieve(id);

		tree.remove(funcCall);

		// Return the type of the function based on the type declared
		return funcDec.getType().getSpelling();
	}

	public Object visitFunctionDeclaration(FunctionDeclaration funcDecl, ArrayList<AST> tree) throws SemanticException {
		tree.add(funcDecl);
		String id = funcDecl.getId().getSpelling();

		// Add the function id to the identification table
		idTable.enter(id, funcDecl);

		funcDecl.getId().visit(this, tree);

		if (funcDecl.getParameters() != null) {
			for (VariableDeclaration varDecl : funcDecl.getParameters()) {
				varDecl.visit(this, tree);
			}
		}

		boolean hasWhileReturn = false;
		boolean hasIfReturn = false;
		boolean hasReturn = false;
		boolean endAssignCmd = false;
		String retType = (String) funcDecl.getType().visit(this, tree);

		// Open scope of local Variables
		idTable.openScope();
		for (Command c : funcDecl.getCommands()) {
			if(!endAssignCmd && !(c instanceof LocalVariableDeclaration))
				endAssignCmd = true;
			// In scope of one function, all variables declarations should be the firsts commands (REGRA 10)
			if(endAssignCmd && (c instanceof LocalVariableDeclaration))
				throw new SemanticException("All variables declarations in the function should be made in begin of scope.");
			c.visit(this, tree);
			if (c instanceof ReturnCommand) {
				hasReturn = true;
			}
			if (c instanceof WhileCommand) {
				hasWhileReturn = hasWhileReturn || ((Boolean) c.visit(this, tree));
			}
			if (c instanceof IfCommand) {
				hasIfReturn = hasIfReturn || ((Boolean) c.visit(this, tree));
			}
		}
		hasReturn = hasReturn || hasWhileReturn || hasIfReturn;

		// If the return of a function was different of VOID, it should be at least one return command (REGRA 4)
		if (hasReturn == false && !retType.equals("VOID")) {
			throw new SemanticException("At least one return command should be defined for function \"" + id + "\".");
		}

		idTable.closeScope();
		tree.remove(funcDecl);
		return null;
	}

	public Object visitGlobalDeclaration(GlobalVariableDeclaration globalDecl, ArrayList<AST> tree)
			throws SemanticException {
		ArrayList<Identifier> lId = globalDecl.getlId();
		Type type = globalDecl.getType();
		for (Identifier id : lId) {
			idTable.enter(id.getSpelling(), globalDecl);
			globalDecl.setScope(idTable.getCurrentScope());
			type.visit(this, tree);
			id.visit(this, tree);
		}
		return null;
	}

	public Object visitIdentifier(Identifier id, ArrayList<AST> tree) throws SemanticException {
		AST varDec = null;
		String ident = id.getSpelling();

		// If not declared (REGRA 1)
		if (!idTable.containsKey(ident)) {
			throw new SemanticException("\"" + id.getSpelling() + "\" must be declared.");
		}

		// Recover the declaration of id in the symbols table.
		// Decore the AST with the declaration of the id 
		AST declaration = idTable.retrieve(id.getSpelling());
		id.setDeclaration(declaration);

		// Verify if the id came of a declaration of variable or function
		// Decore the AST with the type of the id 
		if (declaration instanceof VariableDeclaration){
			varDec = (VariableDeclaration) idTable.retrieve(id.getSpelling());
			id.setType(((VariableDeclaration) varDec).getType().getSpelling());
		}else if(declaration instanceof LocalVariableDeclaration){
			varDec = (LocalVariableDeclaration) idTable.retrieve(id.getSpelling());
			id.setType(((LocalVariableDeclaration) varDec).getType().getSpelling());
		}else if(declaration instanceof GlobalVariableDeclaration) {
			varDec = (GlobalVariableDeclaration) idTable.retrieve(id.getSpelling());
			id.setType(((GlobalVariableDeclaration) varDec).getType().getSpelling());
		}

		return id.getType();
	}

	public Object visitIf(IfCommand ifCmd, ArrayList<AST> tree) throws SemanticException {
		tree.add(ifCmd);
		idTable.openScope();
		ifCmd.getExp().visit(this, tree);

		String type = (String) ifCmd.getExp().visit(this, tree);

		boolean hasIfReturn, hasElseReturn, hasElse;
		hasElse = hasElseReturn = hasIfReturn = false;
		if(!type.equals("BOOL"))
			throw new SemanticException("The expression must be a boolean.");

		for (Command c : ifCmd.getlCmd()) {
			c.visit(this, tree);
			if (c instanceof ReturnCommand) {
				hasIfReturn = true;
			} else if (c instanceof IfCommand || c instanceof WhileCommand) {
				hasIfReturn = hasIfReturn || (Boolean) c.visit(this, tree);
			}
		}
		if(ifCmd.getlElseCmd() != null){
			idTable.openScope();
			for (Command c : ifCmd.getlElseCmd()) {
				hasElse = true;
				c.visit(this, tree);
				if (c instanceof ReturnCommand) {
					hasElseReturn = true;
				} else if (c instanceof IfCommand || c instanceof WhileCommand) {
					hasElseReturn = hasElseReturn || (Boolean) c.visit(this, tree);
				}
			}
			idTable.closeScope();
		}
		tree.remove(ifCmd);
		idTable.closeScope();
		// If no 'else', the 'if' there should be a return and its condition must always be met.
		// If there is an 'else', there must be a return on it and on the 'if'

		return (!hasElse && hasIfReturn) || (hasElse && hasElseReturn); 
	}

	public Object visitLocalDeclaration(LocalVariableDeclaration localDecl, ArrayList<AST> tree)
			throws SemanticException {
		ArrayList<Identifier> lId = localDecl.getlId();
		Type type = localDecl.getType();
		for (Identifier id : lId) {
			idTable.enter(id.getSpelling(), localDecl);
			localDecl.setScope(idTable.getCurrentScope());
			type.visit(this, tree);
			id.visit(this, tree);
		}
		return null;
	}

	public Object visitLogic(Logic logic, ArrayList<AST> tree) throws SemanticException {
		return "BOOL";
	}

	public Object visitNumber(Number num, ArrayList<AST> tree) throws SemanticException {
		return "INT";
	}

	public Object visitOperator(Operator op, ArrayList<AST> tree) throws SemanticException {
		return op.getSpelling();
	}

	public Object visitPrint(PrintCommand printCmd, ArrayList<AST> tree) throws SemanticException {
		printCmd.getExp().visit(this, tree);
		return null;
	}

	public Object visitProgram(Program prog, ArrayList<AST> tree) throws SemanticException {
		for(AST cmd : prog.getCommands()){
			cmd.visit(this, tree);
		}
		return prog;
	}

	public Object visitReturn(ReturnCommand ret, ArrayList<AST> tree) throws SemanticException {
		String functionId = null;
		FunctionDeclaration funcDec = null;

		// Recover the declaration of function in the tree of objects
		for (AST ast : tree) {
			if (ast instanceof FunctionDeclaration) {
				functionId = ((FunctionDeclaration) ast).getId().getSpelling();
				break;
			}
		}

		if(functionId != null){
			funcDec = (FunctionDeclaration) idTable.retrieve(functionId);

			String id = (String) funcDec.getId().getSpelling();
			String type = (String) funcDec.getType().getSpelling();
			String exp;

			exp = (String) ret.getExp().visit(this, tree);

			// The return type must match the return type declared in the function. (REGRA 5)
			if (!exp.equals(type)) {
				throw new SemanticException("Function \"" + id + "\" should return a type " + type + ".");
			}
			return exp;
		}

		// The return command should be used only in functions scope. (REGRA 11) 
		throw new SemanticException("Return command can be used only in function scope.");
	}

	public Object visitTerm(Term term, ArrayList<AST> tree) throws SemanticException {
		String factor1, resultTerm;
		factor1 = resultTerm = (String) term.getF().visit(this, tree);
		ArrayList<Operator> operators = term.getOperators(); 
		ArrayList<Factor> factors = term.getFactors();
		if (factors.size() != 0) {
			// Verify if the factor 1 is boolean (REGRA 7)
			if (!factor1.equals("INT")) {
				throw new SemanticException("Operator not defined for type [BOOL].");
			}
			String factor2 = null;
			for (int i = 0; i < factors.size(); i++) {
				operators.get(i).visit(this, tree);
				factor2 = (String) factors.get(i).visit(this, tree);
				if (!factor2.equals("INT")) {
					throw new SemanticException("Operator not defined for type [BOOL].");
				}
			}
			// Any operation performed up to this point must return INT (REGRA 7 - ( * | / ))
			resultTerm = "INT";
		}
		return resultTerm;
	}

	public Object visitTypeBool(TypeBool tBool, ArrayList<AST> tree) throws SemanticException {
		return "BOOL";
	}

	public Object visitTypeInt(TypeInt tInt, ArrayList<AST> tree) throws SemanticException {
		return "INT";
	}

	public Object visitTypeVoid(TypeVoid tVoid, ArrayList<AST> tree) throws SemanticException {
		return "VOID";
	}

	public Object visitVariableDeclaration(VariableDeclaration varDecl, ArrayList<AST> tree) throws SemanticException {
		ArrayList<Identifier> lId = varDecl.getlId();
		Type type = varDecl.getType();
		for (Identifier id : lId) {
			idTable.enter(id.getSpelling(), varDecl);
			varDecl.setScope(idTable.getCurrentScope());
			type.visit(this, tree);
			id.visit(this, tree);
		}
		return null;
	}

	public Object visitWhile(WhileCommand whileCmd, ArrayList<AST> tree) throws SemanticException {
		tree.add(whileCmd);
		idTable.openScope();

		String type = (String) whileCmd.getExp().visit(this, tree);
		boolean hasWhileReturn;
		hasWhileReturn = false;

		if(!type.equals("BOOL"))
			throw new SemanticException("The expression must be a boolean.");

		for (Command c : whileCmd.getlCmd()) {
			c.visit(this, tree);
			if (c instanceof ReturnCommand) {
				hasWhileReturn = true;
			} else if (c instanceof IfCommand || c instanceof WhileCommand) {
				hasWhileReturn = hasWhileReturn || (Boolean) c.visit(this, tree);
			}
		}
		tree.remove(whileCmd);
		idTable.closeScope();
		return hasWhileReturn;
	}
}
