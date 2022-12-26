package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput

class NotEnoughMinerals : WithInput {

    override val input: InputConfiguration.() -> Unit = {
        filename = "19NotEnoughMinerals"
    }

    data class Blueprint(
        val id: Int,
        val oreRobotCostsOre: Int,
        val clayRobotCostsOre: Int,
        val obsidianRobotCostsOre: Int,
        val obsidianRobotCostsClay: Int,
        val geodeobotCostsOre: Int,
        val geodeobotCostsObsidian: Int
    ) {

        val maxOreCost = maxOf(oreRobotCostsOre, clayRobotCostsOre, obsidianRobotCostsOre, geodeobotCostsOre)

        val maxClayCost = maxOf(obsidianRobotCostsClay)

    }

    data class Production(
        var ore: Int,
        var clay: Int,
        var obsidian: Int,
        var geode: Int
    ) {

        fun buildOreRobot() = copy(ore = ore + 1)
        fun buildClayRobot() = copy(clay = clay + 1)
        fun buildObsidianRobot() = copy(obsidian = obsidian + 1)
        fun buildGeodeRobot() = copy(geode = geode + 1)

    }

    data class Stock(
        val ore: Int,
        val clay: Int,
        val obsidian: Int,
        val geode: Int
    ) {

        operator fun plus(production: Production) = Stock(
            ore = ore + production.ore,
            clay = clay + production.clay,
            obsidian = obsidian + production.obsidian,
            geode = geode + production.geode
        )

        fun buildOreRobot(blueprint: Blueprint) = copy(ore = ore - blueprint.oreRobotCostsOre)

        fun buildClayRobot(blueprint: Blueprint) = copy(ore = ore - blueprint.clayRobotCostsOre)

        fun buildObsidianRobot(blueprint: Blueprint) = copy(
            ore = ore - blueprint.obsidianRobotCostsOre,
            clay = clay - blueprint.obsidianRobotCostsClay
        )

        fun buildGeodeRobot(blueprint: Blueprint) = copy(
            ore = ore - blueprint.geodeobotCostsOre,
            obsidian = obsidian - blueprint.geodeobotCostsObsidian
        )

        fun canAffordOreRobot(blueprint: Blueprint) = ore >= blueprint.oreRobotCostsOre
        fun canAffordClayRobot(blueprint: Blueprint) = ore >= blueprint.clayRobotCostsOre
        fun canAffordObsidianRobot(blueprint: Blueprint) =
            ore >= blueprint.obsidianRobotCostsOre && clay >= blueprint.obsidianRobotCostsClay

        fun canAffordGeodeRobot(blueprint: Blueprint) =
            ore >= blueprint.geodeobotCostsOre && obsidian >= blueprint.geodeobotCostsObsidian

    }

    class Plan(
        val blueprint: Blueprint,
    ) {

        fun next(
            stock: Stock,
            production: Production,
            minute: Int
        ): Int {
            if (minute == 25) {
                return stock.geode
            }
            val options: MutableList<Int> = mutableListOf()
            if (stock.canAffordGeodeRobot(blueprint)) {
                return next(
                    stock.buildGeodeRobot(blueprint) + production,
                    production.buildGeodeRobot(),
                    minute + 1
                )
            }
            if (stock.canAffordObsidianRobot(blueprint)
                && production.obsidian <= blueprint.geodeobotCostsObsidian
                && (26 - minute) * (production.obsidian + 1) >= blueprint.geodeobotCostsObsidian
            ) {
                options += next(
                    stock.buildObsidianRobot(blueprint) + production,
                    production.buildObsidianRobot(),
                    minute + 1
                )
            }
            if (stock.canAffordClayRobot(blueprint) && production.clay <= blueprint.maxClayCost) {
                options += next(
                    stock.buildClayRobot(blueprint) + production,
                    production.buildClayRobot(),
                    minute + 1
                )
            }
            if (stock.canAffordOreRobot(blueprint) && production.ore <= blueprint.maxOreCost) {
                options += next(
                    stock.buildOreRobot(blueprint) + production,
                    production.buildOreRobot(),
                    minute + 1
                )
            }
            options += next(
                stock + production,
                production,
                minute + 1
            )
            return options.max()
        }

    }

    val pattern = ("Blueprint ([0-9]+): " +
            "Each ore robot costs ([0-9]+) ore. " +
            "Each clay robot costs ([0-9]+) ore. " +
            "Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay. " +
            "Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.")
        .toRegex()

    fun solve(input: Input): Long {
        val startStock = Stock(0, 0, 0, 0)
        val startProduction = Production(1, 0, 0, 0)

        val time = System.currentTimeMillis()
        val result = input.toList<String>()
            .asSequence()
            .mapNotNull { pattern.matchEntire(it) }
            .map { res ->
                Blueprint(
                    res.groupValues[1].toInt(),
                    res.groupValues[2].toInt(),
                    res.groupValues[3].toInt(),
                    res.groupValues[4].toInt(),
                    res.groupValues[5].toInt(),
                    res.groupValues[6].toInt(),
                    res.groupValues[7].toInt()
                )
            }
            .map { Plan(it) }
            .map { plan ->
                plan.blueprint.id * plan.next(startStock, startProduction, 1).also {
                    println("${plan.blueprint.id} -> $it")
                }
            }
            .sum()

        println(System.currentTimeMillis() - time)

        return result.toLong()
    }
}