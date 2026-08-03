# Sip — a tiny language interpreter, written in Java

**Sip** is a small scripting language and its interpreter, built from scratch in plain Java —
no parser generators, no external libraries. One sip of Java at a time.

```
> let x = 5 + 3 * 2
> print x
11
> if (x > 10) print "big"
big
```

## Why this project exists

I built Sip while preparing for the Oracle Certified Associate (1Z0-808) exam.
Every component of an interpreter exercises a core part of the Java language:

| Interpreter component | Java concepts it exercises |
|---|---|
| Lexer (text → tokens) | Strings, chars, loops, `switch`, enums |
| Token & AST classes | Classes, encapsulation, constructors, immutability |
| Parser (tokens → tree) | Methods, recursion, `ArrayList` |
| Evaluator | Inheritance, polymorphism, casting, operators |
| Error handling | Custom exceptions, try/catch, exception hierarchy |
| Variable environment | Collections, scoping |

## How it works

An interpreter runs in three stages:

1. **Lexer** — reads raw text `"5 + 3"` and produces tokens: `NUMBER(5)`, `PLUS`, `NUMBER(3)`
2. **Parser** — arranges tokens into a tree that respects precedence: `(+ 5 3)`
3. **Evaluator** — walks the tree and computes the result: `8`

## Running it

Requires JDK 17+ (any JDK 8+ works — the code uses only Java 8 features).

**Plain javac:**
```bash
cd src/main/java
javac sip/*.java
java sip.Main
```

**With Maven (also runs the tests):**
```bash
mvn test
mvn compile exec:java -Dexec.mainClass=sip.Main
```

## Project status

See [ROADMAP.md](ROADMAP.md) — milestones are mapped to 1Z0-808 exam objectives.

## Author

Vanesa Spada
