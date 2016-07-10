package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class LocalVariableDeclaration extends Command {
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
	public LocalVariableDeclaration(Type type, ArrayList<Identifier> lid) {
		this.type = type;
		this.lId = lid;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab + "LocalVarDecl[ ";
		for(int i=0; i<lId.size(); i++)
			str += "( ID[ " + lId.get(i).toString(level) + "] ) ";
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitLocalDeclaration(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof LocalVariableDeclaration)
			return true;
		return false;
	}
}
