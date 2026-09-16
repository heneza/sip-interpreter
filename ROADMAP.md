# Roadmap — build Sip while studying for 1Z0-808

Each milestone matches the exam chapter you're studying. Build the milestone **after**
finishing the chapter — it's your applied practice. Commit to Git after every milestone
with a clear message (`git commit -m "Milestone 2: lexer handles identifiers and keywords"`).

## Milestone 1 — Lexer (Ch. 1–3: basics, operators, decisions)

- [x] Comparison operators: `> < >= <= == !=` (peek at the *next* char to tell `>` from `>=`)
- [x] Identifiers: variable names like `x`, `total`, `myVar2`
- [x] Keywords: `let`, `print`, `if`, `else`, `true`, `false`
- [x] String literals: `"hello"`, with an error on an unterminated string
- [x] All 10 tests in `LexerTest` active and passing

**Exam practice:** char comparisons, `switch`, `String` methods, loop control.

## Milestone 2 — AST classes (Ch. 4–5: methods, encapsulation, class design)

Create classes that represent expressions as a tree. `5 + 3 * 2` becomes:

```
      (+)
     /   \
    5    (*)
        /   \
       3     2
```

- [ ] Abstract class `Expr`
- [ ] Subclasses: `Literal` (a number/string/boolean), `Binary` (left, operator, right), `Variable` (a name)
- [ ] All fields `private final`, values passed via constructor — practice immutability

**Exam practice:** inheritance, abstract classes, constructors, access modifiers.

## Milestone 3 — Parser (Ch. 5: methods, recursion)

Turn the token list into an `Expr` tree, respecting precedence (`*` before `+`).
Technique: *recursive descent* — one method per precedence level, each calling the next:

```
expression() → comparison() → addition() → multiplication() → primary()
```

- [ ] Parse numbers and parenthesized expressions (`primary`)
- [ ] Parse `* /`, then `+ -`, then comparisons
- [ ] Wrong input (e.g. `5 + `) must throw a `ParseException` (your own exception class)

**Exam practice:** method design, recursion, StringBuilder for error messages.

## Milestone 4 — Evaluator (Ch. 6–8: inheritance, polymorphism)

Walk the tree and compute results.

- [ ] `evaluate(Expr)` method using `instanceof` + casting (exam loves this)
- [ ] Then refactor to polymorphism: give each `Expr` subclass its own `evaluate()` — compare both approaches in a commit message (great portfolio storytelling)
- [ ] Division by zero → throw your own `SipRuntimeException`

**Exam practice:** polymorphism, virtual method invocation, casting, `instanceof`.

## Milestone 5 — Variables and statements (Ch. 9: collections)

- [ ] `let x = 5` stores into a `HashMap<String, Object>` environment
- [ ] `print expr` statement
- [ ] REPL keeps variables between lines

**Exam practice:** ArrayList/HashMap, wrapper classes, autoboxing.

## Milestone 6 — Control flow (Ch. 10: exceptions + wrap-up)

- [ ] `if (cond) stmt else stmt`
- [ ] `while (cond) stmt`
- [ ] Run script *files*, not just the REPL: `java sip.Main script.sip`
- [ ] Clean error reports with line numbers

**Exam practice:** exception hierarchy, try/catch/finally, file basics.

## Milestone 7 — Web playground (after the exam — the portfolio piece)

A page where anyone can type Sip code, press Run, and see output — no install,
no clone. The link goes on the CV. The interpreter itself doesn't change: the
browser is just a second frontend alongside the REPL.

```
Browser                POST /run              Java server
editor + Run  ───────► { "source": "..." } ──► Lexer → Parser → Evaluator
output panel  ◄─────── { "output": "..." } ◄──  (unchanged)
```

- [ ] **Facade first:** add `Sip.run(String source)` returning output as a String,
      so `Main`, the web handler, and anything later all share one entry point
- [ ] **Capture output:** `print` currently writes to `System.out`, which a web
      request can't return. Collect into a `StringBuilder` the evaluator holds,
      and return it at the end
- [ ] **HTTP endpoint:** `POST /run` takes source text, returns output as JSON.
      Start with the JDK's built-in `com.sun.net.httpserver`, or Spring Boot
- [ ] **Frontend:** one HTML page — CodeMirror editor, Run button, output panel.
      Syntax highlighting comes straight from the `TokenType` keyword list
- [ ] **Example buttons:** arithmetic, variables, conditionals, fizzbuzz —
      so a visitor sees what Sip does without typing anything
- [ ] **Deploy:** free tier on Render or Fly.io; put the live link in the README

**Skills practiced:** HTTP, JSON, request/response design, API boundaries,
deployment — the cloud/backend side of a masters application.

**Safety note:** anything public that runs submitted code needs limits —
cap input length and add a timeout so an infinite `while` loop can't hang the
server. Worth doing before the link is shared.

## Later ideas

- Functions with parameters and return values
- A written design note on interpreter architecture, for masters applications
- VS Code extension: syntax highlighting + live error squiggles via LSP

## Portfolio checklist

- [ ] Git repo with meaningful commit history (init it now: `git init`)
- [ ] Push to GitHub, add topics: `java`, `interpreter`, `compiler-design`
- [ ] Every milestone has passing JUnit tests
- [ ] README explains design decisions in your own words
