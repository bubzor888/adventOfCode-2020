package day12

import java.io.File
import java.nio.file.Paths
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("F10","N3","F7","R90","F11")
    assertEquals(25, execute(test1))
    assertEquals( 286, execute2(test1))
    println("Tests passed, attempting input")


    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day12\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private val pattern = """(\p{Print})(\d+)""".toRegex()

val N: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { p, v -> Pair(p.first, p.second+v)} // 270
val S: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { p, v -> Pair(p.first, p.second-v)} // 90
val E: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { p, v -> Pair(p.first+v, p.second)} // 0
val W: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { p, v -> Pair(p.first-v, p.second)} // 180
val WP: (Pair<Int, Int>, Pair<Int, Int>) -> Pair<Int, Int> = { p, w -> Pair(p.first+ w.first, p.second+w.second)}
val R: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { p, v ->
    when(v) {
        90 -> Pair(p.second, p.first * -1)
        180 -> Pair(p.first * -1, p.second * -1)
        270 -> Pair(p.second * -1, p.first)
        else -> p
    }
}


private fun execute(input: List<String>): Int {
    var position = Pair(0, 0)
    var facing = 0
    input.forEach {
        val (action, value) = pattern.matchEntire(it)!!.destructured
        when(action) {
            "N" -> position = N(position, value.toInt())
            "S" -> position = S(position, value.toInt())
            "E" -> position = E(position, value.toInt())
            "W" -> position = W(position, value.toInt())
            "R" -> facing = (facing + value.toInt()) % 360
            "L" -> facing = (facing - value.toInt()) % 360
            "F" -> {
                when(facing) {
                    0 -> position = E(position, value.toInt())
                    90, -270 -> position = S(position, value.toInt())
                    180, -180 -> position = W(position, value.toInt())
                    270, -90 -> position = N(position, value.toInt())
                }
            }
        }
    }

    return position.first.absoluteValue + position.second.absoluteValue
}

private fun execute2(input: List<String>): Int {
    var ship = Pair(0, 0)
    var waypoint = Pair(10, 1)
    input.forEach {
        val (action, value) = pattern.matchEntire(it)!!.destructured
        when(action) {
            "N" -> waypoint = N(waypoint, value.toInt())
            "S" -> waypoint = S(waypoint, value.toInt())
            "E" -> waypoint = E(waypoint, value.toInt())
            "W" -> waypoint = W(waypoint, value.toInt())
            "R" -> waypoint = R(waypoint, value.toInt())
            "L" -> waypoint = R(waypoint, 360 - value.toInt())
            "F" -> repeat(value.toInt()) { ship = WP(ship, waypoint) }
        }
    }

    return ship.first.absoluteValue + ship.second.absoluteValue
}