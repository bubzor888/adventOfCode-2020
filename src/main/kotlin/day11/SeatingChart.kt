package day11

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf(
        "L.LL.LL.LL",
        "LLLLLLL.LL",
        "L.L.L..L..",
        "LLLL.LL.LL",
        "L.LL.LL.LL",
        "L.LLLLL.LL",
        "..L.L.....",
        "LLLLLLLLLL",
        "L.LLLLLL.L",
        "L.LLLLL.LL")
    assertEquals(37, execute(test1))

    assertEquals(26, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day11\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

const val FLOOR = '.'
const val EMPTY = 'L'
const val OCCUPIED = '#'

private fun countOccupiedAdjacent(chart: List<List<Char>>, x: Int, y: Int): Int {
    var total = 0
    for (posX in x-1..x+1) {
        for (posY in y-1..y+1) {
            when {
                posX == x && posY == y -> continue
                isValidPosition(chart[0].size, chart.size, posX, posY)
                        && chart[posY][posX] == OCCUPIED -> total++
            }
        }
    }
    return total
}

private fun isValidPosition(width: Int, height: Int, x: Int, y: Int): Boolean {
    return when {
        y in 0 until height && x in 0 until width -> true
        else -> false
    }
}

private fun countOccupiedLineOfSight(chart: List<List<Char>>, x: Int, y: Int): Int {
    var total = 0
    val plus1: (Int) -> Int = { num -> num + 1}
    val minus1: (Int) -> Int = { num -> num - 1}
    val same: (Int) -> Int = { num -> num }
    total += firstSeatOccupied(chart, x, y, plus1, same)
    total += firstSeatOccupied(chart, x, y, minus1, same)
    total += firstSeatOccupied(chart, x, y, same, plus1)
    total += firstSeatOccupied(chart, x, y, same, minus1)
    total += firstSeatOccupied(chart, x, y, plus1, plus1)
    total += firstSeatOccupied(chart, x, y, plus1, minus1)
    total += firstSeatOccupied(chart, x, y, minus1, plus1)
    total += firstSeatOccupied(chart, x, y, minus1, minus1)

    return total
}

private fun firstSeatOccupied(chart: List<List<Char>>, x: Int, y: Int, xOperation: (Int) -> Int, yOperation: (Int) -> Int): Int {
    var posX = xOperation(x)
    var posY = yOperation(y)
    while (isValidPosition(chart[0].size, chart.size, posX, posY)) {
        when(chart[posY][posX]) {
            OCCUPIED -> return 1
            EMPTY -> return 0
        }
        posX = xOperation(posX)
        posY = yOperation(posY)
    }

    return 0
}

private fun processSeats(input: List<String>, tolerance: Int, adjacentOnly: Boolean): Int {
    var lastArrangement = input.map { it.toList() }
    var seatsChanged = true
    while (seatsChanged) {
        seatsChanged = false
        val newArrangement = mutableListOf<MutableList<Char>>()
        lastArrangement.forEachIndexed { y, row ->
            newArrangement.add(mutableListOf())
            row.forEachIndexed { x, seat ->
                if (seat != FLOOR) {
                    val adjacent = when (adjacentOnly) {
                        true -> countOccupiedAdjacent(lastArrangement, x, y)
                        false -> countOccupiedLineOfSight(lastArrangement, x, y)
                    }
                    when {
                        seat == EMPTY && adjacent == 0 -> {
                            newArrangement[y].add(OCCUPIED)
                            seatsChanged = true
                        }
                        seat == OCCUPIED && adjacent >= tolerance -> {
                            newArrangement[y].add(EMPTY)
                            seatsChanged = true
                        }
                        else -> newArrangement[y].add(seat)
                    }
                } else {
                    newArrangement[y].add(seat)
                }
            }
        }

        lastArrangement = newArrangement
    }
    return lastArrangement.fold(0) { total, row ->
        total + row.count { seat -> seat == OCCUPIED }
    }
}

private fun execute(input: List<String>): Int {
    return processSeats(input,4, true)
}

private fun execute2(input: List<String>): Int {
    return processSeats(input, 5, false)
}