# Agent notes

## Copying or moving platform directories

When copying or moving a platform/version module directory, move **the whole directory**,
not just the files git knows about.

Do not use `git archive`, `git ls-files`, or `rsync` with `--exclude` filters to reproduce a
module. Use a plain recursive copy (`cp -a`) or `git mv` for the whole directory, then fix up
only what genuinely differs (version properties, symlink targets).

**Why:** a module directory contains more than tracked sources — untracked local
configuration, generated sources, `runs/` state, and empty directories that exist because a
source root or symlink target is expected to be there. Filtering by git status or by an
exclude list silently drops them, and the module then fails in ways that look unrelated to
the copy.

The same applies in reverse: do not delete "empty" directories as cleanup. An empty
directory under a platform module is usually a source root the build plugin registers
unconditionally, or the parent of a symlink.

## Code style

### Control flow

Prefer nested `if`s over early-return guard clauses. Do not restructure a method into a
sequence of `if (!condition) return;` statements to keep the body flat — nest the positive
conditions instead, so the code reads as the conditions under which the work happens.

Complex boolean expressions are allowed and preferred over splitting one condition across
several guards. Combine with `&&` / `||` and parentheses rather than adding early exits.

```java
// yes
if (delay > 0 && cheatsEnabled) {
    if (removeCooldown || (player != null && player.getAbilities().instabuild && instantMining)) {
        delay = 0;
    }
}

// no
if (delay <= 0) return;
if (!cheatsEnabled) return;
if (removeCooldown) {
    delay = 0;
    return;
}
```

### Line length

Maximum line length is **220** columns. Don't wrap a condition or a call chain that fits
within it — a long single line is preferred over an artificial break.

Both rules apply to Kotlin and Java alike.
