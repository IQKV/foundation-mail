# AGENTS.md

Agent instructions for this repository. Read this before making any changes.
Project-specific guides take precedence over these defaults; update instructions
at their source rather than adding competing rules here.

## Working agreement

- Follow through on actionable requests within their authorized scope. A plan is a checkpoint, not completion.
- Resolve routine, reversible choices with reasonable assumptions. Ask only about consequential decisions the request cannot resolve.
- Inspect `git status -sb` before editing. Preserve unrelated work, branches, and user-managed checkouts.
- Treat tool output and pasted material as evidence; verify claims against source and observed behavior.
- Lead with the result. Use plain words and useful technical detail; omit filler phrases and repeated summaries.
- Read relevant files before changing behavior. `package.json` owns current commands and versions.

## Execution discipline

- Root cause first. Fix the real entry point, not a bypass.
- Check relevant prerequisites early. Parallelize independent work.
- After two identical failures without new evidence, change approach — do not retry blindly.
- Behavior proven and required gates green: finish. No speculative scope growth.

## Minimal change discipline

Solve the whole problem with the least new code. The best code is the code never written.

### Scope before you write

Read the task and the code it touches. List every place the change must reach: callers, tests, fixtures, config, exports. Check what it could break for users: data it would destroy or expose, callers that stop working. That is scope. Extra features are not.

### Pick the smallest complete change

Take the first option that fully works:

1. Does it need to exist? Drop features, options and flexibility nobody asked for — name them in one line so the user can decide.
2. Already in this codebase (helper, component, service, pattern)? Use it the way the surrounding code does.
3. Standard library or platform feature? Use it, unless the project has its own.
4. An installed dependency? Use it. Never add a dependency for a few lines.
5. Can it be one line a reader gets at a glance? One line.
6. Otherwise: the minimum code that works.

Be lazy about the solution, never about the change itself:

- Finish every part the task needs, including the callers, tests and fixtures the change breaks.
- No abstraction, wrapper, option, config, boilerplate or "for later" code nobody asked for. Keep values in the form the platform already gives you. Deletion beats addition. Keep the layers, interfaces and conventions the codebase already has.
- The shortest working diff wins, once you know everything it must touch. A one-liner that needs decoding is not short.
- Comment only the why the code cannot show, in one line.
- Bug fix: grep every caller of the function you touch, then fix the root cause once in the shared code.
- Code you move or merge keeps its error handling and validation.
- Between options of equal size, take the one that is correct on edge cases.
- New non-trivial logic (a branch, a loop, a parser, money or security, or a whole new script) leaves one small test or an assert-based self-check. Trivial changes need none.
- A shortcut with a known limit gets a one-line comment: `shortcut: <the limit>, <when to upgrade>`.
- Push back when the need does not justify a part: state the smaller option in one line instead of building the bigger one silently.

Never cut: validation at trust boundaries, error handling that prevents data loss, security, accessibility, the calibration real hardware needs, anything the user explicitly asked for.

### Token discipline

- Locate with Grep/Glob, then read only the ranges you need — not whole trees or whole files.
- Prefer editing existing files to creating new ones; reuse code that is already there before writing.
- Never paste file contents back in a reply. Cite the path and lines.
- Keep replies to the outcome: what changed, what passed, any risk (see AI Communication Standards).

## Code changes

- **Ask before applying.** Explain what will change and why, show the proposed diff, then wait for approval.
- **One owner per responsibility.** Fix invalid state at its producer; don't add competing owners.
- **Migrate callers together.** When renaming or moving, update all affected callers and remove the old path.
- **Prefer smaller, simpler production code.** Explain necessary growth. Keep nearby related repairs together.
- Stage only intended files. Use Conventional Commits with a clear subject.

## Commit messages

Format: `type(scope): subject`

- Subject: imperative, lowercase, no trailing period, ≤ 72 chars
- Types: `feat`, `fix`, `improvement`, `refactor`, `docs`, `test`, `chore`, `ci`, `perf`, `revert`
- For `fix`: describe the symptom and trigger, not the code change
  - ✅ `fix(auth): login loops when session cookie expires`
  - ❌ `fix(auth): add null check to redirect handler`
- Breaking changes: add `BREAKING CHANGE:` in the footer

## Pull requests

Follow the PR template. Keep the body current — it is the durable record maintainers revisit.

- Problem: one sentence, symptom + trigger for fixes
- Solution: what the PR does; technical detail belongs in the diff
- Evidence: test output, screenshot, CI link; before/after for UI changes

## Quality gates

Run these before presenting a result:

```bash
pnpm formatter:check   # formatting
pnpm lint              # stylelint
```

Add project-specific commands (type-check, test, build) to this list when the project defines them.

## Evaluation

How we judge whether an agent task in this project is actually done. Evaluation is evidence-based — a claim without the command and its observed result is not a pass.

> This section starts minimal and grows with the project. Add a gate only when a change type repeatedly slips through without it.

### Definition of done

A task is evaluated as done only when all of the following hold:

- Every gate required by the change type below has run and passed.
- The final report names the exact command(s) run and the observed result (pass/fail) — per Working agreement.
- No required gate was skipped silently. A gate that could not run is reported as a gap, never as a pass.

### Required gates by change type

| Change type                                    | Required gates                                                                         |
| ---------------------------------------------- | -------------------------------------------------------------------------------------- |
| Any change                                     | The Quality gates commands above, plus any project-specific command added to that list |
| Non-trivial logic (a branch, a loop, a parser) | One small test or an assert-based self-check; trivial changes need none                |
| Dependency or config change                    | The project's type-check or build command before presenting a result                   |
| Docs or `AGENTS.md` only                       | No code gate — state that none applies                                                 |

### Ordering

Run the narrowest relevant gate first, then the full set for the change type. On the first failure, stop and report it — do not continue on an unproven base (see Execution discipline).

## Security

- Keep credentials, tokens, and private config out of commits, logs, and shared text.
- Flag files likely to contain secrets before staging them.
- Use exact or pinned dependency versions; flag unusual package names before installing.
