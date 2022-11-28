package me.kcybulski.aoc.templategenerator

import me.kcybulski.aoc.utilities.Day
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class FetchDayData(
    private val session: String,
    private val host: String = "https://adventofcode.com"
) {

    private val logger = KotlinLogging.logger {}

    suspend fun fetch(day: Day): DayData? {
        logger.info { "Fetching $day data" }
        val dayDescriptionPage = fetchDayPage(day) ?: return null
        val dayInput = fetchDayInput(day)
        return DayData(
            day = day,
            title = getDayTitle(findTitleElement(dayDescriptionPage)),
            description = description(dayDescriptionPage),
            input = dayInput,
            testInput = findTestInput(dayDescriptionPage),
            testSolution = findTestSolution(dayDescriptionPage)
        )
    }

    private fun fetchDayInput(day: Day) = Jsoup.connect(dayInputUrl(day))
        .cookie("session", session)
        .get()
        .body()
        .wholeText()

    private fun fetchDayPage(day: Day) = Jsoup.connect(dayPageUrl(day))
        .followRedirects(false)
        .execute()
        .takeIf { it.statusCode() == 200 }
        ?.parse()
        ?.body()

    private fun getDayTitle(rawTitle: String) =
        "^.*: (.*) ---$".toRegex()
            .matchEntire(rawTitle)
            ?.groupValues
            ?.get(1)
            ?: error("Invalid title format")

    private fun findTitleElement(document: Element) =
        document
            .getElementsByTag("h2")
            .first()
            ?.text()
            ?: error("No title found")

    private fun description(document: Element): String =
        document
            .getElementsByClass("day-desc")
            .html()

    private fun findTestInput(document: Element): String =
        document
            .getElementsByTag("pre")
            .first()
            ?.text()
            ?: ""

    private fun findTestSolution(document: Element): Long =
        document
            .getElementsByClass("day-desc")
            .first()
            ?.getElementsByTag("code")
            ?.mapNotNull { it.getElementsByTag("em").first() }
            ?.lastOrNull()
            ?.text()
            ?.toLongOrNull()
            ?: 0L

    private fun dayInputUrl(day: Day) = "${dayPageUrl(day)}/input"

    private fun dayPageUrl(day: Day) = "$host/${day.year}/day/${day.day}"

}

internal data class DayData(
    val day: Day,
    val title: String,
    val description: String,
    val input: String,
    val testInput: String,
    val testSolution: Long
) {

    val dayPrefix = day.day.toString().padStart(2, '0')

    val formattedTitle = title.replace("[^a-zA-Z]+".toRegex(), "")

    val formattedTitleDayPrefixed = "$dayPrefix$formattedTitle"

}