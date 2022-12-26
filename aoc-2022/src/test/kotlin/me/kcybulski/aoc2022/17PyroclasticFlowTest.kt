package me.kcybulski.aoc2022

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.kcybulski.aoc.utilities.Input.Companion.load

class PyroclasticFlowTest: FunSpec() {

    init {

        val algorithm = PyroclasticFlow()

        test("part 2") {
            // when
            val solution = algorithm.solve(load(algorithm))

            // then
            solution shouldBe 3068
        }
    }
}