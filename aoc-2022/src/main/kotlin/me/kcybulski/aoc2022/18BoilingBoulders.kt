package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class BoilingBoulders: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "18BoilingBoulders"
        lineSplitter = ","
    }

    data class Cube(
        val x: Int,
        val y: Int,
        val z: Int
    ) {

        fun getSides() = listOf(
            Cube(x + 1, y, z),
            Cube(x, y + 1, z),
            Cube(x, y, z + 1),
            Cube(x - 1, y, z),
            Cube(x, y - 1, z),
            Cube(x, y, z - 1)
        )

    }

    data class Lava(
        val cubes: Set<Cube>,
        val steam: MutableSet<Cube> = mutableSetOf()
    ) {

        var expanders = cubes

        fun expand() {
            val newSteam = expanders
                .flatMap { it.getSides() }
                .filter { it !in cubes && it !in steam }
                .toSet()
            expanders = newSteam
            steam += expanders
        }

        fun reduceSteam() {
            steam.removeIf { c ->
                c.getSides().any { it !in cubes && it !in steam }
            }
        }

        fun getFreeSides() =
            cubes
                .flatMap { it.getSides() }
                .filter { it !in cubes && it !in steam }

    }
    fun solve(input: Input): Long {
        val lava = Lava(input.toList<Cube>().toSet())
        repeat(25) {
            lava.expand()
        }
        repeat(100) {
            lava.reduceSteam()
        }
        return lava
            .getFreeSides()
            .count()
            .toLong()
    }
}