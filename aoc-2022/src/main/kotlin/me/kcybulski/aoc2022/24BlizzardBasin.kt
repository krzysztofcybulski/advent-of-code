package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import me.kcybulski.aoc2022.BlizzardBasin.Object.Blizzard
import me.kcybulski.aoc2022.BlizzardBasin.Object.Me
import java.util.LinkedList
import java.util.Queue

class BlizzardBasin : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "24BlizzardBasin"
    }

    sealed interface Object {

        val x: Int
        val y: Int

        data class Blizzard(override val x: Int, override val y: Int, val direction: Pair<Int, Int>) : Object {

            fun move(walls: Set<Wall>): Blizzard {
                val nextPosition = (x + direction.first) to (y + direction.second)
                if (Wall(nextPosition.first, nextPosition.second) in walls) {
                    return when (direction) {
                        1 to 0 -> {
                            val wall = walls.filter { it.y == y }.minBy { it.x }
                            Blizzard(wall.x + 1, y, direction)
                        }
                        -1 to 0 -> {
                            val wall = walls.filter { it.y == y }.maxBy { it.x }
                            Blizzard(wall.x - 1, y, direction)
                        }
                        0 to 1 -> {
                            val wall = walls.filter { it.x == x }.minBy { it.y }
                            Blizzard(x, wall.y + 1, direction)
                        }
                        0 to -1 -> {
                            val wall = walls.filter { it.x == x }.maxBy { it.y }
                            Blizzard(x, wall.y - 1, direction)
                        }
                        else -> error("U")
                    }
                } else {
                    return Blizzard(nextPosition.first, nextPosition.second, direction)
                }
            }

        }

        data class Wall(override val x: Int, override val y: Int) : Object

        data class Me(
            override val x: Int,
            override val y: Int
        ) : Object {

            fun allMoves(objects: Collection<Object>): Set<Me> =
                setOf(
                    Me(x, y),
                    Me(x - 1, y),
                    Me(x + 1, y),
                    Me(x, y - 1),
                    Me(x, y + 1)
                )
                    .filter { me -> me.y >= 0 && y <= objects.maxOf { it.y } && objects.none { it.x == me.x && it.y == me.y } }
                    .toSet()

        }
    }

    class Place(
        val time: Int,
        private val blizzards: List<Blizzard>,
        private val walls: Set<Object.Wall>
    ) {

        val objects: List<Object> = walls.toList() + blizzards

        fun placeAtTime(i: Int): List<Place> {
            val allPlaces = mutableListOf(this)
            while (allPlaces.last().time != i) {
                allPlaces += Place(allPlaces.last().time + 1, allPlaces.last().blizzards.map { it.move(walls) }, walls)
            }
            return allPlaces
        }

    }

    fun solve(input: Input): Long {
        val blizzards = input.toList<String>()
            .flatMapIndexed { y: Int, row: String ->
                row.mapIndexedNotNull { x, c ->
                    when (c) {
                        '>' -> Blizzard(x, y, 1 to 0)
                        '<' -> Blizzard(x, y, -1 to 0)
                        '^' -> Blizzard(x, y, 0 to -1)
                        'v' -> Blizzard(x, y, 0 to 1)
                        else -> null
                    }
                }
            }
        val walls = input.toList<String>()
            .flatMapIndexed { y: Int, row: String ->
                row.mapIndexedNotNull { x, c ->
                    if (c == '#') Object.Wall(x, y) else null
                }
            }
            .toSet()
        val cycle = (walls.maxOf { it.x } - 1) * (walls.maxOf { it.y } - 1)
        println("Cycles $cycle")
        val place = Place(0, blizzards, walls)
        val allPlaces = place.placeAtTime(cycle - 1)


        val a = findPath(1, 0, input.dataLines.size - 1, allPlaces)
        val b = findPath(input.dataLines.first().length - 2, input.dataLines.size - 1, 0, allPlaces.drop(a))
        val c = findPath(1, 0, input.dataLines.size - 1, allPlaces.drop(a + b))
        return a + b + c.toLong()
    }

    private fun findPath(
        startX: Int,
        startY: Int,
        goalY: Int,
        allPlaces: List<Place>
    ): Int {
        val visited: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
        val q: Queue<Triple<Int, Int, Int>> = LinkedList()
        visited += Triple(0, startX, startY)
        q.add(Triple(0, startX, startY))
        while (q.isNotEmpty()) {
            val (time, x, y) = q.poll()
            if (y == goalY) {
                return time
            }
            Me(x, y)
                .allMoves(allPlaces[time + 1].objects)
                .filter { w -> Triple(time + 1, w.x, w.y) !in visited }
                .forEach { w ->
                    visited += Triple(time + 1, w.x, w.y)
                    q.add(Triple(time + 1, w.x, w.y))
                }
        }
        return 0
    }


    fun path(point: Pair<Int, Int>, parents: MutableMap<Pair<Int, Int>, Pair<Int, Int>>): Int {
        return parents[point]?.let { path(it, parents) + 1 } ?: 0
    }
}