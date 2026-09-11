package sip;

/** Thrown when the tokens don't form a valid Sip expression. */
public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }
}
