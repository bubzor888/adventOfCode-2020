package day6

import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val path = Paths.get("").toAbsolutePath().toString()
    val groups = File("$path\\src\\main\\kotlin\\day6\\input.txt").readText()
        .split("\n\n")
        .map {
            it.trimEnd().lines().map { p ->
                ('a'..'z').map { c -> if (c in p) 1 else 0 }.joinToString("").toInt(2)
            }
        }
    println("Part 1: ${groups.sumBy { it.reduce { acc, i -> acc or i }.countOneBits() }}")
    println("Part 2: ${groups.sumBy { it.reduce { acc, i -> acc and i }.countOneBits() }}")
}