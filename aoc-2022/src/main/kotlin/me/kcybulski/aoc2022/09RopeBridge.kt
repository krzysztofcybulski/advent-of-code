package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.lang.Integer.max
import java.lang.Integer.min

class RopeBridge: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "09RopeBridge"
    }

    data class RawInput(
        val direction: String,
        val steps: Int
    ) {

        val moves = when(direction) {
            "U" -> 0 to -1
            "R" -> 1 to 0
            "L" -> -1 to 0
            "D" -> 0 to 1
            else -> error("Invalid")
        }.let { m ->
            (0 until steps).map { m }
        }

    }
    fun solve(input: Input): Long {
        var head = 0 to 0
        var rope: MutableList<Pair<Int, Int>> = (0 until 10).map { 0 to 0 }.toMutableList()
        val visited = mutableSetOf(0 to 0)

        var lastMove = 0 to 0

        input
            .toList<RawInput>()
            .flatMap { it.moves }
            .forEach { move ->
                lastMove = move
                head = (head.first + move.first) to (head.second + move.second)

                rope[0] = calc(head, rope[0])
                (1 until 10).forEach { i ->
                    rope[i] = calc(rope[i - 1], rope[i])
                }

                if(rope[9] !in visited) {
                    println(rope[9])
                }
                visited += rope[9]
            }
        return visited.size.toLong()
    }

    private fun calc(head: Pair<Int, Int>, current: Pair<Int, Int>): Pair<Int, Int> {
        val diff = between1(head.first - current.first) to between1(head.second - current.second)
        return if ((current.first + diff.first) to (current.second + diff.second) != head) {
            (current.first + diff.first) to (current.second + diff.second)
        } else {
            current
        }
    }

    private fun between1(p: Int): Int {
        return when {
            p > 1 -> 1
            p < -1 -> -1
            else -> p
        }
    }

}

// 1 1
// 0 0