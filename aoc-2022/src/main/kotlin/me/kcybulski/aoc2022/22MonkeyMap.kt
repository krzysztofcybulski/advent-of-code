package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import me.kcybulski.aoc2022.MonkeyMap.Tile.Portal
import me.kcybulski.aoc2022.MonkeyMap.Tile.Wall

class MonkeyMap : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "22MonkeyMap"
    }

    sealed interface Tile {

        val x: Int
        val y: Int

        class Portal(
            val name: String,
            override val x: Int,
            override val y: Int,
            val nextX: Int,
            val nextY: Int,
            val dir: Direction
        ) : Tile

        data class Wall(override val x: Int, override val y: Int) : Tile

    }

    enum class Direction(val right: String, val left: String, val value: Int) {

        U("R", "L", 3), R("D", "U", 0), D("L", "R", 1), L("U", "D", 2)
    }

    data class Map(
        val tiles: Set<Tile>,
        var userX: Int,
        var userY: Int,
        var direction: Direction
    ) {

        fun go(steps: Int) {
            if (steps == 0) {
                return
            }
            val nextX = getNextX()
            val nextY = getNextY()
            val foundTiles = tiles.filter { it.x == nextX && it.y == nextY }
            when (foundTiles.firstOrNull()) {
                is Wall -> return
                is Portal -> {
//                    val portal = (if (direction == Direction.U || direction == Direction.D)
//                        foundTiles.find { it is Portal && it.nextX == it.x }
//                    else foundTiles.find { it is Portal && it.nextY == it.y })!! as Portal
                    val portal = foundTiles.first() as Portal
                    if (Wall(portal.nextX, portal.nextY) in tiles) {
                        return
                    }
                    userX = portal.nextX
                    userY = portal.nextY
                    direction = portal.dir
                }
                else -> {
                    userX = nextX
                    userY = nextY
                }
            }
            go(steps - 1)
        }

        fun getNextX() =
            when (direction) {
                Direction.R -> userX + 1
                Direction.L -> userX - 1
                else -> userX
            }

        fun getNextY() =
            when (direction) {
                Direction.U -> userY - 1
                Direction.D -> userY + 1
                else -> userY
            }


        fun turnRight() {
            direction = Direction.valueOf(direction.right)
        }

        fun turnLeft() {
            direction = Direction.valueOf(direction.left)
        }

    }

    val regex = "\\d+[RL]".toRegex()

    fun solve(input: Input): Long {
        val mapRaw = input
            .dataLines
            .dropLast(1)
        val walls = mapRaw
            .flatMapIndexed { y, row -> row.mapIndexedNotNull { x, e -> if (e == '#') Wall(x, y) else null } }

        val size = mapRaw.size / 4

        val aPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("A$x", x, size - 1, size * 3 - x - 1, 0, Direction.D),
                Portal("A$x", size * 3 - x - 1, -1, x, size, Direction.D)
            )
        }

        val bPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("B$x", size + x, size - 1, size * 2, x, Direction.R),
                Portal("B$x", size * 2 - 1, x, size + x, size, Direction.D)
            )
        }

        val cPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("C$x", size * 3, x, size * 4 - 1, size * 3 - x - 1, Direction.L),
                Portal("C$x", size * 4, size * 2 + x, size * 3 - 1, size - x - 1, Direction.L)
            )
        }

        val dPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("D$x", size * 3, size + x, size * 4 - x - 1, size * 2, Direction.D),
                Portal("D$x", size * 3 + x, size * 2 - 1, size * 3 - 1, size * 2 - x - 1, Direction.L)
            )
        }

        val ePortals = (0 until size).flatMap { x ->
            listOf(
                Portal("E$x", size + x, size * 2, size * 2, size * 3 - x - 1, Direction.R),
                Portal("E$x", size * 2 - 1, size * 2 + x, size * 2 - x - 1, size * 2 - 1, Direction.U)
            )
        }

        // 64227 too high
        // 95310
        // 73415

        val fPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("F$x", x, size * 2, size * 3 - x - 1, size * 3 - 1, Direction.U),
                Portal("F$x", size * 2 + x, size * 3, size - x - 1, size * 2 - 1, Direction.U)
            )
        }

        val gPortals = (0 until size).flatMap { x ->
            listOf(
                Portal("G$x", -1, size + x, size * 4 - x - 1, size * 3 - 1, Direction.U),
                Portal("G$x", size * 3 + x, size * 3, 0, size * 2 - x - 1, Direction.R)
            )
        }

        val tiles = walls + aPortals + bPortals + cPortals + dPortals + ePortals + fPortals + gPortals
        val startX = mapRaw.first().takeWhile { it.isWhitespace() }.length
        val map = Map(tiles.toSet(), startX, 0, Direction.R)

        (-1..(size * 4)).forEach { y ->
            (-1..(size * 4)).forEach { x ->
                val tile = tiles.find { it.x == x && it.y == y }
                val goTo = tiles.filterIsInstance<Portal>().find { it.nextX == x && it.nextY == y }
                if (tile is Portal) {
                    print(" ${tile.name} ")
                } else if (tile is Wall) {
                    print(" ## ")
                } else if (goTo != null) {
                    print(" ${goTo.name} ")
                } else {
                    print("    ")
                }
            }
            println()
        }

        val commands = input.dataLines.last()
        regex.findAll(commands)
            .map { it.value }
            .forEach { value ->
                map.go(value.dropLast(1).toInt())
                when (value.last()) {
                    'R' -> map.turnRight()
                    'L' -> map.turnLeft()
                }
            }
        map.go(commands.takeLastWhile { it.isDigit() }.toInt())
        println("1000 x ${map.userY + 1} + 4 * ${map.userX + 1} + ${map.direction.value}")
        return ((1000 * (map.userY + 1)) + (4 * (map.userX + 1)) + map.direction.value).toLong()
    }
}