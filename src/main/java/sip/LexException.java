package sip;

/**
 * Thrown when the Lexer meets a character it doesn't understand.
 *
 * Extends RuntimeException, so it's UNCHECKED — callers aren't forced
 * to catch it. (1Z0-808: checked vs unchecked exceptions — know the difference!)
 */
public class LexException extends RuntimeException {

    public LexException(String message) {
        super(message);
    }
}
