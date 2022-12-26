package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import java.util.LinkedList
import java.util.Queue
import java.util.Stack

class DistressSignal: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "13DistressSignal"
    }

    sealed interface Line {
        fun inOrder(other: Line): Boolean
        fun same(other: Line): Boolean
    }

    class Value(val i: Int): Line {
        override fun inOrder(other: Line): Boolean {
            return when (other) {
                is Value -> i <= other.i
                is Nested -> Nested(listOf(this)).inOrder(other)
            }
        }

        override fun same(other: Line): Boolean {
            return when (other) {
                is Value -> i == other.i
                is Nested -> Nested(listOf(this)).same(other)
            }
        }

    }

    class Nested(val values: List<Line>): Line {

        override fun inOrder(o: Line): Boolean {
            val other = when(o) {
                is Nested -> o
                is Value -> Nested(listOf(o))
            }
            if(values.size > other.values.size && same(other)) return false
            return values.zip(other.values)
                .all { (a, b) -> a.inOrder(b) }
        }

        override fun same(o: Line): Boolean {
            val other = when(o) {
                is Nested -> o
                is Value -> Nested(listOf(o))
            }
            return values.zip(other.values)
                .all { (a, b) -> a.same(b) }
        }

    }

    fun solve(input: Input): Long {
        val parsed = input
            .groupLists<String>()
            .map { group -> group.map { parseLine(it) } }
        val indexes = parsed
            .mapIndexedNotNull { index, group -> if (group[0].inOrder(group[1])) index + 1 else null }
        return indexes
            .sum()
            .toLong()
    }

    private fun parseLine(s: String): Line {
        if(s.toIntOrNull() != null) {
            return Value(s.toInt())
        }
        val q: Stack<Int> = Stack()
        val lines: MutableList<Line> = mutableListOf()
        val sD = s.drop(1)
            .dropLast(1)
        var current: String = ""
        sD
            .forEachIndexed { i, c ->
                when(c) {
                    '[' -> q.add(i)
                    ']' -> {
                        val from = q.pop()
                        if(q.isEmpty()) {
                            lines += parseLine(sD.substring(from, i + 1))
                        }
                    }
                    ',' -> {
                        if(current.isNotBlank() && q.isEmpty()) {
                            lines += Value(current.toInt())
                            current = ""
                        }
                    }
                    else -> {
                        if(q.isEmpty()) {
                            current += c
                        }
                    }
                }
            }
        if(current.isNotBlank() && q.isEmpty()) {
            lines += Value(current.toInt())
            current = ""
        }
        return Nested(lines.toList())
    }
}

// [3,[2]],[5,6],5