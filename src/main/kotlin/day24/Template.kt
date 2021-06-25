package day24

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf(
        "sesenwnenenewseeswwswswwnenewsewsw",
        "neeenesenwnwwswnenewnwwsewnenwseswesw",
        "seswneswswsenwwnwse",
        "nwnwneseeswswnenewneswwnewseswneseene",
        "swweswneswnenwsewnwneneseenw",
        "eesenwseswswnenwswnwnwsewwnwsene",
        "sewnenenenesenwsewnenwwwse",
        "wenwwweseeeweswwwnwwe",
        "wsweesenenewnwwnwsenewsenwwsesesenwne",
        "neeswseenwwswnwswswnw",
        "nenwswwsewswnenenewsenwsenwnesesenew",
        "enewnwewneswsewnwswenweswnenwsenwsw",
        "sweneswneswneneenwnewenewwneswswnese",
        "swwesenesewenwneswnwwneseswwne",
        "enesenwswwswneneswsenwnewswseenwsese",
        "wnwnesenesenenwwnenwsewesewsesesew",
        "nenewswnwewswnenesenwnesewesw",
        "eneswnwswnwsenenwnwnwwseeswneewsenese",
        "neswnwewnwnwseenwseesewsenwsweewe",
        "wseweeenwnesenwwwswnew")
    assertEquals(10, execute(test1, 0))
    assertEquals(15, execute(test1, 1))
    assertEquals(37, execute(test1, 10))
//    assertEquals(2208, execute(test1, 100))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day24\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines(), 0)}")
    println("Final Result 2: ${execute(File(fileName).readLines(), 100)}")
}

private fun findCoordinates(directions: List<String>): Pair<Int, Int> {
    var result = Pair(0,0)
    directions.forEach {
        when (it) {
            "e" -> result = Pair(result.first+2, result.second)
            "w" -> result = Pair(result.first-2, result.second)
            "se" -> result = Pair(result.first+1, result.second-1)
            "sw" -> result = Pair(result.first-1, result.second-1)
            "ne" -> result = Pair(result.first+1, result.second+1)
            "nw" -> result = Pair(result.first-1, result.second+1)
        }
    }
    return result
}

private fun flipTiles(blackTiles: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    var (xMin, yMin, xMax, yMax) = listOf(0,0,0,0)
    //Figure out the ranges of the tiles
    blackTiles.forEach {
        if (it.first < xMin) xMin = it.first
        if (it.first > xMax) xMax = it.first
        if (it.second < yMin) yMin = it.second
        if (it.second > yMax) yMax = it.second
    }
    //Move one tile further, to account for the white tiles around those edge tiles
    xMin--
    yMin--
    xMax++
    yMax++

    val newBlackTiles = blackTiles.toMutableList()
    for (x in xMin..xMax) {
        for (y in yMin..yMax) {
            val tile = Pair(x,y)
            val adjacent = countAdjacent(tile, blackTiles)
            when (blackTiles.contains(tile)) {
                true -> { //Black tile: apply 0 or > 2 logic
                    if (adjacent == 0 || adjacent > 2) {
                        newBlackTiles.remove(tile)
                    }
                }
                false -> { //White tile: apply exactly 2 logic
                    if (adjacent == 2) {
                        newBlackTiles.add(tile)
                    }
                }
            }
        }
    }

    return newBlackTiles
}

private fun countAdjacent(tile: Pair<Int, Int>, blackTiles: List<Pair<Int, Int>>): Int {
    var count = 0

    if (blackTiles.contains(Pair(tile.first-1, tile.second-1))) count++
    if (blackTiles.contains(Pair(tile.first-2, tile.second))) count++
    if (blackTiles.contains(Pair(tile.first-1, tile.second+1))) count++
    if (blackTiles.contains(Pair(tile.first+1, tile.second-1))) count++
    if (blackTiles.contains(Pair(tile.first+2, tile.second))) count++
    if (blackTiles.contains(Pair(tile.first+1, tile.second+1))) count++

    return count
}

private fun execute(input: List<String>, days: Int): Int {
    var blackTiles = mutableListOf<Pair<Int, Int>>()

    input.map {
        val sb = StringBuffer(it)
        val list = mutableListOf<String>()
        while (sb.isNotEmpty()) {
            when (val direction = sb[0]) {
                's','n' -> {
                    list.add("$direction${sb[1]}")
                    sb.delete(0,2)
                }
                else -> {
                    list.add(direction.toString())
                    sb.deleteCharAt(0)
                }
            }

        }
        list
    }.forEach {
        val flip = findCoordinates(it)
        if (!blackTiles.remove(flip)) {
            //If it wasn't in the list, then add it
            blackTiles.add(flip)
        }
    }

    //Now do each day's flipping
    for (i in 1..days) {
        blackTiles = flipTiles(blackTiles).toMutableList()
        if (i%10 == 0) {
            println("Black tiles after $i days: ${blackTiles.size}")
        }
    }

    return blackTiles.size
}