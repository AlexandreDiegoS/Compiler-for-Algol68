package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class TypeVoid extends Type {

	public TypeVoid(String spelling) {
		super(spelling);
	}
	
	@Override
	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitTypeVoid(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof TypeVoid)
			return true;
		return false;
	}
}
