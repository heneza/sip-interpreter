package sip;

/**
 * One token produced by the Lexer. For the input "5 + 3" the lexer
 * produces: Token(NUMBER, "5"), Token(PLUS, "+"), Token(NUMBER, "3").
 * This class is IMMUTABLE: all fields are private final and there are
 * no setters. Once created, a Token never changes.
 * (1Z0-808: encapsulation, final, constructors)
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
