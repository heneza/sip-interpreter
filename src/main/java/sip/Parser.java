package sip;

import java.util.List;
/**
  Turns a flat token list into an Expr tree (recursive descent).
  One method per precedence level, loosest first:
  expression → comparison → addition → multiplication → primary
  Each level calls the next, so tighter-binding operators are
  consumed first and end up deeper in the tree.
 */
public class Parser {

    private final List<Token> tokens;
    private int current = 0;   // index of the token we're looking at

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /** Entry point: parse one whole expression and insist nothing is left over. */
    public Expr parse() {
        Expr expr = expression();
        if (!atEnd()) {
            throw new ParseException("Unexpected '" + peek().getText()
                    + "' on line " + peek().getLine());
        }
        return expr;
    }

    private Expr expression() {
        return comparison();
    }

    /** Loosest level: a > b, a == b */
    private Expr comparison() {
        Expr expr = addition();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL,
                TokenType.LESS, TokenType.LESS_EQUAL,
                TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token operator = previous();
            Expr right = addition();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    /** a + b - c */
    private Expr addition() {
        Expr expr = multiplication();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    /** a * b / c — binds tighter, so it sits deeper in the tree */
    private Expr multiplication() {
        Expr expr = primary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = primary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    /** The atoms: a value, a name, or a parenthesised expression. */
    private Expr primary() {
        if (match(TokenType.NUMBER)) {
            return new Literal(Double.parseDouble(previous().getText()));
        }
        if (match(TokenType.STRING)) {
            return new Literal(previous().getText());
        }
        if (match(TokenType.TRUE)) {
            return new Literal(true);
        }
        if (match(TokenType.FALSE)) {
            return new Literal(false);
        }
        if (match(TokenType.IDENTIFIER)) {
            return new Variable(previous());
        }
        if (match(TokenType.LEFT_PAREN)) {
            Expr inner = expression();   // start over from the top
            if (!match(TokenType.RIGHT_PAREN)) {
                throw new ParseException("Expected ')' on line " + peek().getLine());
            }
            return inner;
        }
        throw new ParseException("Expected a value but found '" + peek().getText()
                + "' on line " + peek().getLine());
    }

    // ---- small helper methods ----

    /** If the current token is any of these types, consume it and return true. */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        return !atEnd() && peek().getType() == type;
    }

    private Token advance() {
        if (!atEnd()) {
            current++;
        }
        return previous();
    }

    private boolean atEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}