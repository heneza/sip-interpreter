package sip;

/**
 * One token from the source. For "5 + 3" the lexer produces
 * Token(NUMBER, "5"), Token(PLUS, "+"), Token(NUMBER, "3").
 *
 * Immutable: fields are private final and there are no setters.
 */
public class Token {

    private final TokenType type;
    private final String text;   // the exact characters from the source
    private final int line;      // which line it appeared on (for error messages)

    public Token(TokenType type, String text, int line) {
        this.type = type;
        this.text = text;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return type + "(" + text + ")";
    }
}
