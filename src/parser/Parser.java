package parser;

import java.util.ArrayList;

import scanner.Scanner;
import scanner.Token;
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
import util.AST.Type;
import util.AST.TypeBool;
import util.AST.TypeInt;
import util.AST.TypeVoid;
import util.AST.VariableDeclaration;
import util.AST.WhileCommand;

/**
 * Parser class
 * @version 2016-april-25
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 * @authors 
 * 			Alexandre D S Silva
 * 			B�rbara M B de Carvalho
 * 			Tha�s M de Almeida
 * @emails 
 * 			adss@ecomp.poli.br
 * 			bmbc@ecomp.poli.br
 * 			tma@ecomp.poli.br
 */

public class Parser {

	// The current token
	private Token currentToken = null;
	// The scanner
	private Scanner scanner = null;
	
	/**
	 * Parser constructor
	 */
	public Parser() {
		// Initializes the scanner object
		this.scanner = new Scanner();
		this.currentToken = scanner.getNextToken();
	}
	
	/**
	 * Veririfes if the current token kind is the expected one
	 */
	private void accept(int kind) throws SyntacticException {
		if (currentToken.getKind() == kind) {
			acceptIt();
		} else {
			throw new SyntacticException("Token not Expected.", currentToken);
		}
	}
	
	/**
	 * Gets next token
	 */
	private void acceptIt() {
		currentToken = scanner.getNextToken();
	}

