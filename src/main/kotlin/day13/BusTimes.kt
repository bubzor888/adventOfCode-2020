package day13

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("939","7,13,x,x,59,x,31,19")
    assertEquals(295, execute(test1))

    //assertEquals(1068781L, execute2(test1))
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
    var bus = -1
    while (true) {
        bus = findMatch(busses, time)
        if (bus != -1) {
            break
        }
        time++
    }

    return bus * (time - input[0].toInt())
}

private fun checkBusses(busses: List<Pair<Long, Int>>, time: Long): Boolean {
    busses.forEach {
        //println("${time - it.second} % ${it.first} ${(time-it.second) % it.first}")
        if ((time - it.second) % it.first != 0L) {
            return false
        }
    }

    return true
}

private fun gcd(a: Long, b: Long): Long {
    var (aVar, bVar) = listOf(a, b)

    while (bVar > 0) {
        var temp = bVar
        bVar = aVar % bVar
        aVar = temp
    }
    return aVar
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

private fun execute2(input: List<String>): Long {
    val busses = input[1].split(",").reversed().mapIndexed { index, it ->
        when (it) {
            "x" -> Pair(1L, 0)
            else -> Pair(it.toLong(), index)
        }
    }.filterNot { it == Pair(1L, 0) }.sortedByDescending { it.first }

    println(busses)

    //Pair is (Value, Offset)
    //X values are removed

//    var lcm = busses[0].first
//    for (i in 1 until busses.size) {
//        lcm = lcm(lcm, busses[i].first + busses[i].second)
//    }

    var time = busses[0].first
    while(!checkBusses(busses, time)) {
        time += busses[0].first
    }

    return time - busses.size
}