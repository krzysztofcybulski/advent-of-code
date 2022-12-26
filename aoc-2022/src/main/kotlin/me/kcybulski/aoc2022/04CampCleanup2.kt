package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class CampCleanup: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "04CampCleanup"
        lineSplitter = ","
    }

    data class RawInput(
        val first: String,
        val second: String
    ) {

        val firstRange = first.toRange()
        val secondRange = second.toRange()

        fun contains(): Boolean {
            return (firstRange.first >= secondRange.first && firstRange.first <= secondRange.last) ||
                    (secondRange.first >= firstRange.first && secondRange.first <= firstRange.last)
        }
    }

    fun solve(input: Input): Long {
        return input.toList<RawInput>()
            .count { it.contains() }
            .toLong()
    }
}

private fun String.toRange(): IntRange {
    val splitted = split("-").map { it.toInt() }
    return (splitted[0]..splitted[1])
}