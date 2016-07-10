package scanner;

import parser.GrammarSymbols;

/**
 * Test Scanner
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
public class TestScanner {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner();
		Token a;
		do {
			a = scan.getNextToken();
			System.out.println(a);
		} while (a.getKind()!= GrammarSymbols.EOT);

	}

}