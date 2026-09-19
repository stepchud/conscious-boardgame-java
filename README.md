# The Conscious Boardgame — Java edition

A desktop Swing implementation of the Conscious Boardgame, a board game built around
Fourth Way ideas: essence and personality, the food diagram, levels of being, laws that
bind and are shed, and the possibility of dying as a Hasnamuss.

Written around 2006 against Java 1.4.

## Layout

```
cbg/boardParts/   the board, spaces, cards, dice, law deck
cbg/player/       player state, strategies, essence/personality, food diagram
cbg/ui/           Swing panels, dialogs, the main window
cbg/common/       shared constants and exceptions
docs/             law card text, board spaces, card deck, design notes
img/              board and card art
dist/             prebuilt CBGame.jar, Java Web Start launcher, printable rules
```

`cbg/` sits at the repository root because the game loads its data as **absolute
classpath resources** — `Decks.java` reads `/docs/LawCardText.txt`, `IconFactory.java`
resolves art the same way. So `docs/` and `img/` must be at the classpath root alongside
the compiled packages.

## Run the prebuilt game

`dist/CBGame.jar` is the released build and is self-contained — it bundles its own
classes, art and card text, and declares `Main-Class`, so it runs from any directory
with nothing else on the classpath:

```sh
java -jar dist/CBGame.jar
```

Any Java 8 or newer runtime works; it was originally built for Java 1.4.

### Hasnamuss test modes

Pass `true` to start a game as a Hasnamuss, and a second `true` to exercise the Causal
Hasnamuss death path:

```sh
java -jar dist/CBGame.jar true          # Hasnamuss game
java -jar dist/CBGame.jar true true     # Causal Hasnamuss death
```

The arguments are read positionally and parsed with `Boolean.valueOf`, so anything other
than `true` is treated as false. With no arguments the game prints both usage hints and
starts normally.

## Build from source

```sh
javac -d out $(find cbg -name '*.java')
cp -r docs img out/
java -cp out cbg.ui.ConsciousBoardgameGUI
```

The `cp` step matters: the game reads its card text and art as classpath resources, so
`docs/` and `img/` have to sit beside the compiled packages in `out/`.

This is 2006-era code; modern compilers emit raw-type and deprecation warnings. They are
expected and harmless.

### The jar and the source have diverged

`dist/CBGame.jar` was not built from the source in this repository. They differ in both
directions:

| | `dist/CBGame.jar` | source in `cbg/` |
| --- | --- | --- |
| Law deck | 85 cards, missing 63 | **86, complete** |
| Second `true` argument | supported | not implemented — `main` reads only `args[0]` |

So a source build gets the restored wild card but only the single Hasnamuss flag, while
the jar is the reverse. The jar's bytecode contains an `"Optional second argument for
testing Causal Hasnamuss"` string that appears nowhere in the archived source, so the
released binary came from a later revision that was not kept.

## The law deck

`docs/LawCardText.txt` _is_ the law deck — `Decks.createLawDeck()` parses it at runtime
into 86 `LawCard`s. The format is a repeating block:

```
63              card number
2S              rank + suit, or JOKER / EXTRA JOKER
WILD CARD: ON THIS TURN,      law text, wrapped, terminated by a blank line
THIS CARD HAS ANY VALUE.

```

Cards are bound to fields by suit: the four kings become `LawCard.KingSpades` and
friends, and the joker becomes `LawCard.Joker`, which `HasnamussStrategy` grants to a
player who becomes a Hasnamuss.

### Deck restoration

This repository carries the complete 86-card deck.

| Source                                              | Cards  | Missing |
| --------------------------------------------------- | ------ | ------- |
| `LawCardText.txt.FINAL` → **now `LawCardText.txt`** | **86** | —       |

**`dist/CBGame.jar` predates this restoration** and bundles its own 85-card copy, so the
prebuilt jar still plays without the wild card. Rebuild it from source to pick up the
full deck.

A `sounds/` directory of stock `.wav`/`.aif` files came with the original source and was
removed. Nothing referenced it, no audio code exists, and the released jar contains no
audio; the filenames carried freesound.org IDs, so it was gathered when sound was planned
but never wired up.

`dist/jnlp/cvgame.jnlp` is kept for the record only. Java Web Start was removed in
Java 11, and the launcher hardcodes an `http://` URL.
