package sip;

import java.util.List;

/**
 * A group of statements between { and }.
 *
 * Runs in its own scope, so variables declared inside are gone afterwards.
 */
public class StmtBlock extends Stmt {

    private final List<Stmt> statements;

    public StmtBlock(List<Stmt> statements) {
        this.statements = statements;
    }

    public List<Stmt> getStatements() {
        return statements;
    }
}
