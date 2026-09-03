package sip;

/** A reference to a variable by name: x, total. */
public class Variable extends Expr {

    private final Token name;

    public Variable(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.getText();
    }
}