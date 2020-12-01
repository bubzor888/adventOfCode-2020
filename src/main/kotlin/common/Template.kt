package com.code.aoc2020.common

import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val test1 = listOf("")
    if (execute(test1) == 1) {
        println("Test1 Pass")
    } else {
        println("Test1 Failed")
    }

//    val test2 = listOf("")
//    if (execute(test2) == 2) {
//        println("Test2 Pass")
//    } else {
//        println("Test2 Failed")
//    }
//
//    val test3 = listOf("")
//    if (execute(test3) == 3) {
//        println("Test3 Pass")
//    } else {
//        println("Test3 Failed")
//    }

    val path = Paths.get("").toAbsolutePath().toString()
    var fileName = "$path\\src\\main\\kotlin\\day1\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")

}

private fun execute(input: List<String>): Int {
    return 0
}