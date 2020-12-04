package common

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf("")
    assertEquals(0, execute(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\dayX\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")
}

private fun execute(input: List<String>): Int {
    return 0
}