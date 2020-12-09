package day8

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals



fun main() {
    val test1 = listOf(
        "nop +0",
        "acc +1",
        "jmp +4",
        "acc +3",
        "jmp -3",
        "acc -99",
        "acc +1",
        "jmp -4",
        "acc +6"
    )
    assertEquals(5, execute(test1))
    assertEquals(8, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day8\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private val pattern = """(\p{Print}{3}) ([+|-]\d+)""".toRegex()

private fun program(input: List<String>): Pair<Boolean, Int> {
    var (accumulator, index) = listOf(0, 0)
    val instructions = HashSet<Int>()
    while (!instructions.contains(index) && index < input.size) {
        instructions.add(index)
        val (command, value) = pattern.matchEntire(input[index])!!.destructured
        when (command) {
            "acc" -> {
                accumulator += value.toInt()
                index++
            }
            "jmp" -> index += value.toInt()
            "nop" -> index++
            else -> { } //Do Nothing
        }
    }

    return Pair(index == input.size, accumulator)
}

private fun execute(input: List<String>): Int {
    return program(input).second
}

private fun execute2(input: List<String>): Int? {
    input.forEachIndexed { index, it ->
        var newCommand = ""
        when (it.substringBefore(" ")) {
            "nop" -> {
                newCommand = it.replace("nop", "jmp")
            } "jmp" -> {
                newCommand = it.replace("jmp", "nop")
            } else -> {}
        }
        if (newCommand.isNotBlank()) {
            val newProgram = input.toMutableList()
            newProgram[index] = newCommand
            val result = program(newProgram)
            if (result.first) return result.second
        }
    }
    return null
}