package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class TypeInt extends Type {
	
	public TypeInt(String spelling) {
		super(spelling);
	}
	
	@Override
	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitTypeInt(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof TypeInt)
			return true;
		return false;
	}
}
