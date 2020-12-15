package day15

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    assertEquals(436, execute("0,3,6", 2020))
    assertEquals(1, execute("1,3,2", 2020))
    assertEquals(10, execute("2,1,3", 2020))
    assertEquals(27, execute("1,2,3", 2020))
    assertEquals(78, execute("2,3,1", 2020))
    assertEquals(438, execute("3,2,1", 2020))
    assertEquals(1836, execute("3,1,2", 2020))

    assertEquals(175594, execute("0,3,6", 30000000))
//    assertEquals(2578, execute("1,3,2", 30000000))
//    assertEquals(3544142, execute("2,1,3", 30000000))
//    assertEquals(261214, execute("1,2,3", 30000000))
//    assertEquals(6895259, execute("2,3,1", 30000000))
//    assertEquals(18, execute("3,2,1", 30000000))
//    assertEquals(362, execute("3,1,2", 30000000))
    println("Tests passed, attempting input")

    val input = "14,8,16,0,1,17"
    println("Final Result 1: ${execute(input, 2020)}")
    println("Final Result 2: ${execute(input, 30000000)}")
}

private fun execute(input: String, iterations: Long): Long {
    val memory = mutableMapOf<Long, MutableList<Long>>()
    var lastSpoken = 0L
    input.split(",").forEachIndexed{ i, num ->
        memory[num.toLong()] = mutableListOf(i.toLong()+1)
        lastSpoken = num.toLong()
    }

    for (i in memory.keys.size+1..iterations) {
        val pastOccurrences = memory.getOrPut(lastSpoken) { mutableListOf() }
        lastSpoken = when (pastOccurrences.size == 2) {
            true -> pastOccurrences[1] - pastOccurrences[0]
            false -> 0
        }
        val updateOccurrences = memory.getOrPut(lastSpoken) { mutableListOf() }
        updateOccurrences.add(i)
        if (updateOccurrences.size > 2){
            updateOccurrences.removeAt(0)
        }
    }

    return lastSpoken
}