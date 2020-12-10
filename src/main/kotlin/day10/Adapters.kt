package day10

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("16","10","15","5","1","11","7","19","6","12","4","0")
    assertEquals(35, execute(test1))
    val test2 = listOf("28","33","18","42","31","14","46","20","48","47","24","23","49","45","19","38","39","11","1","32","25","35","8","17","7","9","4","2","34","10","3","0")
    assertEquals(220, execute(test2))

    assertEquals(8, execute2(test1))
    assertEquals(19208, execute2(test2))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day10\\input.txt"
    val input = mutableListOf("0")
    input.addAll(File(fileName).readLines())

    println("Final Result 1: ${execute(input)}")
    println("Final Result 2: ${execute2(input)}")
}

private fun execute(input: List<String>): Int {
    val sortedList = input.map { it.toInt() }.sorted()
    val result = sortedList.mapIndexed { index, num ->
        when(index) {
            input.size - 1 -> 3
            else -> sortedList[index+1] - num
        }
    }.groupBy{ it }.mapValues{ it.value.size }
    return result[1]!! * result[3]!!
}

private class Node {
    var children = mutableListOf<Node>()
    var leafCount = -1L

    fun addChild(child: Node) {
        children.add(child)
    }

    fun isCounted(): Boolean {
        return leafCount >= 0
    }
}

private fun countLeaves(node: Node): Long {
    return when {
        node.children.isEmpty() -> {
            1
        }
        node.isCounted() -> {
            node.leafCount
        }
        else -> {
            val count = node.children.fold(0L) { total, it -> total + countLeaves(it) }
            node.leafCount = count
            count
        }
    }
}

private fun execute2(input: List<String>): Long {
    val sortedList = input.map { it.toInt() }.sorted()

    //Initialize the graph
    val graph = mutableMapOf<Int, Node>()
    sortedList.forEach{
        graph[it] = Node()
    }

    sortedList.forEachIndexed { i, it ->
        for (j in i until sortedList.size) {
            if (sortedList[j] - it in 1..3) {
                graph[it]?.addChild(graph[sortedList[j]]!!)
            }
        }
    }

    return countLeaves(graph[0]!!)
}