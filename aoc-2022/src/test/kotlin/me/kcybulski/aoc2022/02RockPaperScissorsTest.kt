package me.kcybulski.aoc2022

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.kcybulski.aoc.utilities.Input.Companion.load

class RockPaperScissorsTest : FunSpec() {

    init {

        val algorithm = RockPaperScissors()

        test("part 2") {
            // when
            val solution = algorithm.solve(load(algorithm))

            // then
            solution shouldBe 12L
        }
    }
}