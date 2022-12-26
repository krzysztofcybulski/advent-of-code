package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.Input.Companion.load
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import me.kcybulski.aoc.utilities.copyToClipboard
import me.kcybulski.aoc.utilities.sendSolution

class RockPaperScissors: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "02RockPaperScissors"
    }

    data class RawInput(
        val opponent: String,
        val supposed: String
    ) {

        // A - rock 1
        // B - paper 2
        // C - scissors 3

        // X - lose
        // Y - draw
        // Z - win

        fun won() = when (opponent to supposed) {
            "A" to "X" -> 3
            "A" to "Y" -> 4
            "A" to "Z" -> 8

            "B" to "X" -> 1
            "B" to "Y" -> 5
            "B" to "Z" -> 9

            "C" to "X" -> 2
            "C" to "Y" -> 6
            "C" to "Z" -> 7
            else -> error("")
        }

        fun pointsForSupposed() = when (supposed) {
            "X" -> 1
            "Y" -> 2
            "Z" -> 3
            else -> error("")
        }
    }

    fun solve(input: Input): Long =
        input
            .toList<RawInput>()
            .sumOf(RawInput::won)
            .toLong()
}