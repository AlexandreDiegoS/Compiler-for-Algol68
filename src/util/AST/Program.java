package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class Program extends AST {
	private ArrayList<ProgramCommand> commands;
	
	public Program(ArrayList<ProgramCommand> commands) {
		this.commands = commands;
	}

	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		level++;
		String str = "Program{\n" + tab + "Commands[ \n";
		for(int i = 0; i < commands.size(); i++)
			str += commands.get(i).toString(level) + "\n";
		str += tab + "] \n";
		return str += "} ";
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitProgram(this, tree);
	}

	public ArrayList<ProgramCommand> getCommands() {
		return commands;
	}
	
	@Override
	public boolean equals(AST ast) {
		if(ast instanceof Program)
			return true;
		return false;
	}
}
