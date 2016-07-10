package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

public class FunctionDeclaration extends ProgramCommand{
	private Identifier id;
	private ArrayList<VariableDeclaration> parameters;
	private int numberOfParameters;
	public int getNumberOfParameters() {
		return numberOfParameters;
	}

	private Type type;
	private ArrayList<Command> commands;
	
	public FunctionDeclaration(Identifier id, ArrayList<VariableDeclaration> parameters, Type type,
			ArrayList<Command> commands) {
		this.id = id;
		this.parameters = parameters;
		for(VariableDeclaration vDec : parameters){
			this.numberOfParameters += vDec.getlId().size(); 	
		}
		this.type = type;
		this.commands = commands;
	}

	@Override
	public String toString(int level) {
		String tab = "";
		for(int i = 0; i < level; i++)
			tab += "\t";
		String str = tab + "FunctionDecl[ ID[ " + id.toString(level) + "] Parameters[ ";
		for(int i=0; i<parameters.size(); i++)  
			str += "( " + parameters.get(i).toString(level) + ") "; 
		str += "] Type[ " + type.toString(level) + "] ";
		level++;
		str += "Commands[ \n";
		for(int i=0; i<commands.size(); i++)  
			str += commands.get(i).toString(level) + "\n";
		str += tab + "] ] ";
		return str;
	}
	
	public Object visit(Visitor visit, ArrayList<AST> tree) throws SemanticException{
		return visit.visitFunctionDeclaration(this, tree);
	}
	
	public Identifier getId() {
		return id;
	}

	public ArrayList<VariableDeclaration> getParameters() {
		return parameters;
	}

	public Type getType() {
		return type;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	@Override
	public boolean equals(AST ast) {
		if(ast instanceof FunctionDeclaration)
			return true;
		return false;
	}

}
