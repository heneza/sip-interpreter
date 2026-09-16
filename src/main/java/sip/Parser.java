package sip;

import java.util.ArrayList;
import java.util.List;
/**
 * Turns a flat token list into statements, using recursive descent.
 *
 * Parsing starts at statement() and descends into expressions. The
 * expression methods run one per precedence level, loosest first:
 * comparison, addition, multiplication, primary. Each level calls the
 * next, so tighter operators are consumed first and sit deeper in the
 * tree. That is why 5 + 3 * 2 gives 11 and not 16.
 */
public class Parser {

    private final List<Token> tokens;
    private int current = 0;   // index of the token we're looking at

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /** Parses a whole program: every statement until the end of input. */
    public List<Stmt> parseProgram() {
        List<Stmt> statements = new ArrayList<>();
        while (!atEnd()) {
            statements.add(statement());
        }
        return statements;
    }

    /** Parses one statement and checks that nothing is left over. */
    public Stmt parse() {
        Stmt stmt = statement();
        if (!atEnd()) {
            throw new ParseException("Unexpected '" + peek().getText()
                    + "' on line " + peek().getLine());
        }
        return stmt;
    }
    // ---- statements ----

    /** One statement. The first token decides which kind. */
    private Stmt statement() {
        if (match(TokenType.LET)) {
            return letStatement();
        }
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        if (match(TokenType.IF)) {
            return ifStatement();
        }
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        if (match(TokenType.LEFT_BRACE)) {
            return new StmtBlock(block());
        }
        // An identifier followed by '=' is an assignment. Looking one token
        // ahead is what separates 'x = 1' from the expression 'x'.
        if (check(TokenType.IDENTIFIER) && checkNext(TokenType.EQUALS)) {
            return assignStatement();
        }
        return expressionStatement();
    }

    /** x = 15 */
    private Stmt assignStatement() {
        Token name = advance();
        consume(TokenType.EQUALS, "Expected '=' after the variable name");
        Expr value = expression();
        return new StmtAssign(name, value);
    }

    /** if (condition) statement [else statement] */
    private Stmt ifStatement() {
        Token keyword = previous();
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'if'");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after the condition");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }
        return new StmtIf(keyword, condition, thenBranch, elseBranch);
    }

    /** while (condition) statement */
    private Stmt whileStatement() {
        Token keyword = previous();
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'while'");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after the condition");
        Stmt body = statement();
        return new StmtWhile(keyword, condition, body);
    }

    /** The statements between { and }. The '{' is already consumed. */
    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !atEnd()) {
            statements.add(statement());
        }
        consume(TokenType.RIGHT_BRACE, "Expected '}' to close the block");
        return statements;
    }

    private Stmt letStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expected a variable name after 'let'");
        consume(TokenType.EQUALS, "Expected '=' after the variable name");
        Expr value = expression();
        return new StmtLet(name, value);
    }

    private Stmt printStatement() {
        Expr value = expression();
        return new StmtPrint(value);
    }

    private Stmt expressionStatement() {
        Expr value = expression();
        return new StmtExpression(value);
    }

    private Expr expression() {
        return comparison();
    }

    /** Comparison: a > b, a == b */
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

    /** Addition and subtraction: a + b - c */
    private Expr addition() {
        Expr expr = multiplication();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    /** Multiplication and division. Binds tighter than addition. */
    private Expr multiplication() {
        Expr expr = primary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = primary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    /** A value, a name, or a parenthesised expression. */
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
            Expr inner = expression();   // parentheses restart at the top
            if (!match(TokenType.RIGHT_PAREN)) {
                throw new ParseException("Expected ')' on line " + peek().getLine());
            }
            return inner;
        }
        throw new ParseException("Expected a value but found '" + peek().getText()
                + "' on line " + peek().getLine());
    }

    // ---- helpers ----

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw new ParseException(message + " on line " + peek().getLine());
    }

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

    /** Looks one token past the current one, without consuming anything. */
    private boolean checkNext(TokenType type) {
        if (atEnd() || current + 1 >= tokens.size()) {
            return false;
        }
        return tokens.get(current + 1).getType() == type;
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