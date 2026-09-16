package sip;

/** Changes an existing variable: x = 15 */
public class StmtAssign extends Stmt {

    private final Token name;
    private final Expr value;

    public StmtAssign(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }
}
