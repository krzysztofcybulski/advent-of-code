package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class MonkeyintheMiddle : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "11MonkeyintheMiddle"
    }

    class Monkey(
        val index: Long,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val chooseMonkey: (Long) -> Int,
        val div: Long,
        var business: Long = 0
    ) {

        fun addItem(item: Long) {
            items += item
        }

        fun inspect(monkeys: List<Monkey>) {
            while(items.isNotEmpty()) {
                val item = items.first()
                business++
                val result = operation(item)
                val newMonkey = chooseMonkey(result)
//                println(
//                    """
//                Monkey $index:
//                    Monkey inspects an item with a worry level of $item.
//                    Worry level is calculated to ${operation(item)}.
//                    Monkey gets bored with item. Worry level is divided by 3 to $result.
//                    Item with worry level $result is thrown to monkey $newMonkey.
//            """.trimIndent()
//                )
                items.removeFirst()
                monkeys[newMonkey].addItem(result)
            }
        }

        fun optimize(divAll: Long) {
            val newItems = items.map { it % divAll}
            items.clear()
            items.addAll(newItems)
        }

    }

    fun solve(input: Input): Long {
        val monkeys = input.groupLists<String>().map { stringToMonkey(it) }
        val divAll = monkeys.map { it.div }.reduce { acc, i -> acc * i }
        repeat(10000) { i ->
            println("Round $i")
            println("________")
            if(i % 1 == 0) {
                monkeys.forEach { it.optimize(divAll) }
            }
            monkeys.forEach { monkey ->
                monkey.inspect(monkeys)
            }
        }
        return monkeys.map { it.business }.sortedByDescending { it }.take(2).let { it[0] * it[1] }.toLong()
    }

    private fun stringToMonkey(rawLines: List<String>): Monkey {
        val lines = rawLines.map { it.trimIndent() }
        val index = lines[0].removePrefix("Monkey ").dropLast(1).toLong()
        val items = lines[1].removePrefix("Starting items: ").split(", ").map { it.toLong() }
        val operationRaw = lines[2].removePrefix("Operation: new = old ").take(1)
        val operationNumber = if(lines[2].endsWith("old")) null else lines[2].lastNumber()
        val operation = when(operationRaw) {
            "+" -> { x: Long -> x + (operationNumber ?: x) }
            "*" -> { x: Long -> x * (operationNumber ?: x) }
            else -> error("")
        }
        val div = lines[3].lastNumber()
        val tM = lines[4].lastNumber().toInt()
        val fM = lines[5].lastNumber().toInt()
        val chooseMonkey = { x: Long -> if(x % div == 0L) tM else fM}
        return Monkey(index, items.toMutableList(), operation, chooseMonkey, div)
    }
}

private fun String.lastNumber() = takeLastWhile { !it.isWhitespace() }.toLong()