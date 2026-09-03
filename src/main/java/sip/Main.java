package sip;

import java.util.List;
import java.util.Scanner;

/**
 * The Sip REPL (Read-Eval-Print Loop).
 *
 * For now it only shows the tokens the Lexer produces — that's enough to
 * SEE your Milestone 1 work running. Once the parser and evaluator exist
 * (Milestones 3–4), this will print actual results.
 *
 * Run:  java sip.Main
 * Quit: type 'exit'
 */
public class Main {

    public static void main(String[] args) {

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
                System.out.println("tokens: " + tokens);
                // TODO Milestone 3: parse the tokens into an Expr tree
                // TODO Milestone 4: evaluate the tree and print the result
            } catch (LexException e) {
                System.out.println("error: " + e.getMessage());
            }
        }

        System.out.println("bye!");
    }
}
