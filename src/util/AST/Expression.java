package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Expression extends AST {
	private ArithmeticExpression ae1;
	private Operator op;
	private ArithmeticExpression ae2;
	private String type;
	
	public Expression(ArithmeticExpression ae1, Operator op, ArithmeticExpression ae2) {
		this.ae1 = ae1;
		this.op = op;
		this.ae2 = ae2;
	}

	@Override
	public String toString(int level) {
		String str = "AritExpression[ " + ae1.toString(level); 
		if (op != null)
			str += "OP[ " + op.toString(level) + "] " + ae2.toString(level); 
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitExpression(this, tree);
	}

	public ArithmeticExpression getAe1() {
		return ae1;
	}
	public Operator getOp() {
		return op;
	}

	public ArithmeticExpression getAe2() {
		return ae2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Expression)
			return true;
		return false;
	}
}
