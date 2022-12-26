package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import kotlin.math.abs

class GrovePositioningSystem: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "20GrovePositioningSystem"
    }

    data class NumberToMove(
        val value: Long,
        val originalIndex: Int
    ) {

        lateinit var next: NumberToMove
        lateinit var previous: NumberToMove
        var size: Int? = null

        fun move(indexToMove: Int) {
            if(originalIndex != indexToMove) {
                return next.move(indexToMove)
            }
            val toMove = ((abs(value)) % (size!! - 1)).toInt()
            if(value > 0) {
                repeat(toMove) {
                    val oldP = previous
                    val oldN = next
                    next = next.next
                    previous = oldN
                    oldP.next = oldN
                    oldN.previous = oldP
                    oldN.next = this
                    next.previous = this
                }
            }
            if(value < 0) {
                repeat(toMove) {
                    val oldP = previous
                    val oldN = next
                    oldP.previous.next = this
                    previous = oldP.previous
                    oldP.previous = this
                    oldP.next = oldN
                    oldN.previous = oldP
                    next = oldP
                }
            }
        }

        fun print(size: Int) {
            var c = this
            repeat(size) {
                print("${c.value}, ")
                c = c.next
            }
            println()
        }

        fun find(i: Int): Long {
            var c = this
            while(c.value != 0L) {
                c = c.next
            }
            repeat(i) {
                c = c.next
            }
            return c.value
        }

    }

    fun solve(input: Input): Long {
        val numbers = input.toList<String>()
            .mapIndexed { i, n -> NumberToMove(n.toLong() * 811589153L, i) }
        numbers.forEachIndexed { index, numberToMove ->
            numberToMove.next = numbers[(index + 1) % numbers.size]
            numberToMove.previous = numbers[if(index > 0) index - 1 else numbers.size - 1]
            numberToMove.size = numbers.size
        }
        val first = numbers.first()
        first.print(numbers.size)
        repeat(10) {
            repeat(numbers.size) {
                first.move(it)
            }
            first.print(numbers.size)
        }
        val result = first.find(1000) + first.find(2000) + first.find(3000)
        return result.toLong()
    }
}