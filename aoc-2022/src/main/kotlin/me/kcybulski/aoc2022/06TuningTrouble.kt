package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class TuningTrouble: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "06TuningTrouble"
    }

    data class RawInput(
        val line: String
    ) {

        fun startOfPacket(markerSize: Int = 14) =
            line
                .windowed(markerSize)
                .indexOfFirst { it.toSet().size == markerSize } + markerSize

    }

    fun solve(input: Input): Long {
        return input.toList<RawInput>()
            .first()
            .startOfPacket()
            .toLong()
    }
}