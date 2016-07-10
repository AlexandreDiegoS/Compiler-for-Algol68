package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FunctionCall extends Command {
	private Identifier id;
	private Arguments al;
	

	public FunctionCall(Identifier id, Arguments al) {
		this.id = id;
		this.al = al;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab + "FunctionCall[ ID[ " + id.toString(level) + "] ";
		if (al!=null) 
			str += al.toString(level);
		str += "] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFunctionCall(this, tree);
	}
		
	public Identifier getId() {
		return id;
	}

	public Arguments getAl() {
		return al;
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FunctionCall)
			return true;
		return false;
	}
}
