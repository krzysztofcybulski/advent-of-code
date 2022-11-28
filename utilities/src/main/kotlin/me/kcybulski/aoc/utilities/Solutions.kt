package me.kcybulski.aoc.utilities

import mu.KotlinLogging
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

private val logger = KotlinLogging.logger {}

fun copyToClipboard(solution: Long) {
    logger.info { "Copied $solution to clipboard" }
    val selection = StringSelection(solution.toString())
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}

fun sendSolution(solution: Long) {
    logger.info { "Sending solution $solution" }
}