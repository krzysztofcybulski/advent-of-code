package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import kotlin.math.pow

class FullofHotAir : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "25FullofHotAir"
    }

    data class Snafu(
        val snafuFormat: String
    ) {

        val decimal = snafuFormat.reversed()
            .map {
                when (it) {
                    '-' -> -1
                    '=' -> -2
                    else -> it.digitToInt()
                }
            }
            .mapIndexed { index, i -> i * 5.0.pow(index) }
            .sum()
            .toLong()

        operator fun plus(other: Snafu): Snafu = fromDecimal( decimal + other.decimal)

        companion object {

            fun fromDecimal(n: Long): Snafu =
                when {
                    n == 0L -> Snafu("")
                    n % 5L == 0L -> Snafu(fromDecimal(n / 5).snafuFormat + "0")
                    n % 5L == 1L -> Snafu(fromDecimal(n / 5).snafuFormat + "1")
                    n % 5L == 2L -> Snafu(fromDecimal(n / 5).snafuFormat + "2")
                    n % 5L == 3L -> Snafu(fromDecimal((n + 2) / 5).snafuFormat + "=")
                    n % 5L == 4L -> Snafu(fromDecimal((n  + 1) / 5).snafuFormat + "-")
                    else -> error("!")
                }

        }

    }

    fun solve(input: Input): Long {
        input
            .toList<Snafu>()
            .reduce { acc: Snafu, snafu: Snafu -> acc + snafu }
            .snafuFormat
            .let { println(it) }
        return 0L
    }
}