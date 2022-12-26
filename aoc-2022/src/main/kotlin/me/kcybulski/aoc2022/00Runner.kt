package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input.Companion.load
import me.kcybulski.aoc.utilities.copyToClipboard
import me.kcybulski.aoc.utilities.sendSolution

fun main() {
    val algorithm = NotEnoughMinerals()
    load(algorithm)
        .let(algorithm::solve)
        .also(::copyToClipboard)
        .also(::sendSolution)
}