package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorIdentifier extends Factor {
	private Identifier id;
	
	public FactorIdentifier(Identifier id) {
		this.id = id;
	}

	@Override
	public String toString(int level) {
		return "ID[ " + this.id.toString(0) + "] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFacIdentifier(this, tree);
	}

	public Identifier getId() {
		return id;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FactorIdentifier)
			return true;
		return false;
	}
}
