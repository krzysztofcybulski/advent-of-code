package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.math.BigInteger
import kotlin.math.abs

val MAX_SIZE = 4000000L

class BeaconExclusionZone : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "15BeaconExclusionZone"
    }

    data class Point(val x: Long, val y: Long)

    data class Sensor(
        val x: Long, val y: Long,
        val beaconX: Long, val beaconY: Long
    ) {

        val size = abs(x - beaconX) + abs(y - beaconY)

        fun contains(sx: Long, sy: Long) =
            abs(x - sx) + abs(y - sy) <= size

        fun taken(sx: Long, sy: Long) = (beaconX == sx && beaconY == sy) || (x == sx && y == sy)

        fun possible(): Set<Point> =
            ((x - size - 1)..(x + size + 1))
                .flatMap { sx ->
                    val s = abs(abs(sx - x) - size - 1)
                    listOf(
                        Point(sx, y + s),
                        Point(sx, y - s),
                    )
                }
                .filter { it.x >= 0L && it.y >= 0L }
                .filter { it.y <= MAX_SIZE && it.x <= MAX_SIZE }
                .toSet()

    }

    class Zone(
        val sensors: Set<Sensor>
    ) {

        val left = sensors.minOf { it.x - it.size }
        val right = sensors.maxOf { it.x + it.size }

        fun anySensorContains(x: Long, y: Long): Boolean {
            return sensors.any { it.contains(x, y) } || sensors.any { it.taken(x, y) }
        }

        fun allPossible() = sensors
            .flatMap { it.possible() }
            .toSet()

    }

    val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

    fun solve(input: Input): Long {
        val zone = input.toList<String>()
            .map { regex.matchEntire(it)!! }
            .map {
                val x = it.groups[1]!!.value.toLong()
                val y = it.groups[2]!!.value.toLong()
                val beaconX = it.groups[3]!!.value.toLong()
                val beaconY = it.groups[4]!!.value.toLong()
                Sensor(x, y, beaconX, beaconY)
            }
            .let { Zone(it.toSet()) }
        val part1 = (zone.left..zone.right)
            .count { x -> zone.anySensorContains(x, 2000000L) }
            .toLong()
        println(part1)
        val part2 = zone.allPossible()
            .find { !zone.anySensorContains(it.x, it.y) }!!
        println(part2.x.toBigInteger().multiply(BigInteger.valueOf(4000000L)).plus(part2.y.toBigInteger()))
        return 0L
    }
}