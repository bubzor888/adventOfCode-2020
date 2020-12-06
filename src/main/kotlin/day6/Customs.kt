package day6

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf("abc","","a","b","c","","ab","ac","","a","a","a","a","","b")
    assertEquals(11, execute(test1))
    assertEquals(6, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day6\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private class Group() {
    val answers = mutableMapOf<Char, Int>()
    var groupSize = 0
        private set

    fun addAnswer(data: String) {
        groupSize++
        data.forEach {
            answers[it] = answers.getOrPut(it) { 0 } + 1
        }
    }
}

private fun makeGroups(input: List<String>): List<Group> {
    return input.fold(mutableListOf(Group())) { list, data ->
        if (data.isNotBlank()) {
            list.last().addAnswer(data)
        } else {
            list.add(Group())
        }
        list
    }
}

private fun execute(input: List<String>): Int {
    return makeGroups(input).fold(0) { total, group ->
        total + group.answers.size
    }
}

private fun execute2(input: List<String>): Int {
    return makeGroups(input).fold(0) { total, group ->
        total + group.answers.values.fold(0) { groupTotal, count ->
            when(group.groupSize == count) {
                true -> groupTotal + 1
                false -> groupTotal
            }
        }
    }
}