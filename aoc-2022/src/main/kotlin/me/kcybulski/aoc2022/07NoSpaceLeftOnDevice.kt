package me.kcybulski.aoc2022

import me.kcybulski.aoc.utilities.Input
import me.kcybulski.aoc.utilities.InputConfiguration
import me.kcybulski.aoc.utilities.WithInput
import mu.KotlinLogging

class NoSpaceLeftOnDevice : WithInput {

    private val logger = KotlinLogging.logger {}

    override val input: InputConfiguration.() -> Unit = {
        filename = "07NoSpaceLeftOnDevice"
    }

    sealed interface Node {

        val name: String
        val size: Long
        val parent: Directory?

        class File(
            override val name: String,
            override val size: Long,
            override val parent: Directory?
        ) : Node

        class Directory(
            override val name: String,
            val children: MutableSet<Node>,
            override val parent: Directory?
        ) : Node {

            override val size: Long
                get() = children.sumOf { it.size }

            fun sizeOfDirectoriesWithSizeAtMost(maxSize: Long): Long {
                return children
                    .filterIsInstance<Directory>()
                    .sumOf { it.sizeOfDirectoriesWithSizeAtMost(maxSize) } +
                        (if(size <= maxSize) size else 0)
            }

            fun allDirectories(): Set<Directory> {
                return (children.filterIsInstance<Directory>().flatMap { it.allDirectories() } + this).toSet()
            }

            fun cd(name: String): Directory {
                if(name == "/") {
                    var c: Node = this
                    while(c.name != "/" && c.parent != null) {
                        c = c.parent!!
                    }
                    return c as Directory
                }
                if(name == "..") {
                    return parent ?: this
                }
                if(children.find { it.name == name } == null) {
                    children.add(Directory(name, mutableSetOf(), this))
                }
                return children.find { it.name == name }!! as Directory
            }

            fun ls(): Set<Node> = children.toSet()
        }

    }

    data class RawInput(
        val line: String
    ) {

        fun isCommand() = line.startsWith("/")


    }

    fun solve(input: Input): Long {
        var currentNode = Node.Directory("/", mutableSetOf(), null)

        input.toList<String>()
            .forEach { line ->
                if(line.startsWith("$ cd")) {
                    currentNode = currentNode.cd(line.removePrefix("$ cd "))
                }
                if(!line.startsWith("$")) {
                    val s = line.split(" ")
                    if(s[0].toLongOrNull() != null) {
                        currentNode.children.add(Node.File(s[1], s[0].toLong(), currentNode))
                    } else {
                        currentNode.children.add(Node.Directory(s[1], mutableSetOf(), currentNode))
                    }
                }
            }

        val root = currentNode.cd("/")
        val spaceNeeded = 30000000 - (70000000 - root.size)
        val smallest = root.allDirectories()
            .map { it.size }
            .sorted()
            .find { it >= spaceNeeded  }

        return smallest ?: 0
    }
}