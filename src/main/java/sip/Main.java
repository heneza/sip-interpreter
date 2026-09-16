package sip;

import java.util.List;
import java.util.Scanner;

/**
 * Sip — a small scripting language, interpreted.

 * This is the REPL (Read-Eval-Print Loop): it reads a line you type,
 * runs it, prints the result, and repeats. Each line travels through
 * three stages, one class per stage:
     1. Lexer      text  → tokens   "5 + 3"  → [NUMBER(5), PLUS, NUMBER(3)]
 *   2. Parser     tokens → tree    a structure where nesting encodes
                                    precedence, so 5 + 3 * 2 groups the
                                    multiplication as a subtree
 *   3. Evaluator  tree  → value    walks the tree and computes 11

 * Splitting the work this way is how real interpreters and compilers are
 * built: each stage has one job and hands a simpler representation to the
 * next. Errors from any stage carry a line number and are reported here
 * without stopping the session.

 * Run:  java sip.Main
 * Quit: type 'exit'                                                     */

public class Main {

    public static void main(String[] args) {

        Evaluator evaluator = new Evaluator();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sip 0.1 — type an expression, or 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) break;
            if (input.isEmpty()) continue;

            try {
                List<Token> tokens = new Lexer(input).tokenize();
                Stmt stmt = new Parser(tokens).parse();
                evaluator.execute(stmt);
            } catch (LexException | ParseException | SipRuntimeException e) {
                System.out.println("error: " + e.getMessage());
            }
        }

        System.out.println("bye!");
    }
}
