package sip;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> values = new HashMap<>();

    public void define(String name, Object value) {
        values.put(name, value); }

    public Object get(Token name) {
        String key = name.getText();

        if (values.containsKey(key))
            return values.get(key);
        else {
            throw new SipRuntimeException("Undefined variable '" + key + "' on line " + name.getLine());
        }
    }


}
