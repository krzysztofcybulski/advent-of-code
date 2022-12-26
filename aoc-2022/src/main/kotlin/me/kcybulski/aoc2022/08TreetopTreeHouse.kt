package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.lang.Integer.max

class TreetopTreeHouse: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "08TreetopTreeHouse"
    }

    data class RawInput(
        val trees: String
    ) {
    }

    fun solve(input: Input): Long {
        val map = input.toList<String>()
            .map { row -> row.map { it.digitToInt() } }
        val r = map.drop(1)
            .dropLast(1)
            .flatMapIndexed { rowIndex, row ->
                row
                    .drop(1)
                    .dropLast(1)
                    .mapIndexed { columnIndex, t ->
                        val leftR = map[rowIndex + 1].take(columnIndex + 1)
                        val leftV = leftR.takeLastWhile { it < t }
                        val left = if(leftR.size != leftV.size) leftV.size + 1 else leftV.size

                        val rightR = map[rowIndex + 1].takeLast(row.size - columnIndex - 2)
                        val rightV = rightR.takeWhile { it < t }
                        val right = if(rightR.size != rightV.size) rightV.size + 1 else rightV.size


                        val topR = map.map { it[columnIndex + 1] }.dropLast(map.size - rowIndex - 1)
                        val topV = topR.takeLastWhile { it < t }
                        val top = if(topR.size != topV.size) topV.size + 1 else topV.size

                        val bottomR = map.map { it[columnIndex + 1] }.drop(rowIndex + 2)
                        val bottomV = bottomR.takeWhile { it < t }
                        val bottom = if(bottomR.size != bottomV.size) bottomV.size + 1 else bottomV.size

                        val result = left * right * top * bottom
                        result
                    }
            }
        return r.max().toLong()
    }
}