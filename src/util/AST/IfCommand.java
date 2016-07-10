package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class IfCommand extends Command {
	private Expression exp;
	private ArrayList<Command> lCmd;
	private ArrayList<Command> lElseCmd;
	
	public IfCommand(Expression exp, ArrayList<Command> lCmd, ArrayList<Command> lElseCmd) {
		this.exp = exp;
		this.lCmd = lCmd;
		this.lElseCmd = lElseCmd;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab + "If[ Expression[ " + exp.toString(level) + "] ] Commands[ \n";
		level++;
		for(int i=0; i<lCmd.size(); i++)
			str += lCmd.get(i).toString(level) + "\n";
		str += tab + "] ";
		if (lElseCmd.size() > 0) {
			str += "ElseCommands[ \n";
			for(int i=0; i<lElseCmd.size(); i++)
				str += lElseCmd.get(i).toString(level) + "\n";
			str += tab + "] ";
		}
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitIf(this, tree);
	}

	public Expression getExp() {
		return exp;
	}
	
	public ArrayList<Command> getlCmd() {
		return lCmd;
	}

	public ArrayList<Command> getlElseCmd() {
		return lElseCmd;
	}	
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof IfCommand)
			return true;
		return false;
	}
}
