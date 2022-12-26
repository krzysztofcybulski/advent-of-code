\--- Day 22: Monkey Map ---
---------------------------

The monkeys take you on a surprisingly easy trail through the jungle. They're even going in roughly the right direction according to your handheld device's Grove Positioning System.

As you walk, the monkeys explain that the grove is protected by a _force field_. To pass through the force field, you have to enter a password; doing so involves tracing a specific _path_ on a strangely-shaped board.

At least, you're pretty sure that's what you have to do; the elephants aren't exactly fluent in monkey.

The monkeys give you notes that they took when they last saw the password entered (your puzzle input).

For example:

            ...#
            .#..
            #...
            ....
    ...#.......#
    ........#...
    ..#....#....
    ..........#.
            ...#....
            .....#..
            .#......
            ......#.
    
    10R5L5R10L4R5L5
    

The first half of the monkeys' notes is a _map of the board_. It is comprised of a set of _open tiles_ (on which you can move, drawn `.`) and _solid walls_ (tiles which you cannot enter, drawn `#`).

The second half is a description of _the path you must follow_. It consists of alternating numbers and letters:

*   A _number_ indicates the _number of tiles to move_ in the direction you are facing. If you run into a wall, you stop moving forward and continue with the next instruction.
*   A _letter_ indicates whether to turn 90 degrees _clockwise_ (`R`) or _counterclockwise_ (`L`). Turning happens in-place; it does not change your current tile.

So, a path like `10R5` means "go forward 10 tiles, then turn clockwise 90 degrees, then go forward 5 tiles".

You begin the path in the leftmost open tile of the top row of tiles. Initially, you are facing _to the right_ (from the perspective of how the map is drawn).

If a movement instruction would take you off of the map, you _wrap around_ to the other side of the board. In other words, if your next tile is off of the board, you should instead look in the direction opposite of your current facing as far as you can until you find the opposite edge of the board, then reappear there.

For example, if you are at `A` and facing to the right, the tile in front of you is marked `B`; if you are at `C` and facing down, the tile in front of you is marked `D`:

            ...#
            .#..
            #...
            ....
    ...#.D.....#
    ........#...
    B.#....#...A
    .....C....#.
            ...#....
            .....#..
            .#......
            ......#.
    

It is possible for the next tile (after wrapping around) to be a _wall_; this still counts as there being a wall in front of you, and so movement stops before you actually wrap to the other side of the board.

By drawing the _last facing you had_ with an arrow on each tile you visit, the full path taken by the above example looks like this:

            >>v#    
            .#v.    
            #.v.    
            ..v.    
    ...#...v..v#    
    >>>v...>#.>>    
    ..#v...#....    
    ...>>>>v..#.    
            ...#....
            .....#..
            .#......
            ......#.
    

To finish providing the password to this strange input device, you need to determine numbers for your final _row_, _column_, and _facing_ as your final position appears from the perspective of the original map. Rows start from `1` at the top and count downward; columns start from `1` at the left and count rightward. (In the above example, row 1, column 1 refers to the empty space with no tile on it in the top-left corner.) Facing is `0` for right (`>`), `1` for down (`v`), `2` for left (`<`), and `3` for up (`^`). The _final password_ is the sum of 1000 times the row, 4 times the column, and the facing.

In the above example, the final row is `6`, the final column is `8`, and the final facing is `0`. So, the final password is 1000 \* 6 + 4 \* 8 + 0: `_6032_`.

Follow the path given in the monkeys' notes. _What is the final password?_