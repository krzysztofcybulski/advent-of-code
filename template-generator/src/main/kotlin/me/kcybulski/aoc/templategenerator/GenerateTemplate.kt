package me.kcybulski.aoc.templategenerator

import io.github.furstenheim.CopyDown
import me.kcybulski.aoc.utilities.Day
import mu.KotlinLogging
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.notExists
import kotlin.io.path.writeText

internal class GenerateTemplate(
    private val packageName: String = "me.kcybulski",
    private val copyDown: CopyDown = CopyDown()
) {

    private val logger = KotlinLogging.logger {}

    suspend fun initializeModule(day: Day) {
        if (modulePath(day).notExists()) {
            logger.info { "Module aoc-${day.year} does not exist. Creating new one..." }
            modulePath(day).createDirectory()
            val buildGradle = BuildGradleFile()
            createFile(modulePath(day), buildGradle.fileName, buildGradle.content())

            logger.info { "Registering new module to gradle" }
            settingsGradle(day).appendText("\ninclude(\"aoc-${day.year}\")")
        } else {
            logger.info { "Module aoc-${day.year} already exists" }
        }
    }

    suspend fun generate(data: DayData) {
        val kt = KotlinFile(packageName, data)
        val testKt = KotlinTestFile(packageName, data)
        createFile(kotlinSourcesPath(data), kt.fileName, kt.content())
        createFile(resourcesPath(data), "input.txt", data.input)
        createFile(testSourcesPath(data), testKt.fileName, testKt.content())
        createFile(testResourcesPath(data), "input.txt", data.testInput)
        createFile(resourcesPath(data), "description.html", data.description)
        createFile(resourcesPath(data), "description.md", copyDown.convert(data.description))
    }

    private fun createFile(
        root: Path,
        path: String,
        content: String
    ) {
        try {
            logger.info { "Creating $path file" }
            root.resolve(path).createFile().writeText(content)
        } catch (e: java.nio.file.FileAlreadyExistsException) {
            logger.warn { "File $path already exists, skipping" }
        }
    }

    private fun kotlinSourcesPath(data: DayData): Path =
        modulePath(data.day)
            .resolve("src/main/kotlin/${packageName.replace(".", "/")}/aoc${data.day.year}")
            .createDirectories()

    private fun testSourcesPath(data: DayData): Path =
        modulePath(data.day)
            .resolve("src/test/kotlin/${packageName.replace(".", "/")}/aoc${data.day.year}")
            .createDirectories()

    private fun testResourcesPath(data: DayData): Path =
        modulePath(data.day)
            .resolve("src/test/resources/${data.formattedTitleDayPrefixed}")
            .createDirectories()

    private fun resourcesPath(data: DayData): Path =
        modulePath(data.day)
            .resolve("src/main/resources/${data.formattedTitleDayPrefixed}")
            .createDirectories()

    private fun modulePath(day: Day): Path =
        Paths.get("./aoc-${day.year}")

    private fun settingsGradle(day: Day): Path =
        Paths.get("./settings.gradle.kts")

    class KotlinFile(
        private val packageName: String,
        private val data: DayData
    ) {

        val fileName = "${data.formattedTitleDayPrefixed}.kt"

        fun content() = """
            package $packageName.aoc${data.day.year}
            
            import $packageName.aoc.utilities.Input
            import $packageName.aoc.utilities.Input.Companion.load
            import $packageName.aoc.utilities.copyToClipboard
            import $packageName.aoc.utilities.sendSolution
            
            class ${data.formattedTitle} {
            
                data class RawInput(
                    val line: String
                )
            
                fun solve(input: Input): Long {
                    return 0
                }
            }
            
            fun main() {
                load("${data.formattedTitleDayPrefixed}")
                    .let(${data.formattedTitle}()::solve)
                    .also(::copyToClipboard)
                    .also(::sendSolution)
            }
        """.trimIndent()
    }

    class KotlinTestFile(
        private val packageName: String,
        private val data: DayData
    ) {

        val fileName = "${data.formattedTitleDayPrefixed}Test.kt"

        fun content() = """
            package $packageName.aoc${data.day.year}

            import io.kotest.core.spec.style.FunSpec
            import io.kotest.matchers.shouldBe
            import me.kcybulski.aoc.utilities.Input
            
            class ${data.formattedTitle}Test: FunSpec() {
            
                init {
            
                    val algorithm = ${data.formattedTitle}()
            
                    test("example") {
                        // when
                        val solution = algorithm.solve(Input.load("${data.formattedTitleDayPrefixed}"))

                        // then
                        solution shouldBe ${data.testSolution}
                    }
                }
            }
        """.trimIndent()
    }

    class BuildGradleFile {

        val fileName = "build.gradle.kts"

        fun content() = """
            dependencies {
                implementation(project(":utilities"))
            }
        """.trimIndent()
    }
}