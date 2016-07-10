package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class ContinueCommand extends Command {

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		return tab + "[ CONTINUE ] ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitContinue(this, tree);
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof ContinueCommand)
			return true;
		return false;
	}

}
