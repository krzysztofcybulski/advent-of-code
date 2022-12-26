package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.util.Stack

class SupplyStacks : WithInput {

    val stacks: MutableList<Stack<Crate>> = mutableListOf()

    data class Crate(
        val name: String
    )

    fun move(from: Int, to: Int) {
        stacks[to - 1].push(stacks[from - 1].pop())
    }

    fun move(times: Int, from: Int, to: Int) {
        val newStack = stacks[from - 1].takeLast(times)
        repeat(times) {
            stacks[from - 1].pop()
        }
        newStack.forEach {
            stacks[to - 1].push(it)
        }
    }

    override val input: InputConfiguration.() -> Unit = {
        filename = "05SupplyStacks"
    }

    fun solve(input: Input): Long {
        val lines = input.lines
        val header = lines
            .takeWhile { it.isNotBlank() }
            .dropLast(1)
        repeat((header[0].length + 1) / 4) { column ->
            stacks.add(Stack<Crate>())
            header
                .mapNotNull { it.drop(column * 4)[1].toString().takeIf { it.isNotBlank() } }
                .reversed()
                .forEach { stacks[column].push(Crate(it)) }
        }

        lines
            .dropLast(1)
            .asSequence()
            .drop(header.size + 2)
            .map { it.removePrefix("move ") }
            .map { it.replace(" from ", " ") }
            .map { it.replace(" to ", " ") }
            .map { it.split(" ").map { l -> l.toInt() } }
            .toList()
            .forEach { triple ->
                move(triple[0], triple[1], triple[2])
            }

        stacks
            .map { it.pop() }
            .map { it.name }
            .joinToString("")
            .let { println(it) }

        return 0
    }
}