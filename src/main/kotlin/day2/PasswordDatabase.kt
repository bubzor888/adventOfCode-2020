package day2

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf("1-3 a: abcde")
    assertEquals(1, execute(test1))

    val test2 = listOf("1-3 b: cdefg")
    assertEquals(0, execute(test2))

    val test3 = listOf("2-9 c: ccccccccc")
    assertEquals(1, execute(test3))

    println("Tests passed, attempting input")

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