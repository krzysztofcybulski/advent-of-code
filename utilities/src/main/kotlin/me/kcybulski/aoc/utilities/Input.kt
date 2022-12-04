package me.kcybulski.aoc.utilities

import mu.KotlinLogging

class Input(
    val lines: List<String>,
    private val lineSplitter: String = "\\s+",
    private val hasHeader: Boolean
) {

    inline fun <reified T> header(splitter: String = ",") = lineToType<T>(lines.first(), splitter.toRegex())

    val lineSplitterRegex = lineSplitter.toRegex()

    val dataLines = lines
        .drop(if (hasHeader) 1 else 0)
        .filterNot(String::isBlank)

    inline fun <reified T> toList(): List<T> = dataLines.map { lineToType(it, lineSplitterRegex) }

    inline fun <reified T> groupLists(): List<List<T>> = group().map(Input::toList)

    fun group(withHeaders: Boolean = false, lines: List<String> = this@Input.lines): List<Input> {
        if(lines.isEmpty()) {
            return emptyList()
        }
        val group = lines.takeWhile(String::isNotBlank)
        return listOf(Input(group, lineSplitter, withHeaders)) + group(withHeaders, lines.drop(group.size + 1))
    }

    inline fun <reified T> lineToType(line: String, lineSplitter: Regex): T {
        if (T::class == String::class) {
            return line as T
        }
        if (T::class == Long::class) {
            return line.toLong() as T
        }
        if (T::class == Int::class) {
            return line.toInt() as T
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

        fun load(
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

        fun load(withInput: WithInput): Input {
            val inputConfig = InputConfiguration().also { withInput.input(it) }
            return load(inputConfig.filename!!, inputConfig.lineSplitter, inputConfig.hasHeader)
        }
    }
}