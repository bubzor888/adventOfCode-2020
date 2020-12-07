package common

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf("")
    assertEquals(0, execute(test1))

    println("Tests passed, attempting input")

//    val path = Paths.get("").toAbsolutePath().toString()
//    val fileName = "$path\\src\\main\\kotlin\\dayX\\input.txt"
//
//    println("Final Result 1: ${execute(File(fileName).readLines())}")
//    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun execute(input: List<String>): Int {
    return 0
}

private fun execute2(input: List<String>): Int {
    return 0
}