	/**
	 * Verifies if the source program is syntactically correct
	 */
	public AST parse() {
		Program p = null;
		try {
			p = parseProgram();
			accept(GrammarSymbols.EOT);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return p;
	}
	public Program parseProgram() {
		Program p = null;
		try {
			ArrayList<ProgramCommand> commands = new ArrayList<ProgramCommand>();
			ProgramCommand pCmd = null;
			accept(GrammarSymbols.BEGIN);
			while (currentToken.getKind() != GrammarSymbols.END) {
				pCmd = parseProgramCommand();
				if(pCmd != null)
					commands.add(pCmd);
				pCmd = null;
			}
			accept(GrammarSymbols.END);
			p = new Program(commands);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return p;
	}

	public ProgramCommand parseProgramCommand() {
		ProgramCommand p = null;
			if (currentToken.getKind() == GrammarSymbols.PROC) {
				p = parseFunctionDeclaration();
			} else {
				if(currentToken.getKind() == GrammarSymbols.INT || 
						currentToken.getKind() == GrammarSymbols.BOOL)
					p = parseGlobalVariableDeclaration();
				else{
					p = parseCommand();
				}
			}
		return p;
	}

	public Command parseCommand() {
		Command cmd = null;
		try{
			if(currentToken.getKind() == GrammarSymbols.IF){
				acceptIt();
				Expression exp = parseExpression();
				accept(GrammarSymbols.THEN);
				ArrayList<Command> lCmd = new ArrayList<Command>();
				ArrayList<Command> lElseCmd = new ArrayList<Command>();
				Command command, elseCommand;
				while(currentToken.getKind() != GrammarSymbols.FI &&
						currentToken.getKind() != GrammarSymbols.ELSE){
					command = parseCommand();
					lCmd.add(command);
				}
				if(currentToken.getKind() == GrammarSymbols.ELSE){
					acceptIt();
					while(currentToken.getKind() != GrammarSymbols.FI){
						elseCommand = parseCommand();
						if(elseCommand != null)
							lElseCmd.add(elseCommand);
					}
				}
				accept(GrammarSymbols.FI);
				cmd = new IfCommand(exp, lCmd, lElseCmd);
			}else if(currentToken.getKind() == GrammarSymbols.WHILE){
				acceptIt();
				Expression exp = parseExpression();
				accept(GrammarSymbols.DO);
				ArrayList<Command> lCmd = new ArrayList<Command>();
				Command command;
				while(currentToken.getKind() != GrammarSymbols.OD){
					command = parseCommand();
					lCmd.add(command);
				}
				acceptIt();
				cmd = new WhileCommand(exp,lCmd);
			}else if(currentToken.getKind() == GrammarSymbols.BREAK){
				acceptIt();
				accept(GrammarSymbols.SC);
				cmd = new BreakCommand();
			}else if(currentToken.getKind() == GrammarSymbols.CONTINUE){
				acceptIt();
				accept(GrammarSymbols.SC);
				cmd = new ContinueCommand();
			}else if(currentToken.getKind() == GrammarSymbols.ID){
				Identifier id = new Identifier(currentToken.getSpelling());
				acceptIt();
				if(currentToken.getKind() == GrammarSymbols.ASG){
					acceptIt();
					Expression exp = parseExpression();
					accept(GrammarSymbols.SC);
					cmd = new AssignCommand(id, exp);
				}else{
					accept(GrammarSymbols.LP);
					Arguments arg = null;
					if(currentToken.getKind() != GrammarSymbols.RP)
						arg = parseArguments();
					accept(GrammarSymbols.RP);
					accept(GrammarSymbols.SC);
					cmd = new FunctionCall(id, arg);
				}
			}else if(currentToken.getKind() == GrammarSymbols.PRINT){
				acceptIt();
				accept(GrammarSymbols.LP);
				Expression exp = parseExpression();
				accept(GrammarSymbols.RP);
				accept(GrammarSymbols.SC);
				cmd = new PrintCommand(exp);
			}else if(currentToken.getKind() == GrammarSymbols.RETURN){
				acceptIt();
				Expression exp = parseExpression();
				accept(GrammarSymbols.SC);
				cmd = new ReturnCommand(exp);
			}else{
				cmd = parseLocalVariableDeclaration();
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return cmd;
	}

	public FunctionDeclaration parseFunctionDeclaration() {
		FunctionDeclaration FDecl = null;
		try{
			Type type = null;
			accept(GrammarSymbols.PROC);
			Identifier id = new Identifier(currentToken.getSpelling());
			accept(GrammarSymbols.ID);
			accept(GrammarSymbols.EQ);
			accept(GrammarSymbols.LP);
			ArrayList<VariableDeclaration> parameters = new ArrayList<VariableDeclaration>();
			VariableDeclaration VDecl;
			Command command;
			if(currentToken.getKind() != GrammarSymbols.RP){
				VDecl = parseVariableDeclaration();
				parameters.add(VDecl);
				while(currentToken.getKind() != GrammarSymbols.RP){
					if(!(currentToken.getKind() == GrammarSymbols.BOOL || 
							currentToken.getKind() == GrammarSymbols.INT))
						accept(GrammarSymbols.SEP);
					VDecl = parseVariableDeclaration();
					parameters.add(VDecl);
				}
			}
			acceptIt();
			if(currentToken.getKind() == GrammarSymbols.BOOL || currentToken.getKind() == GrammarSymbols.INT
					|| currentToken.getKind() == GrammarSymbols.VOID){
				if(currentToken.getKind() == GrammarSymbols.BOOL)
					type = new TypeBool(currentToken.getSpelling());
				else if(currentToken.getKind() == GrammarSymbols.INT)
					type = new TypeInt(currentToken.getSpelling());
				else
					type = new TypeVoid(currentToken.getSpelling());
				acceptIt();				
			}
			accept(GrammarSymbols.CO);
			accept(GrammarSymbols.BEGIN);
			ArrayList<Command> commands = new ArrayList<Command>();
			while(currentToken.getKind() != GrammarSymbols.END){
				command = parseCommand();
				commands.add(command);
			}
			acceptIt();
			FDecl = new FunctionDeclaration(id, parameters, type, commands);
			
		}catch(SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		
		return FDecl;
	}

	public VariableDeclaration parseVariableDeclaration() {
		VariableDeclaration VDecl = null;
		try{
			if(currentToken.getKind() == GrammarSymbols.INT || 
					currentToken.getKind() == GrammarSymbols.BOOL){
				Type type = null;
				if(currentToken.getKind() == GrammarSymbols.INT)
					type = new TypeInt(currentToken.getSpelling());
				else
					type = new TypeBool(currentToken.getSpelling());
				acceptIt();
				ArrayList<Identifier> lid = new ArrayList<Identifier>();
				Identifier id = new Identifier(currentToken.getSpelling());
				lid.add(id);
				accept(GrammarSymbols.ID);
				while(currentToken.getKind() == GrammarSymbols.SEP){
					acceptIt();
					if(currentToken.getKind() == GrammarSymbols.INT)
						acceptIt();
					else if(currentToken.getKind() == GrammarSymbols.BOOL)
						break;
					id = new Identifier(currentToken.getSpelling());
					lid.add(id);
					accept(GrammarSymbols.ID);
				}
				VDecl = new VariableDeclaration(type, lid);
			}
		}catch(SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return VDecl;
	}
	
	public LocalVariableDeclaration parseLocalVariableDeclaration() {
		LocalVariableDeclaration lVarDecl = null;
		try {
			Type type = null;
			if (currentToken.getKind() == GrammarSymbols.BOOL) {
				type = new TypeBool(currentToken.getSpelling());
				acceptIt();
			}else{
				type = new TypeInt(currentToken.getSpelling());
				accept(GrammarSymbols.INT);
			}
			ArrayList<Identifier> lid = new ArrayList<Identifier>();
			Identifier id = new Identifier(currentToken.getSpelling());
			lid.add(id);
			accept(GrammarSymbols.ID);
			while(currentToken.getKind() == GrammarSymbols.SEP){
				acceptIt();
				id = new Identifier(currentToken.getSpelling());
				lid.add(id);
				accept(GrammarSymbols.ID);
			}
			accept(GrammarSymbols.SC);
			lVarDecl = new LocalVariableDeclaration(type, lid);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return lVarDecl;
	}

	public GlobalVariableDeclaration parseGlobalVariableDeclaration() {
		GlobalVariableDeclaration gVarDecl = null;
		try {
			Type type = null;
			if (currentToken.getKind() == GrammarSymbols.BOOL) {
				type = new TypeBool(currentToken.getSpelling());
				acceptIt();
			}else{
				type = new TypeInt(currentToken.getSpelling());
				accept(GrammarSymbols.INT);
			}
			ArrayList<Identifier> lid = new ArrayList<Identifier>();
			Identifier id = new Identifier(currentToken.getSpelling());
			lid.add(id);
			accept(GrammarSymbols.ID);
			while(currentToken.getKind() == GrammarSymbols.SEP){
				acceptIt();
				id = new Identifier(currentToken.getSpelling());
				lid.add(id);
				accept(GrammarSymbols.ID);
			}
			accept(GrammarSymbols.SC);
			gVarDecl = new GlobalVariableDeclaration(type, lid);
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return gVarDecl;
	}
	
	public Expression parseExpression() {
		Expression exp = null;
		ArithmeticExpression ae1 = parseArithmeticExpression();
		Operator op = null;
		ArithmeticExpression ae2 = null;
		if (currentToken.getKind() == GrammarSymbols.EQ || currentToken.getKind() == GrammarSymbols.NEQ
				|| currentToken.getKind() == GrammarSymbols.GT || currentToken.getKind() == GrammarSymbols.GE
				|| currentToken.getKind() == GrammarSymbols.LT || currentToken.getKind() == GrammarSymbols.LE) {
			op = new Operator(currentToken.getSpelling());
			acceptIt();
			ae2 = parseArithmeticExpression();
		}
		exp = new Expression(ae1,op, ae2);
		return exp;
	}
	
	public ArithmeticExpression parseArithmeticExpression() {
		ArithmeticExpression ae = null;
		Term t1 = parseTerm();
		ArrayList<Operator> tOp = new ArrayList<Operator>();
		ArrayList<Term> tTerm = new ArrayList<Term>(); 
		while (currentToken.getKind() == GrammarSymbols.ADD || currentToken.getKind() == GrammarSymbols.SUB) {
			Operator op = new Operator(currentToken.getSpelling());
			acceptIt();
			Term tn = parseTerm();
			tOp.add(op);	
			tTerm.add(tn);
		}
		ae = new ArithmeticExpression(t1,tOp,tTerm);
		return ae;
	}

	public Term parseTerm() {
		Term t = null;
		Factor f1 = parseFactor();
		ArrayList<Operator> tOp = new ArrayList<Operator>();
		ArrayList<Factor> tFactor = new ArrayList<Factor>(); 
		while (currentToken.getKind() == GrammarSymbols.MUL || currentToken.getKind() == GrammarSymbols.DIV) {
			Operator op = new Operator(currentToken.getSpelling());
			acceptIt();
			Factor fn = parseFactor();
			tOp.add(op);	
			tFactor.add(fn);
		}
		t = new Term(f1,tOp, tFactor);
		return t;
	}

	public Factor parseFactor() {
		Factor f = null;
		try {
			if (currentToken.getKind() == GrammarSymbols.ID) {
				Identifier id = new Identifier(currentToken.getSpelling());
				acceptIt();
				if(currentToken.getKind() == GrammarSymbols.LP){
					acceptIt();
					Arguments arg = null;
					if(currentToken.getKind() != GrammarSymbols.RP)
						arg = parseArguments();
					accept(GrammarSymbols.RP);
					FunctionCall fc = new FunctionCall(id, arg);
					f = new FactorFunctionCall(fc);
				}
				else{
					f = new FactorIdentifier(id);
				}
			} else if (currentToken.getKind() == GrammarSymbols.NUMBER) {
				Number n = new Number(currentToken.getSpelling());
				acceptIt();
				f = new FactorLiteral(n);
			} else
				if (currentToken.getKind() == GrammarSymbols.TRUE || currentToken.getKind() == GrammarSymbols.FALSE) {
					Logic l = new Logic(currentToken.getSpelling());
					acceptIt();
					f = new FactorLiteral(l);
			} else {
				accept(GrammarSymbols.LP);
				Expression exp = parseExpression();
				accept(GrammarSymbols.RP);
				f = new FactorExpression(exp);
			}
		} catch (SyntacticException exception) {
			System.err.println(exception.toString());
			System.exit(0);
		}
		return f;
	}

	private Arguments parseArguments() {
		Arguments arg = null;
		ArrayList<Expression> lExp = new ArrayList<Expression>();
		Expression exp = parseExpression();
		lExp.add(exp);
		while(currentToken.getKind() == GrammarSymbols.SEP){
			acceptIt();
			exp = parseExpression();
			lExp.add(exp);
		}
		arg = new Arguments(lExp);
		return arg;
	}
}