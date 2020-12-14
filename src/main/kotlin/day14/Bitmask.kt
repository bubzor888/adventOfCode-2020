package day14

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 =
    assertEquals(165, execute(listOf("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X","mem[8] = 11","mem[7] = 101","mem[8] = 0")))
    assertEquals(208, execute2(listOf("mask = 000000000000000000000000000000X1001X","mem[42] = 100","mask = 00000000000000000000000000000000X0XX","mem[26] = 1")))
    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day14\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}



private fun applyMask(mask:String, number: Long): Long {
    return number.toString(2)
        .padStart(36, '0')
        .mapIndexed { index, ch ->
            when (mask[index] == 'X') {
                true -> ch
                false -> mask[index]
            }
        }.joinToString("").toLong(2)
}

private fun execute(input: List<String>): Long {
    val memory = mutableMapOf<Int, Long>()
    var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    input.forEach {
        val (command, value) = it.split(" = ")
        when (command) {
            "mask" -> mask = value
            else -> memory[command.substring(4, command.length-1).toInt()] = applyMask(mask, value.toLong())
        }
    }

    return memory.values.reduce { acc, num -> acc + num }
}

private fun copyAndAdd(list: List<Char>, element: Char): MutableList<Char> {
    val result = list.toMutableList()
    result.add(element)
    return result
}

private fun applyDecoder(mask:String, number: Long): List<Long> {
    var result = mutableListOf(mutableListOf('0'))
    number.toString(2)
        .padStart(36, '0')
        .forEachIndexed { index, ch ->
            when (mask[index]) {
                '0' -> result.map { it.add(ch) }
                '1' -> result.map { it.add('1')}
                'X' -> {
                    val newResult = mutableListOf<MutableList<Char>>()
                    result.forEach{
                        newResult.add(copyAndAdd(it, '0'))
                        newResult.add(copyAndAdd(it, '1'))
                    }
                    result = newResult
                }
            }
        }

    return result.map { it.joinToString("").toLong(2) }
}

private fun execute2(input: List<String>): Long {
    val memory = mutableMapOf<Long, Long>()
    var mask = "000000000000000000000000000000000000"
    input.forEach {
        val (command, value) = it.split(" = ")
        when (command) {
            "mask" -> mask = value
            else -> {
                applyDecoder(mask, command.substring(4, command.length-1).toLong()).forEach { i -> memory[i] = value.toLong() }
            }
        }
    }

    return memory.values.reduce { acc, num -> acc + num }
}