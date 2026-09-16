package sip;

public class StmtLet extends Stmt{
    private final Token name;
    private final Expr initializer;

    public StmtLet(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public Token getName() {
        return name;
    }

    public Expr getInitializer() {
        return initializer;
    }
}
