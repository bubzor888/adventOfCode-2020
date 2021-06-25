package day23

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    assertEquals("92658374", execute("389125467", 10))
    assertEquals("67384529", execute("389125467", 100))
    println("Part 1 tests passed")

    assertEquals(149245887792L, execute2("389125467", 10000000))

    println("Part 2 tests passed, attempting input")

    println("Final Result 1: ${execute("952438716", 100)}")
    println("Final Result 2: ${execute2("952438716", 10000000)}")

}

private class Cups(initial: List<Int>, additional: Int) {
    var current = Cup(-1)
    val cup1: Cup

    init {
        var lastCup = Cup(-1)
        val initialMap = mutableMapOf<Int, Cup>()
        initial.forEachIndexed { i, c ->
            val newCup = Cup(c)
            initialMap[c] = newCup
            if (i == 0) {
                current = newCup
            } else {
                lastCup.next = newCup
            }
            lastCup = newCup
        }
        //Populate the minusOnes of the initial list
        initialMap.values.forEach {
            it.minusOne = when (it.label > 1) {
                true -> initialMap[it.label-1]!!
                false -> initialMap[initial.size]!!
            }
        }
        //Keep track of cup1 for later
        cup1 = initialMap[1]!!

        //See if we need to pad additional nodes
        if (additional > 0) {
            //Do one manually so the minus one is right:
            val newCup = Cup(initial.size+1)
            lastCup.next = newCup
            newCup.minusOne = initialMap[initial.size]!!
            lastCup = newCup
            for (i in initial.size+2..additional) {
                val newCup = Cup(i)
                lastCup.next = newCup
                newCup.minusOne = lastCup
                lastCup = newCup
            }
            //Since we added more nodes, remap the minus one for cup1
            cup1.minusOne = lastCup
        }
        //Finally loop the last cup back to the first
        lastCup.next = current
    }

    fun playRound() {
        val pickup1 = current.next
        val pickup2 = pickup1.next
        val pickup3 = pickup2.next
        current.next = pickup3.next

        var target = current.minusOne
        while (target == pickup1 || target == pickup2 || target == pickup3) {
            target = target.minusOne
        }
        val temp = target.next
        target.next = pickup1
        pickup3.next = temp

        current = current.next
    }

    fun print(): String {
        var next = cup1.next
        val sb = StringBuffer()
        while (next != cup1) {
            sb.append(next?.label)
            next = next.next
        }
        return sb.toString()
    }
}

private class Cup(label: Int) {
    var next = this
    var minusOne = this
    var label = label
}

private fun execute(input: String, rounds: Int): String {
    val cups = Cups(input.map { Character.getNumericValue(it) }, 0)
    for (i in 1..rounds) {
        cups.playRound()
    }

    return cups.print()
}

private fun execute2(input: String, rounds: Int): Long {
    val cups = Cups(input.map { Character.getNumericValue(it) }, 1000000)
    for (i in 1..rounds) {
        cups.playRound()
    }

    return cups.cup1.next.label.toLong() * cups.cup1.next.next.label.toLong()
}