package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

class HillClimbingAlgorithm : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "12HillClimbingAlgorithm"
    }

    data class Point(val x: Int, val y: Int) {

//        fun length(): Int {
//            if(parent == null) {
//                return 0
//            }
//            return parent!!.length() + 1
//        }

        fun neighbors() = listOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x, y - 1)
        )

    }

    class HeightMap(
        val map: List<List<Int>>,
        var S: Point = Point(0, 0),
        var E: Point = Point(0, 0),
        val visited: MutableList<Point> = mutableListOf(),
        val parents: MutableMap<Point, Point> = mutableMapOf()
    ) {

        fun go(): Int {

            val q: Queue<Point> = LinkedList()
            visited += E
            q.add(E)

            while (q.isNotEmpty()) {
                val v = q.poll()
                if (map[v.y][v.x] == 1) {
                    println("Done")
                    return path(v)
                }
                v.neighbors()
                    .filter { canGo(v, it) }
                    .filter {  it !in visited }
                    .forEach { w ->
                        visited += w
                        parents[w] = v
                        q.add(w)
                    }
            }

            return -1
        }

        fun path(point: Point = E): Int {
            return parents[point]?.let { path(it) + 1 } ?: 0
        }

        private fun canGo(currentPosition: Point, point: Point): Boolean {
            if(point.x < 0 || point.y < 0 || point.x >= map[0].size || point.y >= map.size) return false
            return map[currentPosition.y][currentPosition.x] - map[point.y][point.x] <= 1
        }

    }

    fun solve(input: Input): Long {
        var S: Point? = null
        var E: Point? = null
        val heightMap = input
            .toList<String>()
            .mapIndexed { rowI, row ->
                row.mapIndexed { columnI, c ->
                    when (c) {
                        'S' -> {
                            S = Point(columnI, rowI)
                            'a'.code - 96
                        }
                        'E' -> {
                            E = Point(columnI, rowI)
                            'z'.code - 96
                        }
                        else -> c.code - 96
                    }
                }
            }
            .let { HeightMap(it, S = S!!, E = E!!) }
        return heightMap.go().toLong()
    }
}