package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Number extends Literal {

	public Number(String spelling) {
		super(spelling);
	}

	@Override
	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitNumber(this, tree);
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Number)
			return true;
		return false;
	}
}
