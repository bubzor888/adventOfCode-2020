package day18

import java.io.File
import java.nio.file.Paths
import kotlin.math.exp
import kotlin.test.assertEquals

fun main() {
    assertEquals(71, execute(listOf("1 + 2 * 3 + 4 * 5 + 6")))
    assertEquals(51, execute(listOf("1 + (2 * 3) + (4 * (5 + 6))")))
    assertEquals(26, execute(listOf("2 * 3 + (4 * 5)")))
    assertEquals(437, execute(listOf("5 + (8 * 3 + 9 + 3 * 4 * 3)")))
    assertEquals(12240, execute(listOf("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    assertEquals(13632, execute(listOf("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))

    assertEquals(231, execute2(listOf("1 + 2 * 3 + 4 * 5 + 6")))
    assertEquals(51, execute2(listOf("1 + (2 * 3) + (4 * (5 + 6))")))
    assertEquals(46, execute2(listOf("2 * 3 + (4 * 5)")))
    assertEquals(1445, execute2(listOf("5 + (8 * 3 + 9 + 3 * 4 * 3)")))
    assertEquals(669060, execute2(listOf("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    assertEquals(23340, execute2(listOf("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day18\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private fun findMatchingParen(s: String): Int {
    var lParen = 0
    s.forEachIndexed { index, ch ->
        when (ch) {
            '(' -> lParen++
            ')' -> {
                when (lParen == 0) {
                    true -> return index
                    false -> lParen--
                }
            }
        }
    }
    return s.length
}

private fun evaluate(exp: String, lToR: (String) -> Long): Long {
    return when (exp.contains("(")) {
        true -> {
            val before = exp.substringBefore("(")
            val after = exp.substringAfter("(")
            val afterEnd = findMatchingParen(after)
            return evaluate("$before${evaluate(after.substring(0,afterEnd), lToR)}${after.substring(afterEnd+1)}", lToR)
        }
        false -> lToR(exp)
    }
}

val normalPrecedence: (String) -> Long = { exp ->
    var lastOp = "+"
    exp.split(" ").fold(0L){ acc, s ->
        when (s) {
            "+", "*" -> {
                lastOp = s
                acc
            }
            else -> {
                when (lastOp) {
                    "+" -> acc + s.toLong()
                    "*" -> acc * s.toLong()
                    else -> acc
                }
            }
        }
    }
}

val addFirst: (String) -> Long = { exp ->
    exp.split(" * ").fold(1L){ acc, s ->
        acc * normalPrecedence(s)
    }
}

private fun execute(input: List<String>): Long {
    return input.map { evaluate(it, normalPrecedence) }.sum()
}

private fun execute2(input: List<String>): Long {
    return input.map { evaluate(it, addFirst) }.sum()
}