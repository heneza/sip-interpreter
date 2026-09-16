package sip;

import java.util.List;

/**
 * Runs a parsed program.
 *
 * evaluate(Expr) returns a value. execute(Stmt) causes an effect and
 * returns nothing. The environment field holds the variables, so they
 * survive between statements. It is swapped temporarily while a block
 * runs, which is how block scope works.
 *
 * echo is true for the REPL, where a bare expression prints its value,
 * and false for script files, where only print produces output.
 */
public class Evaluator {

    private Environment environment = new Environment();
    private final boolean echo;

    public Evaluator() {
        this(true);
    }

    public Evaluator(boolean echo) {
        this.echo = echo;
    }

    /** Returns the value of an expression: a Double, String or Boolean. */
    public Object evaluate(Expr expr) {
        if (expr instanceof Literal) {
            Literal literal = (Literal) expr;
            return literal.getValue();
        }
        if (expr instanceof Binary) {
            return evaluateBinary((Binary) expr);
        }
        if (expr instanceof Variable) {
            Variable variable = (Variable) expr;
            return environment.get(variable.getName());
        }
        throw new SipRuntimeException("Don't know how to evaluate this expression");
    }

    /** Computes both sides first, then applies the operator. */
    private Object evaluateBinary(Binary expr) {
        Object left = evaluate(expr.getLeft());
        Object right = evaluate(expr.getRight());
        Token operator = expr.getOperator();

        // Case labels for an enum switch are written unqualified.
        switch (operator.getType()) {
            case PLUS:
                // '+' means concatenation if either side is text
                if (left instanceof String || right instanceof String) {
                    return stringify(left) + stringify(right);
                }
                return toNumber(left, operator) + toNumber(right, operator);
            case MINUS:
                return toNumber(left, operator) - toNumber(right, operator);
            case STAR:
                return toNumber(left, operator) * toNumber(right, operator);
            case SLASH:
                double divisor = toNumber(right, operator);
                if (divisor == 0) {
                    throw new SipRuntimeException("Division by zero on line "
                            + operator.getLine());
                }
                return toNumber(left, operator) / divisor;
            case GREATER:
                return toNumber(left, operator) > toNumber(right, operator);
            case GREATER_EQUAL:
                return toNumber(left, operator) >= toNumber(right, operator);
            case LESS:
                return toNumber(left, operator) < toNumber(right, operator);
            case LESS_EQUAL:
                return toNumber(left, operator) <= toNumber(right, operator);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case BANG_EQUAL:
                return !isEqual(left, right);
            default:
                throw new SipRuntimeException("Unknown operator '"
                        + operator.getText() + "' on line " + operator.getLine());
        }
    }

    /** Unwraps a Double, or reports a clear error if the value isn't a number. */
    private double toNumber(Object value, Token operator) {
        if (value instanceof Double) {
            return (Double) value;
        }
        throw new SipRuntimeException("Operator '" + operator.getText()
                + "' needs numbers, but got '" + stringify(value)
                + "' on line " + operator.getLine());
    }
    /** Runs a statement. Statements produce no value. */
    public void execute(Stmt stmt) {
        if (stmt instanceof StmtLet) {
            StmtLet let = (StmtLet) stmt;
            environment.define(let.getName(), evaluate(let.getInitializer()));
            return;
        }
        if (stmt instanceof StmtAssign) {
            StmtAssign assign = (StmtAssign) stmt;
            environment.assign(assign.getName(), evaluate(assign.getValue()));
            return;
        }
        if (stmt instanceof StmtPrint) {
            StmtPrint print = (StmtPrint) stmt;
            System.out.println(stringify(evaluate(print.getExpression())));
            return;
        }
        if (stmt instanceof StmtExpression) {
            StmtExpression exprStmt = (StmtExpression) stmt;
            Object value = evaluate(exprStmt.getExpression());
            if (echo) {
                System.out.println(stringify(value));
            }
            return;
        }
        if (stmt instanceof StmtBlock) {
            StmtBlock block = (StmtBlock) stmt;
            executeBlock(block.getStatements(), new Environment(environment));
            return;
        }
        if (stmt instanceof StmtIf) {
            StmtIf ifStmt = (StmtIf) stmt;
            if (asBoolean(evaluate(ifStmt.getCondition()), ifStmt.getKeyword())) {
                execute(ifStmt.getThenBranch());
            } else if (ifStmt.getElseBranch() != null) {
                execute(ifStmt.getElseBranch());
            }
            return;
        }
        if (stmt instanceof StmtWhile) {
            StmtWhile whileStmt = (StmtWhile) stmt;
            while (asBoolean(evaluate(whileStmt.getCondition()), whileStmt.getKeyword())) {
                execute(whileStmt.getBody());
            }
            return;
        }
        throw new SipRuntimeException("Don't know how to run this statement");
    }

    /**
     * Runs the statements of a block in their own scope.
     * The previous environment is restored in a finally block, so it comes
     * back even if a statement throws.
     */
    private void executeBlock(List<Stmt> statements, Environment blockEnvironment) {
        Environment previous = this.environment;
        try {
            this.environment = blockEnvironment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    /** Conditions must be true or false. Numbers and strings are rejected. */
    private boolean asBoolean(Object value, Token keyword) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new SipRuntimeException("The condition of '" + keyword.getText()
                + "' must be true or false, but was '" + stringify(value)
                + "' on line " + keyword.getLine());
    }

    /** Uses equals() so contents are compared, not references. */
    private boolean isEqual(Object a, Object b) {
        if (a == null || b == null) {
            return a == b;
        }
        return a.equals(b);
    }

    /** Prints 11 rather than 11.0, and leaves text and booleans alone. */
    public static String stringify(Object value) {
        if (value == null) {
            return "nothing";
        }
        String text = String.valueOf(value);
        if (value instanceof Double && text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

}
