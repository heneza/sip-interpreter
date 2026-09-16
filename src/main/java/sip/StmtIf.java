package sip;

/**
 * if (condition) thenBranch else elseBranch
 *
 * elseBranch is null when there is no else. The keyword token is kept so
 * errors about the condition can report a line number.
 */
public class StmtIf extends Stmt {

    private final Token keyword;
    private final Expr condition;
    private final Stmt thenBranch;
    private final Stmt elseBranch;

    public StmtIf(Token keyword, Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.keyword = keyword;
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Token getKeyword() {
        return keyword;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getThenBranch() {
        return thenBranch;
    }

    public Stmt getElseBranch() {
        return elseBranch;
    }
}
