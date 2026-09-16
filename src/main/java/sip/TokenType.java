package sip;
/** Every kind of token the Sip language understands. */
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

    // Comparison
    GREATER,       // >
    GREATER_EQUAL, // >=
    LESS,          // <
    LESS_EQUAL,    // <=
    EQUAL_EQUAL,   // ==
    BANG_EQUAL,    // !=

    // Grouping
    LEFT_PAREN,    // (
    RIGHT_PAREN,   // )
    LEFT_BRACE,    // {
    RIGHT_BRACE,   // }

    // Keywords
    LET, PRINT, IF, ELSE, WHILE, TRUE, FALSE,

    // End of input marker
    EOF
}
