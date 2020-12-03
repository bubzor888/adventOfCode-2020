package day3

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf("..##.......","#...#...#..",".#....#..#.","..#.#...#.#",".#...##..#.","..#.##.....",".#.#.#....#",".#........#","#.##...#...","#...##....#",".#..#...#.#")
    assertEquals(2, checkSlope(test1, 1, 1))
    assertEquals(7, checkSlope(test1, 3, 1))
    assertEquals(3, checkSlope(test1, 5, 1))
    assertEquals(4, checkSlope(test1, 7, 1))
    assertEquals(2, checkSlope(test1, 1, 2))
    assertEquals(336, execute(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    var fileName = "$path\\src\\main\\kotlin\\day3\\input.txt"

    println("Final Result: ${execute(File(fileName).readLines())}")

}

private fun checkSlope(input: List<String>, right: Int, down: Int): Int {
    var count = 0
    var x = right
    for (y in down until input.size step down) {
        if (input[y][x] == '#') {
            count++
        }

        x = (x + right) % input[y].length
    }

    return count
}

private fun execute(input: List<String>): Long {
    var total: Long = 1
    total *= checkSlope(input, 1, 1)
    total *= checkSlope(input, 3, 1)
    total *= checkSlope(input, 5, 1)
    total *= checkSlope(input, 7, 1)
    total *= checkSlope(input, 1, 2)

    return total;
}