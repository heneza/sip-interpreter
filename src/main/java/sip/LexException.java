package sip;

/**
 * Thrown when the lexer meets a character it doesn't understand.
 * Unchecked, so callers are not forced to catch it.
 */
public class LexException extends RuntimeException {

    public LexException(String message) {
        super(message);
    }
}
