package me.kcybulski.aoc.utilities

interface WithInput {

    val input: InputConfiguration.() -> Unit

}

data class InputConfiguration(
    var filename: String? = null,
    var lineSplitter: String = "\\s+",
    var hasHeader: Boolean? = null
)