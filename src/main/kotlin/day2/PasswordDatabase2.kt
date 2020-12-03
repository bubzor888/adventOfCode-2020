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
    assertEquals(0, execute(test3))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    var fileName = "$path\\src\\main\\kotlin\\day2\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")

    println("Option 2: ${option2(File(fileName).readLines())}")
}

private fun execute(input: List<String>): Int {
    return input.fold(0) { total, string ->
        //String will be of the form: int-int char: String
        val parts = string.split(" ")
        val position1 = parts[0].substringBefore("-").toInt() - 1
        val position2 = parts[0].substringAfter("-").toInt() - 1
        val needle = parts[1][0]

        if ((parts[2][position1] == needle && parts[2][position2] != needle) ||
                (parts[2][position1] != needle && parts[2][position2] == needle)) {
            total + 1
        } else {
            total
        }
    }
}

private fun option2(input: List<String>): Int {
    val pattern = """(\d+)-(\d+) (\p{Print}): (\p{Print}*)""".toRegex()

    return input.fold(0) { total, string ->
        val (position1, position2, needle, haystack) = pattern.matchEntire(string)!!.destructured

        //We want true/false or false/true, so true/true and false/false will fail
        if ((haystack[position1.toInt() - 1] == needle.single()) !=
                (haystack[position2.toInt() - 1] == needle.single())) {
            total + 1
        } else {
            total
        }
    }
}