# Sip

A small scripting language and its interpreter, written in plain Java.
No parser generators, no external libraries.

```
> let price = 42
> let tax = price * 0.2
> print "total: " + (price + tax)
total: 50.4
> price >= 40
true
```

## Why I built it

Internship project, built while studying for the Oracle OCA and OCP exams.
Each part of an interpreter uses a different area of Java, so the lexer covers
strings and loops, the parser covers methods and recursion, and the evaluator
covers inheritance and casting. Building one was a way to practise all of it on
something real instead of on exercises.

## How it works

Three stages. Each has one job and passes a simpler form to the next.

| Stage | Input | Output |
|---|---|---|
| `Lexer` | `"5 + 3"` | `[NUMBER(5), PLUS, NUMBER(3)]` |
| `Parser` | tokens | a tree: `(+ 5 3)` |
| `Evaluator` | tree | `8` |

The parser uses recursive descent. One method per precedence level, each
calling the next. This is why `5 + 3 * 2` gives 11 and not 16.

## What works now

- Numbers, strings, booleans
- Arithmetic: `+ - * /`
- Comparison: `> < >= <= == !=`
- String concatenation: `"a" + 1`
- Parentheses
- Variables: `let x = 5` to declare, `x = 6` to change
- `print`
- Blocks with `{ }`, each with its own scope
- `if` / `else` and `while`
- Script files as well as the REPL
- Errors report a line number and do not stop the REPL

Sip has no functions yet. See [ROADMAP.md](ROADMAP.md).

## Running it

Needs JDK 17 or later.

```bash
javac -d out src/main/java/sip/*.java
java -cp out sip.Main                      # REPL
java -cp out sip.Main examples/demo.sip    # run a file
```

In the REPL, type `exit` to quit. A block can span several lines: the prompt
changes to `...` until the closing brace.

Tests:

```bash
mvn test
```

## A longer example

```
let total = 0
let i = 1

while (i <= 5) {
    total = total + i
    i = i + 1
}

print "sum of 1 to 5 is " + total
```

`examples/demo.sip` has more.

## Project layout

```
src/main/java/sip/
  Lexer.java          text to tokens
  Token.java          one token
  TokenType.java      every kind of token
  Parser.java         tokens to statements and expression trees
  Expr.java           base class for expressions
  Literal.java        5, "hi", true
  Binary.java         5 + 3
  Variable.java       x
  Stmt.java           base class for statements
  StmtLet.java        let x = 5
  StmtAssign.java     x = 6
  StmtPrint.java      print x
  StmtExpression.java a bare expression
  StmtBlock.java      { ... }
  StmtIf.java         if / else
  StmtWhile.java      while
  Evaluator.java      runs the statements
  Environment.java    stores variables, one scope per block
  Main.java           REPL and file runner
```

## Author

Vanesa Spada
