package checker;

import java.util.ArrayList;
import util.AST.*;
import util.AST.Number;

/**
 * Visitor Interface
 * 
 * @version 2016-maio-23
 * @course Compiladores
 * @authors 
 * 			Alexandre D S Silva
 * 			Bárbara M B de Carvalho
 * 			Thaís M de Almeida
 * @emails 
 * 			adss@ecomp.poli.br
 * 			bmbc@ecomp.poli.br
 * 			tma@ecomp.poli.br
 */

public interface Visitor {
	
	public Object visitArguments(Arguments argList, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitArithmeticExpression(ArithmeticExpression aExp, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitAssign(AssignCommand asg, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitBreak(BreakCommand brk, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitContinue(ContinueCommand cont, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitExpression(Expression exp, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFacExpression(FactorExpression facExp, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFacFunctionCall(FactorFunctionCall facFuncCall, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFacIdentifier(FactorIdentifier facId, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFacLiteral(FactorLiteral facLit, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFunctionCall(FunctionCall funcCall, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitFunctionDeclaration(FunctionDeclaration funcDecl, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitGlobalDeclaration(GlobalVariableDeclaration globalDecl, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitIdentifier(Identifier id, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitIf(IfCommand ifCmd, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitLocalDeclaration(LocalVariableDeclaration localDecl, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitLogic(Logic logic, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitNumber(Number num, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitOperator(Operator op, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitPrint(PrintCommand printCmd, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitProgram(Program prog, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitReturn(ReturnCommand ret, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitTerm(Term term, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitTypeBool(TypeBool tBool, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitTypeInt(TypeInt tInt, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitTypeVoid(TypeVoid tVoid, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitVariableDeclaration(VariableDeclaration varDecl, ArrayList<AST> tree) throws SemanticException;
	
	public Object visitWhile(WhileCommand whileCmd, ArrayList<AST> tree) throws SemanticException;
}
