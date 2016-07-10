package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class BreakCommand extends Command {
	
	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		return tab + "[ BREAK ] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitBreak(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof BreakCommand)
			return true;
		return false;
	}
}
