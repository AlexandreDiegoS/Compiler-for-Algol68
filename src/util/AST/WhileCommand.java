package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class WhileCommand extends Command {
	private Expression exp;
	private ArrayList<Command> lCmd;
	
	public WhileCommand(Expression exp, ArrayList<Command> lCmd) {
		this.exp = exp;
		this.lCmd = lCmd;
	}
	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab + "While[ Expression[ " + exp.toString(level) + "] Commands[ \n";
		level++;
		for(int i = 0; i<lCmd.size();i++)
			str += lCmd.get(i).toString(level) + "\n";
		str += tab + "] ] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitWhile(this, tree);
	}
	public Expression getExp() {
		return exp;
	}
	public ArrayList<Command> getlCmd() {
		return lCmd;
	}
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof WhileCommand)
			return true;
		return false;
	}

}
