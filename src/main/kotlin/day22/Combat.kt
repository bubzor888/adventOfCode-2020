package day22

import sun.plugin2.applet2.Plugin2Host
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = "Player 1:\n9\n2\n6\n3\n1\n\nPlayer 2:\n5\n8\n4\n7\n10"
    assertEquals(306, execute(test1))

    assertEquals(291, execute2(test1))
    assertEquals(105, execute2("Player 1:\n43\n19\n\nPlayer 2:\n2\n29\n14"))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day22\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readText())}")
    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun execute(input: String): Long {
    val (p1Input, p2Input) = input.split("\n\n")
    val p1Hand = ArrayDeque(p1Input.substringAfter("Player 1:\n").split("\n").map { it.toInt() })
    val p2Hand = ArrayDeque(p2Input.substringAfter("Player 2:\n").split("\n").map { it.toInt() })
    val deckSize = p1Hand.size + p2Hand.size

    while (p1Hand.isNotEmpty() && p2Hand.isNotEmpty()) {
        val p1Play = p1Hand.removeFirst()
        val p2Play = p2Hand.removeFirst()
        when (p1Play > p2Play) {
            true -> {
                p1Hand.add(p1Play)
                p1Hand.add(p2Play)
            }
            false -> {
                p2Hand.add(p2Play)
                p2Hand.add(p1Play)
            }
        }
    }

    return when (p1Hand.isNotEmpty()) {
        true -> p1Hand.foldIndexed(0L) { i, acc, num -> acc + ((p1Hand.size - i) * num) }
        false -> p2Hand.foldIndexed(0L) { i, acc, num -> acc + ((p2Hand.size - i) * num) }
    }
}

private fun playGame(p1Hand: ArrayDeque<Int>, p2Hand: ArrayDeque<Int>): Int {
    var winner = -1
    val prevRounds = mutableListOf<String>()
    var round = 1
    while (winner == -1) {
        //Check previous rounds first
        val roundString = "${p1Hand.joinToString("")} ${p2Hand.joinToString("")}"
        if (prevRounds.contains(roundString)) {
            winner = 1
            break;
        } else {
            prevRounds.add(roundString)
        }

        val p1Play = p1Hand.removeFirst()
        val p2Play = p2Hand.removeFirst()

        val handWin = when (p1Hand.size >= p1Play && p2Hand.size >= p2Play) {
            true -> playGame(ArrayDeque(p1Hand.subList(0, p1Play)), ArrayDeque(p2Hand.subList(0, p2Play)))
            false -> when (p1Play > p2Play) {
                true -> 1
                false -> 2
            }
        }

        when (handWin) {
            1 -> {
                p1Hand.add(p1Play)
                p1Hand.add(p2Play)
            }
            2 -> {
                p2Hand.add(p2Play)
                p2Hand.add(p1Play)
            }
        }

        if (p1Hand.isEmpty()) {
            winner = 2
        } else if (p2Hand.isEmpty()) {
            winner = 1
        }
        round++
    }
    return winner
}

private fun execute2(input: String): Long {
    val (p1Input, p2Input) = input.split("\n\n")
    val p1Hand = ArrayDeque(p1Input.substringAfter("Player 1:\n").split("\n").map { it.toInt() })
    val p2Hand = ArrayDeque(p2Input.substringAfter("Player 2:\n").split("\n").map { it.toInt() })

    return when (playGame(p1Hand, p2Hand)) {
        1 -> p1Hand.foldIndexed(0L) { i, acc, num -> acc + ((p1Hand.size - i) * num) }
        2 -> p2Hand.foldIndexed(0L) { i, acc, num -> acc + ((p2Hand.size - i) * num) }
        else -> -1
    }
}