package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Operator extends Terminal {

	public Operator(String spelling) {
		this.spelling = spelling;
	}

	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitOperator(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Operator)
			return true;
		return false;
	}

}
