package day13

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("939","7,13,x,x,59,x,31,19")
    assertEquals(295, execute(test1))

    assertEquals(1068781L, execute2(test1))
    assertEquals(3417L, execute2(listOf("","17,x,13,19")))
    assertEquals(754018L, execute2(listOf("","67,7,59,61")))
    assertEquals(779210L, execute2(listOf("","67,x,7,59,61")))
    assertEquals(1261476L, execute2(listOf("","67,7,x,59,61")))
    assertEquals(1202161486L, execute2(listOf("","1789,37,47,1889")))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day13\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private fun findMatch(busses: List<String>, time: Int): Int {
    busses.forEach{
        if (time % it.toInt() == 0) {
            return it.toInt()
        }
    }
    return -1
}

private fun execute(input: List<String>): Int {
    var time = input[0].toInt()
    val busses = input[1].split(",").filterNot { it == "x" }
    var bus: Int
    while (true) {
        bus = findMatch(busses, time)
        if (bus != -1) {
            break
        }
        time++
    }

    return bus * (time - input[0].toInt())
}

private fun execute2(input: List<String>): Long {
    var time = 0L
    var step = 1L
    input[1].split(",").forEachIndexed { i, bus ->
        if (bus != "x") {
            while ((time + i.toLong()) % bus.toLong() != 0L) {
                time += step
            }
            step *= bus.toLong()
        }
    }

    return time
}