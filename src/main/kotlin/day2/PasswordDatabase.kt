package com.code.aoc2020.common

import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val test1 = listOf("1-3 a: abcde")
    if (execute(test1) == 1) {
        println("Test1 Pass")
    } else {
        println("Test1 Failed")
    }

    val test2 = listOf("1-3 b: cdefg")
    if (execute(test2) == 0) {
        println("Test2 Pass")
    } else {
        println("Test2 Failed")
    }

    val test3 = listOf("2-9 c: ccccccccc")
    if (execute(test3) == 1) {
        println("Test3 Pass")
    } else {
        println("Test3 Failed")
    }

    val path = Paths.get("").toAbsolutePath().toString()
    var fileName = "$path\\src\\main\\kotlin\\day2\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")

}

private fun execute(input: List<String>): Int {
    return input.fold(0) { total, string ->
        //String will be of the form: int-int char: String
        val parts = string.split(" ")
        val min = parts[0].substringBefore("-").toInt()
        val max = parts[0].substringAfter("-").toInt()
        val needle = parts[1][0]

        val count = parts[2].count{ it == needle }
        if (count in min..max) {
            total + 1
        } else {
            total
        }
    }
}