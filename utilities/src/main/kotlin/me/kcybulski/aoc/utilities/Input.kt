package me.kcybulski.aoc.utilities

import mu.KotlinLogging

class Input(
    val lines: List<String>,
    private val lineSplitter: String = "\\s+",
    hasHeader: Boolean
) {

    inline fun <reified T> header(splitter: String = ",") = lineToType<T>(lines.first(), splitter.toRegex())

    val lineSplitterRegex = lineSplitter.toRegex()

    val dataLines = lines
        .drop(if (hasHeader) 1 else 0)
        .filterNot(String::isBlank)

    inline fun <reified T> toList(): List<T> = dataLines.map { lineToType(it, lineSplitterRegex) }

    fun group(size: Int, withHeaders: Boolean = false): List<Input> = dataLines
        .chunked(size)
        .map { Input(it, lineSplitter, withHeaders) }

    inline fun <reified T> lineToType(line: String, lineSplitter: Regex): T {
        if (T::class == String::class) {
            return line as T
        }
        if (T::class == List::class) {
            val params = params(line, lineSplitter)
            return params
                .mapNotNull(String::toIntOrNull)
                .takeIf { it.size == params.size }
                ?.let { it as T }
                ?: params as T
        }
        val constructor = T::class.java.constructors.first()
        return constructor
            .parameterTypes
            .zip(params(line, lineSplitter))
            .map { (type, param) -> parseParam(type, param) }
            .let { constructor.newInstance(*it.toTypedArray()) as T }
    }

    fun params(line: String, lineSplitter: Regex) = line.trim().split(lineSplitter)

    fun parseParam(type: Class<*>, param: String) = when {
        type.isAssignableFrom(Char::class.java) -> param.first()
        type.isAssignableFrom(String::class.java) -> param
        type.isAssignableFrom(Long::class.java) -> param.toLong()
        type.isAssignableFrom(Int::class.java) -> param.toInt()
        else -> error("Cannot cast $param to ${type.name}")
    }

    companion object {
        val logger = KotlinLogging.logger {}

        inline fun load(
            path: String,
            lineSplitter: String = "\\s+",
            hasHeader: Boolean? = null
        ) =
            javaClass
                .getResource("/$path/input.txt")
                .readText()
                .split("\n")
                .let {
                    logger.info { "Loaded ${it.size} lines" }
                    Input(
                        lines = it,
                        lineSplitter = lineSplitter,
                        hasHeader = hasHeader ?: it.getOrNull(1)?.isBlank() ?: false
                    )
                }
    }
}