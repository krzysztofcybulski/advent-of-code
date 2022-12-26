package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class RucksackReorganization : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "03RucksackReorganization"
    }

    data class RawInput(
        val line: String
    ) {

        private val left = line.substring(0, line.length / 2).toCharArray().toSet()
        private val right = line.substring(line.length / 2).toCharArray().toSet()

        fun prioritySum(): Int =
            (left.toList() + right.toList())
                .groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .keys
                .sumOf(::priority)

    }

    fun solve(input: Input): Long = input.toList<RawInput>()
        .chunked(3)
        .sumOf { lines ->
            lines
                .map { line -> line.line.toCharArray().toSet() }
                .fold(emptyList<Char>()) { a, b -> a + b.toList() }
                .groupingBy { it }
                .eachCount()
                .filter { it.value == 3 }
                .keys
                .first()
                .let(::priority)
        }
        .toLong()


    companion object {
        fun priority(c: Char): Int = c.code - if (c.isUpperCase()) 38 else 96
    }
}