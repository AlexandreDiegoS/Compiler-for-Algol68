package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class GlobalVariableDeclaration extends Command {
	private Type type;
	private ArrayList<Identifier> lId;
	private int scope;
	public GlobalVariableDeclaration(Type type, ArrayList<Identifier> lId) {
		this.type = type;
		this.lId = lId;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab+ "GlobalVarDecl[ ";
		for(int i=0; i<lId.size(); i++)
			str += "( ID[ " + lId.get(i).toString(level) + "] ) ";
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitGlobalDeclaration(this, tree);
	}

	public Type getType() {
		return type;
	}
	
	public ArrayList<Identifier> getlId() {
		return lId;
	}

	public void setType(Type type) {
		this.type = type;
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
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof GlobalVariableDeclaration)
			return true;
		return false;
	}
}
