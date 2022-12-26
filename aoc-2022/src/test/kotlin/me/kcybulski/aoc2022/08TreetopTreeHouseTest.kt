package me.kcybulski.aoc2022

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.kcybulski.aoc.utilities.Input.Companion.load

class TreetopTreeHouseTest: FunSpec() {

    init {

        val algorithm = TreetopTreeHouse()

        test("part 2") {
            // when
            val solution = algorithm.solve(load(algorithm))

            // then
            solution shouldBe 21
        }
    }
}