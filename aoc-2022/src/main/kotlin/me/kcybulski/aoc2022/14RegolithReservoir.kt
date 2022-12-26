package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class RegolithReservoir : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "14RegolithReservoir"
    }

    class Cave(
        val rocks: MutableSet<Point> = mutableSetOf()
    ) {

        var sand: Int = 0
        var stopped: Boolean = false
        fun max() = rocks.maxOf { it.y }

        fun dropSand(point: Point = Point(500, 0)) {
            if(point == Point(500, 0) && Point(501, 1) in rocks) {
                stopped = true
                return
            }
            listOf(
                Point(point.x, point.y + 1),
                Point(point.x - 1, point.y + 1),
                Point(point.x + 1, point.y + 1)
            )
                .find { it !in rocks }
                ?.let { dropSand(it) }
                ?: run { sand++; rocks += point }
        }

        fun addRocks(from: Point, to: Point) {
            if (from.x == to.x && from.y < to.y) {
                (from.y..to.y).forEach { rocks += Point(from.x, it) }
            }
            if (from.x == to.x && from.y > to.y) {
                (to.y..from.y).forEach { rocks += Point(from.x, it) }
            }
            if (from.y == to.y && from.x < to.x) {
                (from.x..to.x).forEach { rocks += Point(it, from.y) }
            }
            if (from.y == to.y && from.x > to.x) {
                (to.x..from.x).forEach { rocks += Point(it, from.y) }
            }
        }

    }

    data class Point(val x: Int, val y: Int) {

        companion object {

            fun fromString(string: String): Point {
                val s = string.split(",")
                return Point(s[0].toInt(), s[1].toInt())
            }

        }

    }

    fun solve(input: Input): Long {
        val cave = Cave()
        input.toList<String>()
            .forEach { line ->
                line.split(" -> ")
                    .map { Point.fromString(it) }
                    .windowed(2, 1)
                    .forEach { cave.addRocks(it[0], it[1]) }
            }
        cave.addRocks(Point(0, cave.max() + 2), Point(1000, cave.max() + 2))
        while(!cave.stopped) {
            cave.dropSand()
        }
        return cave.sand.toLong()
    }
}