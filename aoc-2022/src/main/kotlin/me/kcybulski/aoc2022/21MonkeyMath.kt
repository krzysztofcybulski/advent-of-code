package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class MonkeyMath: WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "21MonkeyMath"
    }

    data class Monkey(
        val name: String,
        val canYell: (Jungle) -> Boolean,
        val yell: (Jungle) -> Long,
        val required: List<String>
    )

    data class Jungle(
        val monkeys: MutableMap<String, Monkey> = mutableMapOf(),
        val yells: MutableMap<String, Long> = mutableMapOf()
    ) {

        fun addSimpleMonkey(name: String, number: Long) {
            monkeys[name] = Monkey(name, { true }, { number }, listOf())
        }

        fun addPlusMonkey(name: String, aName: String, bName: String) {
            monkeys[name] = Monkey(name, { aName in yells && bName in yells }, { yells[aName]!! + yells[bName]!! }, listOf(aName, bName))
        }

        fun addMinusMonkey(name: String, aName: String, bName: String) {
            monkeys[name] = Monkey(name, { aName in yells && bName in yells }, { yells[aName]!! - yells[bName]!! }, listOf(aName, bName))
        }

        fun addMultiplyMonkey(name: String, aName: String, bName: String) {
            monkeys[name] = Monkey(name, { aName in yells && bName in yells }, { yells[aName]!! * yells[bName]!! }, listOf(aName, bName))
        }

        fun addDivideMonkey(name: String, aName: String, bName: String) {
            monkeys[name] = Monkey(name, { aName in yells && bName in yells }, { if(yells[aName]!! % yells[bName]!! == 0L) yells[aName]!! / yells[bName]!! else 0 }, listOf(aName, bName))
        }

        fun addRootMonkey(name: String, aName: String, bName: String) {
            monkeys[name] = Monkey(name, { aName in yells && bName in yells }, { if(yells[aName]!! == yells[bName]!!) 1 else 0 }, listOf(aName, bName))
        }

        fun yell() {
            val monkeyToYell = monkeys.values.find { it.name !in yells && it.canYell(this) }
            if(monkeyToYell != null) {
                yells[monkeyToYell.name] = monkeyToYell.yell(this)
            }
        }

        fun reset() {
            yells.clear()
        }
    }

    fun solve(input: Input): Long {
        val jungle = Jungle()
        input.toList<String>()
            .map { line ->
                val name = line.takeWhile { it != ':' }
                val yell = line.drop(name.length + 2)
                when {
                    name == "root" -> jungle.addRootMonkey(name, yell.takeWhile { !it.isWhitespace() }, yell.takeLastWhile { !it.isWhitespace() })
                    '+' in yell -> jungle.addPlusMonkey(name, yell.takeWhile { !it.isWhitespace() }, yell.takeLastWhile { !it.isWhitespace() })
                    '-' in yell -> jungle.addMinusMonkey(name, yell.takeWhile { !it.isWhitespace() }, yell.takeLastWhile { !it.isWhitespace() })
                    '*' in yell -> jungle.addMultiplyMonkey(name, yell.takeWhile { !it.isWhitespace() }, yell.takeLastWhile { !it.isWhitespace() })
                    '/' in yell -> jungle.addDivideMonkey(name, yell.takeWhile { !it.isWhitespace() }, yell.takeLastWhile { !it.isWhitespace() })
                    else -> jungle.addSimpleMonkey(name, yell.toLong())
                }
            }

        var humn = 3555057453220L // 3555057453229
        while(humn < 3555057453240L) {
            solve(jungle, humn)
            println("$humn -> ${jungle.yells["qntq"]!! - 8226036122233L}")
            humn++
            jungle.reset()
        }
        return humn
    }

    fun solve(jungle: Jungle, humn: Long = 1): Boolean {
        jungle.addSimpleMonkey("humn", humn)
        while("root" !in jungle.yells) {
            jungle.yell()
        }
        return jungle.yells["root"]!! == 1L
    }
}