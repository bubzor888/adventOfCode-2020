package day5

import java.io.File
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    assertEquals(357, readSeat("FBFBBFFRLR"))
    assertEquals(567, readSeat("BFFFBBFRRR"))
    assertEquals(119, readSeat("FFFBBBFRRR"))
    assertEquals(820, readSeat("BBFFBBFRLL"))



    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day5\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")

    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private fun readSeat(input: String): Int {
    val binary = input.map { ch -> when(ch) {
        'B' -> 1
        'F' -> 0
        'R' -> 1
        'L' -> 0
        else -> 0
    } }.joinToString("");

    return binaryToInt(binary.substring(0, 7)) * 8 + binaryToInt(binary.substring(7))
}

private fun binaryToInt(num: String): Int {
    return num.foldIndexed(0) { index, total, digit ->
        total + (digit.toString().toInt() * 2.toDouble().pow(num.length - (index + 1))).toInt()
    }
}

private fun execute(input: List<String>): Int {
    return input.map { readSeat(it) }.maxOrNull()!!
}

private fun execute2(input: List<String>): Int {
    val seats = input.map { readSeat(it) }.sorted()
    for (i in 0 until seats.size - 1) {
        if (seats[i] + 2 == seats[i+1]) {
            return seats[i] + 1
        }
    }

    return -1
}