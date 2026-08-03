package sip;
/**
 * Every kind of token the Sip language understands.
 * An enum is a fixed set of constants — perfect for token kinds.
 * (1Z0-808: enums, switch on enums)
 */
public enum TokenType {
    // Literals
    NUMBER,        // 42, 3.14
    STRING,        // "hello"
    IDENTIFIER,    // variable names: x, total

    // Operators
    PLUS,          // +
    MINUS,         // -
    STAR,          // *
    SLASH,         // /
    EQUALS,        // =   (assignment)

    // Comparison — Milestone 1: the lexer doesn't produce these yet!
    GREATER,       // >
    GREATER_EQUAL, // >=
    LESS,          // <
    LESS_EQUAL,    // <=
    EQUAL_EQUAL,   // ==
    BANG_EQUAL,    // !=

    // Grouping
    LEFT_PAREN,    // (
    RIGHT_PAREN,   // )

    // Keywords — Milestone 1
    LET, PRINT, IF, ELSE, TRUE, FALSE,

    // End of input marker
    EOF
}
