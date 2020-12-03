package day1

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("1721","979","366","299","675","1456");
    assertEquals(241861950, execute(test1))

    println("Tests passed, attempting input")

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