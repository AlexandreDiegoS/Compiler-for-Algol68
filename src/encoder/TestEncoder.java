package encoder;

import checker.Checker;
import parser.Parser;
import util.AST.AST;
import util.AST.Program;

/**
 * Test of the Encoder
 * @version 2016-june-
 * @discipline Compiladores
 * @authors 
 * 			Alexandre D S Silva
 * 			Bárbara M B de Carvalho
 * 			Thaís M de Almeida
 * @emails 
 * 			adss@ecomp.poli.br
 * 			bmbc@ecomp.poli.br
 * 			tma@ecomp.poli.br
 */

public class TestEncoder {
	public static void main(String[] args) {
		Parser parser = new Parser();
		AST ast = parser.parseProgram();
		Checker checker = new Checker();
		AST decoredAST = checker.check((Program) ast);
		Encoder encoder = new Encoder();
		encoder.encode((Program) decoredAST);
	}
}
