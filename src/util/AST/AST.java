package util.AST;

import java.util.ArrayList;

import checker.SemanticException;
import checker.Visitor;

/**
 * AST class
 * @version 2010-september-04
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 */
public abstract class AST {

	public abstract String toString(int level);
	
	public abstract Object visit(Visitor v, ArrayList<AST> tree) throws SemanticException;

	public abstract boolean equals(AST ast);
}
