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
sounds/           effects
dist/             prebuilt CBGame.jar, Java Web Start launcher, printable rules
```

`cbg/` sits at the repository root because the game loads its data as **absolute
classpath resources** — `Decks.java` reads `/docs/LawCardText.txt`, `IconFactory.java`
resolves art the same way. So `docs/`, `img/`, and `sounds/` must be at the classpath
root alongside the compiled packages.

## Build and run

```sh
javac -d out $(find cbg -name '*.java')
cp -r docs img sounds out/
java -cp out cbg.ui.ConsciousBoardgameGUI
```

Pass `true` as the single argument to start a Hasnamuss game:

```sh
java -cp out cbg.ui.ConsciousBoardgameGUI true
```

This is 2006-era code; modern compilers emit raw-type and deprecation warnings. They are
expected and harmless.

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

`dist/jnlp/cvgame.jnlp` is kept for the record only. Java Web Start was removed in
Java 11, and the launcher hardcodes an `http://` URL.
