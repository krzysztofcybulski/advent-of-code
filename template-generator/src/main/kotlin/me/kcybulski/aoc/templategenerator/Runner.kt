package me.kcybulski.aoc.templategenerator

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.kcybulski.aoc.utilities.Day
import java.time.LocalDate
import kotlin.time.Duration.Companion.seconds

private val fetchDayData = FetchDayData(System.getenv("SESSION"))
private val generateTemplate = GenerateTemplate()

fun main() {
    val localDate = LocalDate.now()
    val day = Day(localDate.year, localDate.dayOfMonth)

    runBlocking {
        generateTemplate.initializeModule(day)
        val dayData = waitForDayData(day)
        generateTemplate.generate(dayData)
    }
}

private suspend fun waitForDayData(day: Day): DayData = coroutineScope {
    var dayData: DayData? = fetchDayData.fetch(day)

    while (dayData == null) {
        delay(1.seconds)
        dayData = fetchDayData.fetch(day)
    }

    dayData
}