package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ArithmeticExpression extends AST {
	private Term t;
	private ArrayList<Operator> operators;
	private ArrayList<Term> terms;
	
	public ArithmeticExpression(Term t1, ArrayList<Operator> tOp, ArrayList<Term> tTerm) {
		this.t = t1;
		this.operators = tOp;
		this.terms = tTerm;
	}

	@Override
	public String toString(int level) {
		String str = "Term[ " + t.toString(level); 
		for(int i=0; i<terms.size(); i++) 
			str += "OP[ " + operators.get(i).toString(level) +"] " +  terms.get(i).toString(level);
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitArithmeticExpression(this, tree);
	}

	public Term getT() {
		return t;
	}

	public ArrayList<Operator> getOperators() {
		return operators;
	}

	public ArrayList<Term> getTerms() {
		return terms;
	}
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof ArithmeticExpression)
			return true;
		return false;
	}
}
