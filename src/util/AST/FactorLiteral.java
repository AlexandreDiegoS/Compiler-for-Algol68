package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorLiteral extends Factor {
private Literal l;
	
	public FactorLiteral(Literal l) {
		this.l = l;
	}

	@Override
	public String toString(int level) {
		return "Literal[ " + this.l.toString(0) + "] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFacLiteral(this, tree);
	}

	public Literal getL() {
		return l;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FactorLiteral)
			return true;
		return false;
	}
}
