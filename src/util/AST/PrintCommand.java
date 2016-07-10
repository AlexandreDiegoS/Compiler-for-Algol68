package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class PrintCommand extends Command {
	private Expression exp;
	public PrintCommand(Expression exp) {
		this.exp = exp;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		return tab + "Print[ Expression[ " + exp.toString(level) + "] ] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitPrint(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof PrintCommand)
			return true;
		return false;
	}

	public Expression getExp() {
		return exp;
	}

}
