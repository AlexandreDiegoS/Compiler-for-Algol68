package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class VariableDeclaration extends Command{
	private Type type;
	private ArrayList<Identifier> lId;
	private int scope;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ArrayList<Identifier> getlId() {
		return lId;
	}

	public void setlId(ArrayList<Identifier> lId) {
		this.lId = lId;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public VariableDeclaration(Type type, ArrayList<Identifier> lId) {
		this.type = type;
		this.lId = lId;
	}

	@Override
	public String toString(int level) {
		String str = "VarDecl[ ";
		for(int i=0; i<lId.size(); i++)
			str += "( ID[ " + lId.get(i).toString(level) + "] ) ";
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitVariableDeclaration(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof VariableDeclaration)
			return true;
		return false;
	}
}
