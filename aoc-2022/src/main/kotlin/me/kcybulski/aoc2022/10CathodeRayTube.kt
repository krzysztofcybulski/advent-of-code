package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import kotlin.math.abs

class CathodeRayTube : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "10CathodeRayTube"
    }

    val register = mutableMapOf<String, Int>("x" to 1)
    var cycle = 1
    var xSum = 0

    val result: StringBuilder = StringBuilder()

    fun noop() {
        afterCycle()
    }

    fun add(variable: String, value: Int) {
        val current = register[variable] ?: 1
        afterCycle()
        afterCycle()
        register[variable] = current + value
    }

    fun afterCycle() {
        if ((cycle - 20) % 40 == 0) {
            xSum += cycle * register["x"]!!
        }
        if(cycle % 40 == 0) {
            result.append("\n")
        }
        if (abs(register["x"]!! - ((cycle - 1) % 40)) <= 1) {
            result.append("#")
        } else {
            result.append(".")
        }
        cycle++
    }

    fun solve(input: Input): Long {
        input.toList<String>()
            .forEach { line ->
                when {
                    line.startsWith("noop") -> noop()
                    line.startsWith("addx") -> add("x", line.takeLastWhile { !it.isWhitespace() }.toInt())
                }
            }
        println(result)
        return xSum.toLong()
    }
}