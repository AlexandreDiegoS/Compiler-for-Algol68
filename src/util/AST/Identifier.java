package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Identifier extends Terminal {
	private String type = null;
	private AST declaration = null;
	
	public Identifier(String spelling) {
		this.spelling = spelling;
	}

	@Override
	public String toString(int level) {
		return spelling + " ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitIdentifier(this, tree);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AST getDeclaration() {
		return declaration;
	}

	public void setDeclaration(AST declaration) {
		this.declaration = declaration;
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Identifier)
			return true;
		return false;
	}
}
