package sip;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Lexer. Run with: mvn test
 *
 * The first group passes already. The @Disabled ones are YOUR Milestone 1 —
 * implement the feature, remove the @Disabled line, and make it green.
 */
class LexerTest {

    // ---------- These pass with the starter code ----------

    @Test
    void tokenizesSingleNumber() {
        List<Token> tokens = new Lexer("42").tokenize();
        assertEquals(2, tokens.size()); // NUMBER + EOF
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals("42", tokens.get(0).getText());
    }

    @Test
    void tokenizesDecimalNumber() {
        List<Token> tokens = new Lexer("3.14").tokenize();
        assertEquals("3.14", tokens.get(0).getText());
    }

    @Test
    void tokenizesArithmetic() {
        List<Token> tokens = new Lexer("5 + 3 * 2").tokenize();
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(TokenType.PLUS, tokens.get(1).getType());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals(TokenType.STAR, tokens.get(3).getType());
        assertEquals(TokenType.NUMBER, tokens.get(4).getType());
        assertEquals(TokenType.EOF, tokens.get(5).getType());
    }

    @Test
    void tokenizesParentheses() {
        List<Token> tokens = new Lexer("(1 + 2)").tokenize();
        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(4).getType());
    }

    @Test
    void throwsOnUnexpectedCharacter() {
        // assertThrows: the test PASSES if the exception IS thrown
        assertThrows(LexException.class, () -> new Lexer("5 # 3").tokenize());
    }

    // ---------- Milestone 1: implement, then remove @Disabled ----------

    @Disabled("Milestone 1: comparison operators")
    @Test
    void tokenizesComparisons() {
        List<Token> tokens = new Lexer("a >= 5").tokenize();
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
        assertEquals(TokenType.GREATER_EQUAL, tokens.get(1).getType());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
    }

    @Disabled("Milestone 1: == vs =")
    @Test
    void distinguishesAssignFromEquality() {
        List<Token> tokens = new Lexer("x == 1").tokenize();
        assertEquals(TokenType.EQUAL_EQUAL, tokens.get(1).getType());

        tokens = new Lexer("x = 1").tokenize();
        assertEquals(TokenType.EQUALS, tokens.get(1).getType());
    }

    @Disabled("Milestone 1: identifiers and keywords")
    @Test
    void tokenizesKeywordsAndIdentifiers() {
        List<Token> tokens = new Lexer("let total = 5").tokenize();
        assertEquals(TokenType.LET, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("total", tokens.get(1).getText());
    }

    @Disabled("Milestone 1: string literals")
    @Test
    void tokenizesStringLiteral() {
        List<Token> tokens = new Lexer("\"hello\"").tokenize();
        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("hello", tokens.get(0).getText());
    }

    @Disabled("Milestone 1: unterminated string should fail")
    @Test
    void throwsOnUnterminatedString() {
        assertThrows(LexException.class, () -> new Lexer("\"oops").tokenize());
    }
}
