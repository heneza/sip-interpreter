package sip;

/** Two expressions joined by an operator: 5 + 3, x >= 10. */
public class Binary extends Expr {

    private final Expr left;
    private final Token operator;  // the whole Token, so we keep the line number
    private final Expr right;

    public Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }

    /** Prints the tree in bracket form: (+ 5 (* 3 2)) */
    @Override
    public String toString() {
        return "(" + operator.getText() + " " + left + " " + right + ")";
    }
}
