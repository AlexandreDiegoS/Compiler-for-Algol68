package encoder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import checker.SemanticException;
import checker.Visitor;
import compiler.Properties;
import util.AST.AST;
import util.AST.Arguments;
import util.AST.ArithmeticExpression;
import util.AST.AssignCommand;
import util.AST.BreakCommand;
import util.AST.Command;
import util.AST.ContinueCommand;
import util.AST.Expression;
import util.AST.Factor;
import util.AST.FactorExpression;
import util.AST.FactorFunctionCall;
import util.AST.FactorIdentifier;
import util.AST.FactorLiteral;
import util.AST.FunctionCall;
import util.AST.FunctionDeclaration;
import util.AST.GlobalVariableDeclaration;
import util.AST.Identifier;
import util.AST.IfCommand;
import util.AST.LocalVariableDeclaration;
import util.AST.Logic;
import util.AST.Number;
import util.AST.Operator;
import util.AST.PrintCommand;
import util.AST.Program;
import util.AST.ProgramCommand;
import util.AST.ReturnCommand;
import util.AST.Term;
import util.AST.TypeBool;
import util.AST.TypeInt;
import util.AST.TypeVoid;
import util.AST.VariableDeclaration;
import util.AST.WhileCommand;

public class Encoder implements Visitor{
	
	private StringBuffer extern, data, text;
	private Hashtable<String, Integer> variables;
	private int ebp;
	private int countCmp;
	private int countIf;
	private int countWhile;
	private int countLocalCmp;
	private int countLocalIf;
	private int countLocalWhile;
	
	public Encoder() {
		extern = new StringBuffer();
		data = new StringBuffer();
		text = new StringBuffer();
	}

	public void emit(String section, String instruction) {
		if (section.equals("data")) {
			data.append(instruction+"\n");
		} else if (section.equals("extern")) {
			extern.append(instruction+"\n");
		} else if (section.equals("text")) {
			text.append(instruction+"\n");
		}
	}

