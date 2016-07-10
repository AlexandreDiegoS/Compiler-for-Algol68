package parser;

/**
 * This class contains codes for each grammar terminal
 * 
 * @version 2016-april-24
 * @discipline Compiladores
 * @author Gustavo H P Carvalho
 * @email gustavohpcarvalho@ecomp.poli.br
 * @authors 
 * 			Alexandre D S Silva
 * 			Bárbara M B de Carvalho
 * 			Thaís M de Almeida
 * @emails 
 * 			adss@ecomp.poli.br
 * 			bmbc@ecomp.poli.br
 * 			tma@ecomp.poli.br
 */

public class GrammarSymbols {
	// Language terminals (starts from 0)
	public static final int INT = 0, IF = 1, ELSE = 2, THEN = 3, DO = 4, FI = 5,
							OD = 6, PROC = 7, WHILE = 8, BEGIN = 9, END = 10, BOOL = 11,
							ID = 12, NUMBER = 13, BREAK = 14, CONTINUE = 15, PRINT =16,
							RETURN = 17, ASG = 18, ADD = 19, SUB = 20, MUL = 21, DIV = 22,
							LP = 23, RP = 24, SC = 25, EQ = 26, NEQ = 27, TRUE = 28, 
							FALSE = 29, VOID = 30, CO = 31, GT = 32, GE = 33, LT = 34, 
							LE = 35, SEP = 36, EOT = 37;
}
