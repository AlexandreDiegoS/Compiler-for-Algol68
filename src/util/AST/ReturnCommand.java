package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ReturnCommand extends Command {
	private Expression exp;
	public ReturnCommand(Expression exp) {
		this.exp = exp;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		return tab + "Return[ Expression[ " + exp.toString(level) + "] ] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitReturn(this, tree);
	}
	
	public Expression getExp() {
		return exp;
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof ReturnCommand)
			return true;
		return false;
	}
}
