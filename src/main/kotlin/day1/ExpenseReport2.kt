package com.code.aoc2020.day1

import java.io.File
import java.nio.file.Paths

fun main() {
    val test1 = listOf("1721","979","366","299","675","1456");
    if (execute(test1) == 241861950) {
        println("Test1 Pass")
    } else {
        println("Test1 Failed")
    }

    val path = Paths.get("").toAbsolutePath().toString()
    var fileName = "$path\\src\\main\\kotlin\\day1\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")
}

private fun execute(input: List<String>): Int {

    input.forEachIndexed{ index, element ->
        for (i in index + 1 until input.size) {
            for (j in index + 2 until input.size ) {
                if (element.toInt() + input[i].toInt() + input[j].toInt() == 2020) {
                    return element.toInt() * input[i].toInt() * input[j].toInt()
                }
            }
        }
    }
    println("No match found")

    return 0
}