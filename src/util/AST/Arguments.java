package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Arguments extends AST {
	private ArrayList<Expression> expressions;
	
	public Arguments(ArrayList<Expression> lExp) {
		this.expressions = lExp;
	}
	
	@Override
	public String toString(int level) {
		String str = "Parameters[ ";
		
		for(int i=0; i < this.expressions.size(); i++) 
			str += "( " + this.expressions.get(i).toString(level) + ") ";

		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitArguments(this, tree);
	}
		
	public ArrayList<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Arguments)
			return true;
		return false;
	}
}
