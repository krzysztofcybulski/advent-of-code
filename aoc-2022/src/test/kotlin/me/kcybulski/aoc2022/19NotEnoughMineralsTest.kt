package me.kcybulski.aoc2022

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.kcybulski.aoc.utilities.Input.Companion.load

class NotEnoughMineralsTest: FunSpec() {

    init {

        val algorithm = NotEnoughMinerals()

        test("part 2") {
            // when
            val solution = algorithm.solve(load(algorithm))

            // then
            solution shouldBe 33
        }
    }
}