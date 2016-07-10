package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class TypeBool extends Type {

	public TypeBool(String spelling) {
		super(spelling);
	}

	@Override
	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitTypeBool(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof TypeBool)
			return true;
		return false;
	}
}
