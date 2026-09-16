package sip;

/** while (condition) body */
public class StmtWhile extends Stmt {

    private final Token keyword;
    private final Expr condition;
    private final Stmt body;

    public StmtWhile(Token keyword, Expr condition, Stmt body) {
        this.keyword = keyword;
        this.condition = condition;
        this.body = body;
    }

    public Token getKeyword() {
        return keyword;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getBody() {
        return body;
    }
}
