package parser;

/**
 * Test Parser
 * @version 2016-april-26
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

public class TestParser {
	public static void main(String[] args) {
		Parser test = new Parser();
		System.out.println(test.parseProgram().toString(1));
	}
}
