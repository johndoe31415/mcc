/**
 * Token class of the Lexer
 *  
 */
public final class Yytoken {

    private final int line;

    private final String text;

    private final int type;

    /**
     * Creates a new Token
     * 
     * @param type
     *            subtype of the token
     * @param text
     *            literal text of the token as identified by the lexer
     * @param line
     *            location of the token in terms of the line number
     */
    public Yytoken(int type, String text, int line) {
        this.line = line;
        this.text = text;
        this.type = type;
    }

    /**
     * Returns the line number in which the token occured
     * 
     * @return line number
     */
    public final int line() {
        return line;
    }

    /**
     * Returns the text of the token
     * 
     * @return literal text of the token
     */
    public final String text() {
        return text;
    }

    /**
     * Returns the type of the token
     * 
     * @return type of the token
     */
    public final int type() {
        return type;
    }
}
