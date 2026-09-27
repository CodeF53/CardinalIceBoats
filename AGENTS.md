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
