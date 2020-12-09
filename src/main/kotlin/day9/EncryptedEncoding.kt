package day9

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf(20,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,23,24,25,45,65).map { it.toLong() }
    assertEquals(65L, execute(test1, 25))
    val test2 = listOf(35,20,15,25,47,40,62,55,65,95,102,117,150,182,127,219,299,277,309,576).map { it.toLong() }
    assertEquals(127L, execute(test2, 5))

    assertEquals(62L, execute2(test2, 5))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day9\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines().map { it.toLong() }, 25)}")
    println("Final Result 2: ${execute2(File(fileName).readLines().map { it.toLong() }, 25)}")
}

private fun isValid(input: List<Long>, target: Long): Boolean {
    for (i in input.indices) {
        for (j in i+1 until input.size) {
            if (input[i] + input[j] == target) return true
        }
    }
    return false
}

private fun findContiguousSet(input: List<Long>, target: Long): List<Long> {
    for (i in input.indices) {
        var total = input[i]
        for (j in i+1 until input.size) {
            total += input[j]
            if (total == target) {
                return input.subList(i, j+1)
            } else if (total > target) {
                //Don't need to keep checking
                break;
            }
        }
    }

    return listOf(0L, 0L)
}

private fun execute(input: List<Long>, preamble: Long): Long {
    var (start, end) = listOf(0, preamble.toInt())
    while (end < input.size) {
        if (!isValid(input.subList(start,end), input[end])) {
            return input[end]
        }
        start++
        end++
    }

    return 0L
}

private fun execute2(input: List<Long>, preamble: Long): Long {
    val list = findContiguousSet(input, execute(input, preamble)).sorted()
    return list.first() + list.last()
}