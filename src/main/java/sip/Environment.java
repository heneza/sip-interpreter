package sip;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores variables by name.
 *
 * Each block gets its own Environment with a link to the one outside it.
 * A lookup checks the innermost scope first and walks outwards, so a
 * variable declared inside a block disappears when the block ends.
 */
public class Environment {

    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;

    /** The global scope. */
    public Environment() {
        this.enclosing = null;
    }

    /** A block scope sitting inside another one. */
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    /** Declares a new variable. Fails if the name is taken in this scope. */
    public void define(Token name, Object value) {
        String key = name.getText();
        if (values.containsKey(key)) {
            throw new SipRuntimeException("Variable '" + key
                    + "' is already defined on line " + name.getLine());
        }
        values.put(key, value);
    }

    /** Changes an existing variable. Fails if it was never declared. */
    public void assign(Token name, Object value) {
        String key = name.getText();
        if (values.containsKey(key)) {
            values.put(key, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new SipRuntimeException("Undefined variable '" + key
                + "' on line " + name.getLine() + ". Use 'let' to declare it first.");
    }

    public Object get(Token name) {
        String key = name.getText();
        if (values.containsKey(key)) {
            return values.get(key);
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new SipRuntimeException("Undefined variable '" + key
                + "' on line " + name.getLine());
    }
}
