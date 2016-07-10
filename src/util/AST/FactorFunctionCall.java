package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FactorFunctionCall extends Factor {
	private FunctionCall fc;
	
	public FactorFunctionCall(FunctionCall fc) {
		this.fc = fc;
	}

	@Override
	public String toString(int level) {
		return "FunctionCall[ " + this.fc.toString(0) + "] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFacFunctionCall(this, tree);
	}

	public FunctionCall getFc() {
		return fc;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FactorFunctionCall)
			return true;
		return false;
	}
}
