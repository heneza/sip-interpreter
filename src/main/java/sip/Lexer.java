package sip;

import java.util.ArrayList;
import java.util.List;

/**
 * The Lexer (also called tokenizer or scanner) reads raw source text
 * one character at a time and groups characters into Tokens.
 * "5 + 30"  →  [NUMBER(5), PLUS(+), NUMBER(30), EOF()]
 *
 * Handles: numbers, strings, identifiers, keywords, arithmetic and
 * comparison operators, parentheses, assignment, whitespace and newlines.
 */
public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int pos = 0;    // index of the character we're currently looking at
    private int line = 1;   // current line number, for error messages

    public Lexer(String source) {
        this.source = source;
    }


     // Main loop: scan one token at a time until we run out of characters.

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
                    case '+':
                        addToken(TokenType.PLUS, "+");
                        advance();
                        break;
                    case '-':
                        addToken(TokenType.MINUS, "-");
                        advance();
                        break;
                    case '*':
                        addToken(TokenType.STAR, "*");
                        advance();
                        break;
                    case '/':
                        addToken(TokenType.SLASH, "/");
                        advance();
                        break;
                    case '(':
                        addToken(TokenType.LEFT_PAREN, "(");
                        advance();
                        break;
                    case ')':
                        addToken(TokenType.RIGHT_PAREN, ")");
                        advance();
                        break;
                    case '=':
                        advance();
                        if (!atEnd() && peek() == '=') {
                            addToken(TokenType.EQUAL_EQUAL, "==");
                            advance();
                        } else {
                            addToken(TokenType.EQUALS, "=");
                        }
                        break;
                    case '>':
                        advance();
                        if (!atEnd() && peek() == '=') {
                            addToken(TokenType.GREATER_EQUAL, ">=");
                            advance();
                        } else {
                            addToken(TokenType.GREATER, ">");
                        }
                        break;
                    case '<':
                        advance();
                        if (!atEnd() && peek() == '=') {
                            addToken(TokenType.LESS_EQUAL, "<=");
                            advance();
                        } else {
                            addToken(TokenType.LESS, "<");
                        }
                        break;
                    case '!':
                        advance();
                        if (!atEnd() && peek() == '=') {
                            addToken(TokenType.BANG_EQUAL, "!=");
                            advance();
                        } else {
                            throw new LexException("Unexpected character '!' on line " + line);
                        }
                        break;
                    case '"':
                        string();
                        break;

                    default:
                        // A word starts with a letter (or underscore), so hand off
                        // to identifier(). Anything else is not valid Sip.
                        if (Character.isLetter(c) || c == '_') {
                            identifier();
                            break;
                        }
                        throw new LexException("Unexpected character '" + c + "' on line " + line);
                }
            }
        }
        addToken(TokenType.EOF, "");
        return tokens;
    }


    // Reads a whole number like 42 or 3.14 — study this as your template.

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

    /**
     * Reads a string literal: "hello".
     * The quotes are only markers — they are NOT part of the token's text.
     * So "hello" produces one token: STRING(hello).
     */
    private void string() {
        advance();          // consume the opening quote
        int start = pos;    // first character of the actual text

        while (!atEnd() && peek() != '"') {
            if (peek() == '\n') {
                line++;     // a string may span lines; keep the counter honest
            }
            advance();
        }

        // We stopped for one of two reasons: found the closing quote, or ran out.
        if (atEnd()) {
            throw new LexException("Unterminated string on line " + line);
        }

        addToken(TokenType.STRING, source.substring(start, pos));
        advance();          // consume the closing quote
    }

    /**
     * Reads a word: a variable name like total, or a keyword like let.
     * Same shape as number(), but for letters — and with one extra step:
     * once the word is read, we check whether it is a reserved keyword.
     */
    private void identifier() {
        int start = pos;

        // First char is already known to be a letter or '_'.
        // After that, digits are allowed too: myVar2 is a valid name.
        while (!atEnd() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            advance();
        }

        String word = source.substring(start, pos);
        addToken(keywordOrIdentifier(word), word);
    }

    /**
     * Decides whether a word is one of Sip's reserved keywords.
     * Note: switch on String compares with equals(), NOT ==.
     * (1Z0-808: == compares references, equals() compares contents.)
     */
    private TokenType keywordOrIdentifier(String word) {
        switch (word) {
            case "let":   return TokenType.LET;
            case "print": return TokenType.PRINT;
            case "if":    return TokenType.IF;
            case "else":  return TokenType.ELSE;
            case "true":  return TokenType.TRUE;
            case "false": return TokenType.FALSE;
            default:      return TokenType.IDENTIFIER;
        }
    }

    // ---- small helper methods ----

     // Look at the current character without consuming it.
    private char peek() {
        return source.charAt(pos);
    }

     // Move to the next character.
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
