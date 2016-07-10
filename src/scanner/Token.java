package scanner;

/**
 * Token class
 * @version 2016-april-16
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

public class Token {

	// The token kind
	private int kind;
	// The token spelling
	private String spelling;
	// The line and column that the token was found
	private int line, column;
	
	/**
	 * Default constructor
	 * @param kind
	 * @param spelling
	 * @param line
	 * @param column
	 */
	public Token(int kind, String spelling, int line, int column) {
		this.kind = kind;
		this.spelling = spelling;
		this.line = line;
		this.column = column;
	}

	/**
	 * Returns token kind
	 * @return
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * Returns token spelling
	 * @return
	 */
	public String getSpelling() {
		return spelling;
	}

	/**
	 * Returns the line where the token was found
	 * @return
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column where the token was found
	 * @return
	 */	
	public int getColumn() {
		return column;
	}
	
	public String toString() {
		String type = null;
		switch (getKind()) {
		case 0:
			type = "INT";
			break;
		case 1:
			type = "IF";
			break;
		case 2:
			type = "ELSE";
			break;
		case 3:
			type = "THEN";
			break;
		case 4:
			type = "DO";
			break;
		case 5:
			type = "FI";
			break;
		case 6:
			type = "OD";
			break;
		case 7:
			type = "PROC";
			break;
		case 8:
			type = "WHILE";
			break;
		case 9:
			type = "BEGIN";
			break;
		case 10:
			type = "END";
			break;
		case 11:
			type = "BOOL";
			break;
		case 12:
			type = "ID";
			break;
		case 13:
			type = "NUMBER";
			break;
		case 14:
			type = "BREAK";
			break;
		case 15:
			type = "CONTINUE";
			break;
		case 16:
			type = "PRINT";
			break;
		case 17:
			type = "RETURN";
			break;
		case 18:
			type = "ASG";
			break;
		case 19:
			type = "ADD";
			break;
		case 20:
			type = "SUB";
			break;
		case 21:
			type = "MUL";
			break;
		case 22:
			type = "DIV";
			break;
		case 23:
			type = "LP";
			break;
		case 24:
			type = "RP";
			break;
		case 25:
			type = "SC";
			break;
		case 26:
			type = "EQ";
			break;
		case 27:
			type = "NEQ";
			break;
		case 28:
			type = "TRUE";
			break;
		case 29:
			type = "FALSE";
			break;
		case 30:
			type = "VOID";
			break;
		case 31:
			type = "CO";
			break;
		case 32:
			type = "GT";
			break;
		case 33:
			type = "GE";
			break;
		case 34:
			type = "LT";
			break;
		case 35:
			type = "LE";
			break;
		case 36:
			type = "SEP";
			break;
		case 37:
			type = "EOT";
			break;
		}
		
		return "Token: " + spelling + " -> type:" + type + " in : (Line: " + getLine() + "  Column:" + getColumn() + " )";
	}
}
