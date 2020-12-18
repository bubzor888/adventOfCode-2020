package day17

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf(
        ".#.",
        "..#",
        "###")
    assertEquals(848, execute(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day17\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")
}

private fun printGrid(grid: Map<String, Char>, zRange: IntRange, xRange: IntRange, yRange: IntRange, cycles: Int) {
    println("Cycle: $cycles")
    println()
    for (z in zRange) {
        println("z=$z")
        for (y in yRange) {
            var row = StringBuffer()
            for (x in xRange) {
                row.append(grid["$z,$x,$y"])
            }
            println("$row")
        }
        println()
    }
}

private class Grid2(input: List<String>) {
    var wMin = 0
    var wMax = 0
    var zMin = 0
    var zMax = 0
    var xMin= 0
    var yMin = 0
    var xMax = input[0].length - 1
    var yMax = input.size - 1
    var cycles = 0
    var grid = mutableMapOf<String, Char>()

    init {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                grid["0,0,$x,$y"] = ch
            }
        }
    }

    fun printGrid() {
        println("Cycle: $cycles")
        println()
        for (w in wMin..wMax) {
            for (z in zMin..zMax) {
                println("z=$z, w=$w")
                for (y in yMin..yMax) {
                    var row = StringBuffer()
                    for (x in xMin..xMax) {
                        row.append(grid["$z,$x,$y"])
                    }
                    println("$row")
                }
                println()
            }
        }
    }

    fun nextCycle() {
        val newGrid = mutableMapOf<String, Char>()
        wMin--
        zMin--
        xMin--
        yMin--
        wMax++
        zMax++
        xMax++
        yMax++
        for (w in wMin..wMax) {
            for (z in zMin..zMax) {
                for (y in yMin..yMax) {
                    for (x in xMin..xMax) {
                        val activeNeighbors = countActiveNeighbors(w, z, x, y)
                        val pos = "$w,$z,$x,$y"
                        when (grid.getOrDefault(pos) { '.' }) {
                            '#' -> {
                                when (activeNeighbors in 2..3) {
                                    true -> newGrid[pos] = '#'
                                    false -> newGrid[pos] = '.'
                                }
                            }
                            else -> {
                                when (activeNeighbors == 3) {
                                    true -> newGrid[pos] = '#'
                                    false -> newGrid[pos] = '.'
                                }
                            }
                        }
                    }
                }
            }
        }
        grid = newGrid
        cycles++
    }

    private fun countActiveNeighbors(w: Int, z: Int, x: Int, y:Int): Int {
        var count = 0
        val thisPos = "$w,$z,$x,$y"
        for (wPos in w-1..w+1) {
            for (zPos in z - 1..z + 1) {
                for (yPos in y - 1..y + 1) {
                    for (xPos in x - 1..x + 1) {
                        val newPos = "$wPos,$zPos,$xPos,$yPos"
                        if (newPos != thisPos && grid.getOrDefault(newPos) { '.' } == '#') {
                            count++
                        }
                    }
                }
            }
        }
        return count
    }

    fun countTotalActive(): Int {
        var count = 0
        for (w in wMin..wMax) {
            for (z in zMin..zMax) {
                for (y in yMin..yMax) {
                    for (x in xMin..xMax) {
                        if (grid.getOrDefault("$w,$z,$x,$y") { '.' } == '#') {
                            count++
                        }
                    }
                }
            }
        }
        return count
    }
}

private fun execute(input: List<String>): Int {
    val grid = Grid2(input)

    for (i in 0..5) {
        grid.nextCycle()
    }

    return grid.countTotalActive()
}