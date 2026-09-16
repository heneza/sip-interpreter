# Roadmap

Milestones 1 to 6 are built alongside the 1Z0-808 chapters. Commit after each one.

## Done

**1. Lexer**

- Numbers, arithmetic, parentheses
- Comparison operators (`>` `<` `>=` `<=` `==` `!=`)
- Identifiers and keywords
- String literals, with an error on an unterminated string
- 10 passing tests

**2. AST classes**

- `Expr` (abstract), `Literal`, `Binary`, `Variable`
- All fields private final, set through constructors
- `Binary.toString()` prints the tree as `(+ 5 (* 3 2))`

**3. Parser**

- Recursive descent, one method per precedence level
- Parentheses handled by recursing back to the top
- `ParseException` on bad input

**4. Evaluator**

- Walks the tree with `instanceof` and casting
- Arithmetic, comparison, string concatenation
- `SipRuntimeException` for division by zero and type errors
- Prints `11` rather than `11.0`

**5. Variables and statements**

- `Environment` wrapping a `HashMap<String, Object>`
- `Stmt` hierarchy: `StmtLet`, `StmtPrint`, `StmtExpression`
- `Parser.statement()` decides which one to build
- `Evaluator.execute(Stmt)`
- Variables persist across REPL lines

**6. Assignment and control flow**

- `x = 6` changes a variable, `let` declares it and rejects a name already taken
- `Environment` scopes chain outwards, so a block can see the scope around it
- `StmtAssign`, `StmtBlock`, `StmtIf`, `StmtWhile`
- `{` and `}` tokens, `while` keyword
- Conditions must be true or false, so `if (5)` is an error
- Run a file: `java -cp out sip.Main examples/demo.sip`
- The REPL buffers lines while a block is open

## 7. Web playground

After the exam. A page where anyone can type Sip code, press Run and see
output. No install, no clone. The interpreter does not change. The browser
is a second frontend next to the REPL.

```
Browser              POST /run              Java server
editor + Run  ─────► { "source": "..." } ──► Lexer → Parser → Evaluator
output panel  ◄───── { "output": "..." } ◄──
```

- Add `Sip.run(String source)` returning output as a String, so the REPL and
  the web handler share one entry point
- Collect `print` output into a `StringBuilder` instead of `System.out`,
  since a web request cannot return console output
- `POST /run` endpoint. `com.sun.net.httpserver` is built into the JDK, or
  use Spring Boot
- One HTML page: CodeMirror editor, Run button, output panel. Syntax
  highlighting comes from the keyword list in `TokenType`
- Example buttons: arithmetic, variables, conditionals, fizzbuzz
- Deploy on Render or Fly.io, link it in the README

Before sharing the link: cap input length and add a timeout, so an infinite
`while` loop cannot hang the server.

## Later

- Functions with parameters and return values
- A written note on the design decisions, for masters applications
- VS Code extension: syntax highlighting and live error underlining
