package sip;

import java.util.ArrayList;
import java.util.List;

/**
 * The Lexer (also called tokenizer or scanner) reads raw source text
 * one character at a time and groups characters into Tokens.
 *   "5 + 30"  →  [NUMBER(5), PLUS(+), NUMBER(30), EOF()]
 * ALREADY WORKING: numbers, + - * /, parentheses, =, whitespace.
 * YOUR JOB (Milestone 1): comparisons, identifiers, keywords, strings.
 * Follow the pattern of the existing code — then un-@Disable the tests.
 */
public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int pos = 0;    // index of the character we're currently looking at
    private int line = 1;   // current line number, for error messages

    public Lexer(String source) {
        this.source = source;
    }

    /** Main loop: scan one token at a time until we run out of characters. */
    public List<Token> tokenize() {
        while (!atEnd()) {
            char c = peek();

            if (c == ' ' || c == '\t' || c == '\r') {
                advance(); // skip whitespace
            } else if (c == '\n') {
                line++;
                advance();
            } else if (Character.isDigit(c)) {
                number();
            } else {
                switch (c) {
                    case '+': addToken(TokenType.PLUS, "+"); advance(); break;
                    case '-': addToken(TokenType.MINUS, "-"); advance(); break;
                    case '*': addToken(TokenType.STAR, "*"); advance(); break;
                    case '/': addToken(TokenType.SLASH, "/"); advance(); break;
                    case '(': addToken(TokenType.LEFT_PAREN, "("); advance(); break;
                    case ')': addToken(TokenType.RIGHT_PAREN, ")"); advance(); break;
                    case '=':
                        // TODO Milestone 1: if the NEXT char is also '=',
                        // this is EQUAL_EQUAL, not assignment.
                        // Hint: advance(); then check peek() == '='
                        addToken(TokenType.EQUALS, "=");
                        advance();
                        break;

                    // TODO Milestone 1: handle '>' and '>=' (GREATER / GREATER_EQUAL)
                    // TODO Milestone 1: handle '<' and '<=' (LESS / LESS_EQUAL)
                    // TODO Milestone 1: handle '!' followed by '=' (BANG_EQUAL)
                    // TODO Milestone 1: handle '"' → call a new method string()

                    default:
                        // TODO Milestone 1: if Character.isLetter(c), call a new
                        // method identifier() — like number() but for letters.
                        // After reading the word, check if it's a keyword
                        // ("let" → LET, "print" → PRINT, "if" → IF, "else" → ELSE,
                        //  "true" → TRUE, "false" → FALSE), otherwise IDENTIFIER.
                        throw new LexException("Unexpected character '" + c + "' on line " + line);
                }
            }
        }
        addToken(TokenType.EOF, "");
        return tokens;
    }

    /** Reads a whole number like 42 or 3.14 — study this as your template. */
    private void number() {
        int start = pos;
        while (!atEnd() && Character.isDigit(peek())) {
            advance();
        }
        // optional decimal part
        if (!atEnd() && peek() == '.' && pos + 1 < source.length()
                && Character.isDigit(source.charAt(pos + 1))) {
            advance(); // consume the '.'
            while (!atEnd() && Character.isDigit(peek())) {
                advance();
            }
        }
        addToken(TokenType.NUMBER, source.substring(start, pos));
    }

    // ---- small helper methods ----

    /** Look at the current character without consuming it. */
    private char peek() {
        return source.charAt(pos);
    }

    /** Move to the next character. */
    private void advance() {
        pos++;
    }

    private boolean atEnd() {
        return pos >= source.length();
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text, line));
    }
}
