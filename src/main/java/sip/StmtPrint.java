package sip;

public class StmtPrint extends Stmt {
    private final Expr expression;

    public StmtPrint(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

}
