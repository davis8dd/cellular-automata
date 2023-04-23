# Elementary Cellular Automata

Terminal visualization of elementary cellular automata.
An elementary cellular automaton is visualized as filled/empty cells on a two-dimensional grid that evolves from top to bottom.  A elementary cellular automata has a single rule that determines how it evolves.
Each cell in an elementary cellular automata can only have a value of 'on' or 'off'.
The value of a cell in the current generation is determined by the state of the three cells above it in the previous generation.
The top row (or first generation) is the initial state, and subsequent generations are calculated based on the value of cells in the current generation based on a rule.

### Rules
A rule is a number between 0 - 255 that corresponds to an 8-bit value that represents how three adjacent cells determine the value of the middle cell in the next generation.

Rules:
```
_______  _______  _______  _______  _______  _______  _______  _______
| | | |  |x| | |  | |x| |  | | |x|  |x|x| |  |x| |x|  | |x|x|  |x|x|x|
_______  _______  _______  _______  _______  _______  _______  _______
  |1|      |2|      |3|      |4|      |5|      |6|      |7|      |8|  
```

## Installation

It's recommended to use Leiningen to build and execute this Clojure project.

### Prerequisites
Your terminal must support Curses.

## Usage
Start the program by opening a terminal window in this directory and then running

`lein run`

You will be prompted with the following options:
1. The rule to determine the initial state of the automaton.
1. The width of the grid generated.  The initial state will always be at the center.
1. The number of generations to calculate (i.e. the length of the grid generated).

## Options

The integer **rule** to use when initializing the first generation. Valid values are 1-256.

## Examples
sdf

## References

1. https://mathworld.wolfram.com/CellularAutomaton.html
1. https://natureofcode.com/book/chapter-7-cellular-automata/

## License

Distributed under the GNU General Public License version 3.

See https://choosealicense.com/licenses/gpl-3.0/ or the included LICENSE file for full text.
