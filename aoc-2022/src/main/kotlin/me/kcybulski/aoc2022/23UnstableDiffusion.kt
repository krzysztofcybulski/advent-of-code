package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import kotlin.math.abs

class UnstableDiffusion : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "23UnstableDiffusion"
    }

    class Elf(
        var x: Long,
        var y: Long,
        var nextX: Long = x,
        var nextY: Long = y,
        var positions: MutableList<Pair<Int, Int>> = mutableListOf(0 to -1, 0 to 1, -1 to 0, 1 to 0)
    ) {

        fun plan(elves: Set<Elf>) {
            val adjacentElves = elves.filter { (abs(it.x - x) <= 1L) && (abs(it.y - y) <= 1L) } - this
            if (adjacentElves.isEmpty()) {
                nextX = x
                nextY = y
            } else {
                positions
                    .firstOrNull { p -> adjacentElves.none { if(p.first == 0) it.y == y + p.second else it.x == x + p.first } }
                    ?.let { p ->
                        nextX = x + p.first
                        nextY = y + p.second
                    }
            }
        }

        fun reset() {
            nextX = x
            nextY = y
        }

        fun go(): Boolean {
            var r = false
            if(x != nextX || y != nextY) {
                r = true
            }
            x = nextX
            y = nextY
            positions.add(positions.first())
            positions.removeAt(0)
            return r
        }
    }

    fun print(elves: Set<Elf>) {
        val startX = elves.minOf { it.x }
        val startY = elves.minOf { it.y }
        (startY..(elves.maxOf { it.y })).forEach { y ->
            (startX..(elves.maxOf { it.x })).forEach { x ->
                if (elves.any { it.x == x && it.y == y }) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun solve(input: Input): Long {
        val elves = input.toList<String>()
            .flatMapIndexed { y: Int, row: String ->
                row.mapIndexedNotNull { x, c -> if (c == '#') Elf(x.toLong(), y.toLong()) else null }
            }
            .toSet()


        repeat(10000) { i ->
//            println("Round $i")
            elves.forEach { it.plan(elves) }
            elves
                .groupBy { it.nextX to it.nextY }
                .filter { it.value.size > 1 }
                .values
                .flatten()
                .forEach { it.reset() }
            val r = elves.map { it.go() }
            if(r.none { it }) {
                return i.toLong() + 1L
            }
        }
        //1019
        println("After round")
        print(elves)
        val rec = (elves.maxOf { it.x } - elves.minOf { it.x } + 1) * (elves.maxOf { it.y } - elves.minOf { it.y } + 1)
        return rec - elves.size
    }
}