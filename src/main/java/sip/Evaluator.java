package sip;

/**
 * Walks an Expr tree and computes its value.
 *
 * Version 1 (Milestone 4a): one evaluate() method that asks each node
 * "what kind are you?" with instanceof, then casts to get at its data.
 * Step B refactors this to polymorphism — compare the two.
 */
public class Evaluator {
    private final Environment environment = new Environment();

    /** Returns the value of an expression: a Double, String or Boolean. */
    public Object evaluate(Expr expr) {
        if (expr instanceof Literal) {
            Literal literal = (Literal) expr;     // cast to reach getValue()
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
        Object left = evaluate(expr.getLeft());     // recursion
        Object right = evaluate(expr.getRight());   // recursion
        Token operator = expr.getOperator();

        // Note: case labels in a switch over an enum are UNqualified —
        // write PLUS, not TokenType.PLUS, or it won't compile. (1Z0-808)
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
            return (Double) value;   // unboxing: Double → double
        }
        throw new SipRuntimeException("Operator '" + operator.getText()
                + "' needs numbers, but got '" + stringify(value)
                + "' on line " + operator.getLine());
    }
    /** Runs a statement. Statements produce no value — they cause effects. */
    public void execute(Stmt stmt) {
        if (stmt instanceof StmtLet) {
            StmtLet let = (StmtLet) stmt;
            Object value = evaluate(let.getInitializer());
            environment.define(let.getName().getText(), value);
            return;
        }
        if (stmt instanceof StmtPrint) {
            StmtPrint print = (StmtPrint) stmt;
            System.out.println(stringify(evaluate(print.getExpression())));
            return;
        }
        if (stmt instanceof StmtExpression) {
            StmtExpression exprStmt = (StmtExpression) stmt;
            System.out.println(stringify(evaluate(exprStmt.getExpression())));
            return;
        }
        throw new SipRuntimeException("Don't know how to run this statement");
    }

    /** equals() compares contents; == would compare references. */
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
