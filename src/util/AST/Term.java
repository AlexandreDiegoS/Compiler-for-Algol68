package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Term extends AST {
	private Factor f;
	private ArrayList<Operator> operators;
	private ArrayList<Factor> factors;
	
	public Term(Factor f1, ArrayList<Operator> tOp, ArrayList<Factor> tFactor) {
		this.f = f1;
		this.operators = tOp;
		this.factors = tFactor;
	}

	@Override
	public String toString(int level) {
		String str = "Factor[ " + f.toString(level); 
		for(int i=0; i<factors.size(); i++) 
			str += operators.get(i).toString(level) + factors.get(i).toString(level);
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitTerm(this, tree);
	}

	public Factor getF() {
		return f;
	}

	public ArrayList<Operator> getOperators() {
		return operators;
	}

	public ArrayList<Factor> getFactors() {
		return factors;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Term)
			return true;
		return false;
	}
}
