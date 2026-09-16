package sip;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for Sip. With a file argument it runs that file, otherwise
 * it starts the REPL.
 *
 * Either way the source goes through three stages:
 *
 *   Lexer      text to tokens    "5 + 3" becomes [NUMBER(5), PLUS, NUMBER(3)]
 *   Parser     tokens to a tree  nesting records precedence
 *   Evaluator  tree to a value   computes 8
 *
 * One Evaluator handles the whole session, so variables declared with let
 * are still there on the next line. Errors carry a line number. In the REPL
 * they do not end the session.
 *
 * Run:   java sip.Main
 *        java sip.Main program.sip
 * Quit:  type 'exit'
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            runFile(args[0]);
        } else {
            runRepl();
        }
    }

    /** Runs a whole .sip file. Only print produces output here. */
    private static void runFile(String path) {
        String source;
        try {
            source = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not read file: " + path);
            return;
        }

        try {
            List<Token> tokens = new Lexer(source).tokenize();
            List<Stmt> program = new Parser(tokens).parseProgram();
            Evaluator evaluator = new Evaluator(false);
            for (Stmt statement : program) {
                evaluator.execute(statement);
            }
        } catch (LexException | ParseException | SipRuntimeException e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    /**
     * Reads a line, runs it, prints the result, repeats.
     *
     * A statement can span several lines. While a block is still open the
     * lines are collected in a buffer and the prompt changes to '...', so
     * an if or while with braces can be typed or pasted across lines.
     */
    private static void runRepl() {
        Evaluator evaluator = new Evaluator();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sip 0.1. Type an expression, or 'exit' to quit.");

        StringBuilder buffer = new StringBuilder();

        while (true) {
            System.out.print(buffer.length() == 0 ? "> " : "... ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            String trimmed = line.trim();

            if (trimmed.equals("exit")) {
                break;
            }
            if (trimmed.isEmpty() && buffer.length() == 0) {
                continue;
            }

            buffer.append(line).append('\n');
            String source = buffer.toString();

            // An unclosed block means the statement is not finished yet.
            if (openBraces(source) > 0) {
                continue;
            }
            buffer.setLength(0);

            try {
                List<Token> tokens = new Lexer(source).tokenize();
                Stmt stmt = new Parser(tokens).parse();
                evaluator.execute(stmt);
            } catch (LexException | ParseException | SipRuntimeException e) {
                System.out.println("error: " + e.getMessage());
            }
        }

        System.out.println("bye!");
    }

    /**
     * How many blocks are still open. The lexer does the counting, so braces
     * inside a string do not confuse it. A lexing problem returns 0 so the
     * normal parse can report it properly.
     */
    private static int openBraces(String source) {
        try {
            int depth = 0;
            for (Token token : new Lexer(source).tokenize()) {
                if (token.getType() == TokenType.LEFT_BRACE) {
                    depth++;
                } else if (token.getType() == TokenType.RIGHT_BRACE) {
                    depth--;
                }
            }
            return depth;
        } catch (LexException e) {
            return 0;
        }
    }
}
