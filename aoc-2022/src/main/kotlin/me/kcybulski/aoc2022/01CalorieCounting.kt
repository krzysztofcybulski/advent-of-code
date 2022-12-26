package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.Input.Companion.load
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import me.kcybulski.aoc.utilities.copyToClipboard
import me.kcybulski.aoc.utilities.sendSolution

class CalorieCounting : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "01CalorieCounting"
    }

    fun solve(input: Input): Long =
        input
            .groupLists<Long>()
            .map(List<Long>::sum)
            .sortedDescending()
            .take(3)
            .sum()

}