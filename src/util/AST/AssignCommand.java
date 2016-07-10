package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class AssignCommand extends Command {
	private Identifier id;
	private Expression exp;
	
	public AssignCommand(Identifier id, Expression exp) {
		this.id = id;
		this.exp = exp;
	}
	
	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		return tab + "Assign[ ID[ " + id.toString(level) + "] Expression[ " + exp.toString(level) + "] ] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitAssign(this, tree);
	}

	public Identifier getId() {
		return id;
	}

	public Expression getExp() {
		return exp;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof AssignCommand)
			return true;
		return false;
	}
}
