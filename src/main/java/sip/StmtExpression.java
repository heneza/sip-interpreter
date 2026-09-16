package sip;

public class StmtExpression extends Stmt {
    private final Expr expression;

    public StmtExpression(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }
}
