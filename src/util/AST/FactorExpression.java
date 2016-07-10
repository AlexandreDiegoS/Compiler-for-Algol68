package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorExpression extends Factor {
	private Expression exp;
	
	public FactorExpression(Expression exp) {
		this.exp = exp;
	}

	@Override
	public String toString(int level) {
		return "Expression[ " + this.exp.toString(0) + "] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFacExpression(this, tree);
	}
	
	public Expression getExp() {
		return exp;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FactorExpression)
			return true;
		return false;
	}
}
