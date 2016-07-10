package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Logic extends Literal {

	public Logic(String spelling) {
		super(spelling);
	}

	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitLogic(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Logic)
			return true;
		return false;
	}
}