	public void encode(Program p) {
		try {
			p.visit(this, new ArrayList<AST>());
			StringBuffer objectCode = new StringBuffer();
			objectCode.append(extern);
			objectCode.append("\n");
			objectCode.append("SECTION .data\n");
			objectCode.append(data);
			objectCode.append("\n");
			objectCode.append("SECTION .text\n");
			objectCode.append("global _WinMain@16\n\n");
			objectCode.append(text);
			objectCode.append("\n");
			System.out.println(objectCode.toString());
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(Properties.objectCodeLocation));
			
			bw.write(objectCode.toString());
			bw.flush();
			bw.close();
		} catch (SemanticException | IOException e) {
			System.err.println(e);
		}
	}
	
	@Override
	public Object visitArguments(Arguments argList, ArrayList<AST> tree) throws SemanticException {
		for(Expression exp : argList.getExpressions()){
			exp.visit(this, tree);
		}
		return null;
	}

	@Override
	public Object visitArithmeticExpression(ArithmeticExpression aExp, ArrayList<AST> tree) throws SemanticException {
		if (aExp.getTerms().isEmpty()) {
			aExp.getT().visit(this, tree);
		} else {
			int i = 0;
			for (Term t : aExp.getTerms()) {
				if (i == 0) {
					aExp.getT().visit(this, tree);
					i++;
				}
				t.visit(this, tree);
				emit("text", "pop ebx");
				emit("text", "pop eax");
				if (aExp.getOperators().get(i-1).getSpelling().equals("+")){
					emit("text", "add eax, ebx");
				} else {
					emit("text", "sub eax, ebx");
				}
				emit("text", "push eax");
			}
		}
		return null;
	}

	@Override
	public Object visitAssign(AssignCommand asg, ArrayList<AST> tree) throws SemanticException {
		asg.getExp().visit(this, tree);
		AST decl = asg.getId().getDeclaration();
		if(decl instanceof GlobalVariableDeclaration){
			emit("text", "pop dword [" + asg.getId().getSpelling() + "]");
		}else if(decl instanceof LocalVariableDeclaration){
			emit("text", "pop dword [ebp" + variables.get(asg.getId().getSpelling()) + "]");
		}else{
			emit("text", "pop dword [ebp+" + variables.get(asg.getId().getSpelling()) + "]");
		}
		return null;
	}

	@Override
	public Object visitBreak(BreakCommand brk, ArrayList<AST> tree) throws SemanticException {
		String func = "";
		int countWhile = -1;
		
		for(AST ast : tree){
			if(ast instanceof FunctionDeclaration){
				func = "_" + ((FunctionDeclaration)ast).getId().getSpelling();
			}
			else if(ast instanceof WhileCommand){
				countWhile++;
			} 
		}
		
		emit("text", "jmp " + func + "_end_while_" + countWhile);
		return null;
	}

	@Override
	public Object visitContinue(ContinueCommand cont, ArrayList<AST> tree) throws SemanticException {
		String func = "";
		int countWhile = -1;
		
		for(AST ast : tree){
			if(ast instanceof FunctionDeclaration){
				func = "_" + ((FunctionDeclaration)ast).getId().getSpelling();
			}
			else if(ast instanceof WhileCommand){
				countWhile++;
			} 
		}
		
		emit("text", "jmp " + func + "_while_" + countWhile);
		return null;
	}

	@Override
	public Object visitExpression(Expression exp, ArrayList<AST> tree) throws SemanticException {
		String func = "";
		for (AST ast : tree) {
			if (ast instanceof FunctionDeclaration) {
				func = "_"+((FunctionDeclaration) ast).getId().getSpelling();
				break;
			}
		}
		if(exp.getAe2() == null){
			exp.getAe1().visit(this, tree);
		}
		else{
			exp.getAe1().visit(this, tree);
			exp.getAe2().visit(this, tree);
			emit("text", "pop ebx");
			emit("text", "pop eax");
			emit("text", "cmp eax, ebx");
			String op = exp.getOp().getSpelling();
			int auxCountCmp;
			if(func.equals("")){
				auxCountCmp = countCmp;
			}else{
				auxCountCmp = countLocalCmp;
			}
			if(op.equals("<")){
				emit("text", "jge " + func + "_false_cmp_"+auxCountCmp);
			}else if(op.equals("<=")){
				emit("text", "jg " + func + "_false_cmp_"+auxCountCmp);
			}else if(op.equals(">")){
				emit("text", "jle " + func + "_false_cmp_"+auxCountCmp);
			}else if(op.equals(">=")){
				emit("text", "jl " + func + "_false_cmp_"+auxCountCmp);
			}else if(op.equals("=")){
				emit("text", "jne " + func + "_false_cmp_"+auxCountCmp);
			}else if(op.equals("/=")){
				emit("text", "je " + func + "_false_cmp_"+auxCountCmp);
			}
			emit("text", "push dword 1");
			emit("text", "jmp " + func + "_end_cmp_" + auxCountCmp);
			emit("text", func + "_false_cmp_" + auxCountCmp + ":");
			emit("text", "push dword 0");
			emit("text", func + "_end_cmp_" + auxCountCmp + ":");
			if(func.equals("")){
				countCmp++;
			}else{
				countLocalCmp++;
			}
		}
		return null;
	}

	@Override
	public Object visitFacExpression(FactorExpression facExp, ArrayList<AST> tree) throws SemanticException {
		facExp.getExp().visit(this, tree);
		return null;
	}

	@Override
	public Object visitFacFunctionCall(FactorFunctionCall facFuncCall, ArrayList<AST> tree) throws SemanticException {
		tree.add(facFuncCall);
		facFuncCall.getFc().visit(this, tree);
		tree.remove(facFuncCall);
		return null;
	}

	@Override
	public Object visitFacIdentifier(FactorIdentifier facId, ArrayList<AST> tree) throws SemanticException {
		facId.getId().visit(this, tree);
		return null;
	}

	@Override
	public Object visitFacLiteral(FactorLiteral facLit, ArrayList<AST> tree) throws SemanticException {
		facLit.getL().visit(this, tree);
		return null;
	}

	@Override
	public Object visitFunctionCall(FunctionCall funcCall, ArrayList<AST> tree) throws SemanticException {
		if(funcCall.getAl() != null){
			funcCall.getAl().visit(this, tree);
		}
		emit("text", "call _" + funcCall.getId().getSpelling());
		if(funcCall.getAl() != null)
			emit("text", "add esp, " + funcCall.getAl().getExpressions().size() * 4);
		if (!tree.isEmpty() && (tree.get(tree.size()-1) instanceof FactorFunctionCall)) {
			emit("text", "push eax");
		}
		return null;
	}

	@Override
	public Object visitFunctionDeclaration(FunctionDeclaration funcDecl, ArrayList<AST> tree) throws SemanticException {
		variables = new Hashtable<>();
		ebp = funcDecl.getNumberOfParameters()*4 + 4;
		countLocalIf = countLocalWhile = 0;
		countLocalCmp = 0;
		if (funcDecl.getParameters() != null) {
			for(VariableDeclaration param : funcDecl.getParameters()){
				int aux = ebp - (param.getlId().size()*4);
				param.visit(this, tree); 
				ebp = aux;
			}
		}
		ebp = 0;
		emit("text", "_" + funcDecl.getId().getSpelling() + ":");
		emit("text", "push ebp");
		emit("text", "mov ebp, esp");
		tree.add(funcDecl);
		for (Command c : funcDecl.getCommands()) {
			if(c instanceof LocalVariableDeclaration){
				c.visit(this, tree);
				ebp -= ((LocalVariableDeclaration)c).getlId().size()*4;
			}
			else{
				c.visit(this, tree);
			}
		}
		tree.remove(funcDecl);
		emit("text", "_end_" + funcDecl.getId().getSpelling() + ":");
		emit("text", "mov esp, ebp");
		emit("text", "pop ebp");
		emit("text", "ret");
		emit("text", "");
		return null;
	}

	@Override
	public Object visitGlobalDeclaration(GlobalVariableDeclaration globalDecl, ArrayList<AST> tree)
			throws SemanticException {
		for(Identifier id : globalDecl.getlId()){
			emit("data", id.getSpelling() + ": dd 0");
		}
		return null;
	}

	@Override
	public Object visitIdentifier(Identifier id, ArrayList<AST> tree) throws SemanticException {
		AST decl = id.getDeclaration();
		if(decl instanceof GlobalVariableDeclaration){
			emit("text", "push dword [" + id.getSpelling() + "]");
		}else if(decl instanceof LocalVariableDeclaration){
			emit("text", "push dword [ebp" + variables.get(id.getSpelling()) + "]");
		}else{
			emit("text", "push dword [ebp+" + variables.get(id.getSpelling()) + "]");
		}
		return null;
	}

	@Override
	public Object visitIf(IfCommand ifCmd, ArrayList<AST> tree) throws SemanticException {
		String lblEndIf = "";
		String lblElse = "";
		String func = "";
		for (AST ast : tree){
			if(ast instanceof FunctionDeclaration){
				func = "_" + ((FunctionDeclaration)ast).getId().getSpelling();
				break;
			}
		}
		if(func.equals("")){
			lblEndIf = "_end_if_" + countIf;
			lblElse = "_else_"+ countIf;
			countIf++;
		}else{
			lblEndIf = func + "_end_if_" + countLocalIf;
			lblElse = func + "_else_"+ countLocalIf;
			countLocalIf++;
		}
		ifCmd.getExp().visit(this, tree);
		emit("text", "push dword 1");
		emit("text", "pop ebx");
		emit("text", "pop eax");
		emit("text", "cmp eax, ebx");
		if(ifCmd.getlElseCmd().isEmpty()){
			emit("text", "jne " + lblEndIf);
			for(Command cmd : ifCmd.getlCmd()){
				if(!func.equals("") && cmd instanceof LocalVariableDeclaration){
					cmd.visit(this, tree);
					ebp -= ((LocalVariableDeclaration)cmd).getlId().size()*4;
				}
				else
					cmd.visit(this, tree);
			}
		}else{
			emit("text", "jne " + lblElse);
			for(Command cmd : ifCmd.getlCmd()){
				if(!func.equals("") && cmd instanceof LocalVariableDeclaration){
					cmd.visit(this, tree);
					ebp -= ((LocalVariableDeclaration)cmd).getlId().size()*4;
				}
				else
					cmd.visit(this, tree);
			}
			emit("text", "jmp " + lblEndIf);
			emit("text", lblElse + ":");
			for(Command cmd : ifCmd.getlElseCmd()){
				if(!func.equals("") && cmd instanceof LocalVariableDeclaration){
					cmd.visit(this, tree);
					ebp -= ((LocalVariableDeclaration)cmd).getlId().size()*4;
				}
				else
					cmd.visit(this, tree);
			}
		}
		emit("text", lblEndIf + ":");
		return null;
	}

	@Override
	public Object visitLocalDeclaration(LocalVariableDeclaration localDecl, ArrayList<AST> tree)
			throws SemanticException {
		int nIds = localDecl.getlId().size();
		emit("text", "sub esp, " + nIds*4);
		for(Identifier id : localDecl.getlId()){
			variables.put(id.getSpelling(), ebp - (4*(localDecl.getlId().indexOf(id)+1)));
		}
		return null;
	}

	@Override
	public Object visitLogic(Logic logic, ArrayList<AST> tree) throws SemanticException {
		emit("text", "push dword " + (logic.getSpelling().equals("true") ? "1" : "0"));
		return null;
	}

	@Override
	public Object visitNumber(Number num, ArrayList<AST> tree) throws SemanticException {
		emit("text", "push dword " + num.getSpelling());
		return null;
	}

	@Override
	public Object visitOperator(Operator op, ArrayList<AST> tree) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitPrint(PrintCommand printCmd, ArrayList<AST> tree) throws SemanticException {
		if(extern.length() == 0){
			emit("extern", "extern _printf");
		}
		printCmd.getExp().visit(this, tree);
		emit("text", "push dword intFormat");
		emit("text", "call _printf");
		emit("text", "add esp, 8");
		return null;
	}

	@Override
	public Object visitProgram(Program prog, ArrayList<AST> tree) throws SemanticException {
		countCmp = 0;
		countIf = 0;
		countWhile = 0;
		emit("text", "_WinMain@16:");
		emit("text", "push ebp");
		emit("text", "mov ebp, esp");
		for (ProgramCommand prgCmd : prog.getCommands()) {
			if(!(prgCmd instanceof FunctionDeclaration)){
					prgCmd.visit(this, tree);
			}
		}
		emit("text", "mov esp, ebp");
		emit("text", "pop ebp");
		emit("text", "ret");
		emit("text", "");
		
		for (ProgramCommand prgCmd : prog.getCommands()) {
			if(prgCmd instanceof FunctionDeclaration){
					prgCmd.visit(this, tree);
			}
		}
		if(extern.length() != 0){
			emit("text", "");
			emit("data", "intFormat: db \"%d\", 10, 0");
		}
		return null;
	}

	@Override
	public Object visitReturn(ReturnCommand ret, ArrayList<AST> tree) throws SemanticException {
		String funcId = "";
		if (ret.getExp() != null) {
			ret.getExp().visit(this, tree);
			emit("text", "pop eax");
		}
		for(AST function: tree){
			if(function instanceof FunctionDeclaration)
				funcId = ((FunctionDeclaration) function).getId().getSpelling();
		}
		emit("text", "jmp _end_" + funcId);
		return null;
	}

	@Override
	public Object visitTerm(Term term, ArrayList<AST> tree) throws SemanticException {
		if (term.getFactors().isEmpty()) {
			term.getF().visit(this, tree);
		} else {
			int i = 0;
			for (Factor f : term.getFactors()) {
				if (i == 0) {
					term.getF().visit(this, tree);
					i++;
				}
				f.visit(this, tree);
				emit("text", "pop ebx");
				emit("text", "pop eax");
				if (term.getOperators().get(i-1).getSpelling().equals("*")){
					emit("text", "imul ebx");
				} else {
					emit("text", "idiv ebx");
				}
				emit("text", "push eax");
			}
		}
		return null;
	}

	@Override
	public Object visitTypeBool(TypeBool tBool, ArrayList<AST> tree) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitTypeInt(TypeInt tInt, ArrayList<AST> tree) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitTypeVoid(TypeVoid tVoid, ArrayList<AST> tree) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitVariableDeclaration(VariableDeclaration varDecl, ArrayList<AST> tree) throws SemanticException {
		for(Identifier id : varDecl.getlId()){
			variables.put(id.getSpelling(), ebp - (4*(varDecl.getlId().indexOf(id))));
		}
		return null;
	}

	@Override
	public Object visitWhile(WhileCommand whileCmd, ArrayList<AST> tree) throws SemanticException {
		String func = "";
		String lblWhile = "";
		String lblEndWhile = "";
		for(AST ast : tree){
			if(ast instanceof FunctionDeclaration){
				func = "_" + ((FunctionDeclaration)ast).getId().getSpelling();
				break;
			}
		}
		if(func.equals("")){
			lblWhile = "_while_" + countWhile;
			lblEndWhile = "_end_while_" + countWhile;
			countWhile++;
		}else{
			lblWhile = func + "_while_" + countLocalWhile;
			lblEndWhile = func + "_end_while_" + countLocalWhile;
			countLocalWhile++;
		}
		emit("text", lblWhile + ":");
		whileCmd.getExp().visit(this, tree);
		emit("text", "push dword 1");
		emit("text", "pop ebx");
		emit("text", "pop eax");
		emit("text", "cmp eax, ebx");
		emit("text", "jne " + lblEndWhile);
		tree.add(whileCmd);
		for (Command cmd : whileCmd.getlCmd()) {
			if(!func.equals("") && cmd instanceof LocalVariableDeclaration){
				cmd.visit(this, tree);
				ebp -= ((LocalVariableDeclaration)cmd).getlId().size()*4;
			}
			else
				cmd.visit(this, tree);
		}
		tree.remove(whileCmd);
		emit("text", "jmp " + lblWhile);
		emit("text", lblEndWhile + ":");
		return null;
	}
	
}
