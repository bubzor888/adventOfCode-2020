package day16

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    assertEquals(71, execute("class: 1-3 or 5-7\nrow: 6-11 or 33-44\nseat: 13-40 or 45-50\n\nyour ticket:\n7,1,14\n\nnearby tickets:\n7,3,47\n40,4,50\n55,2,20\n38,6,12"))
//    assertEquals(1716, execute2("class: 0-1 or 4-19\nrow: 0-5 or 8-19\nseat: 0-13 or 16-19\n\nyour ticket:\n11,12,13\n\nnearby tickets:\n3,9,18\n15,1,5\n5,14,9"))
    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day16\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readText())}")
    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private class Rule(input: String) {
    val name: String
    val constraint1: IntRange
    val constraint2: IntRange

    val pattern = """(\p{Print}+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()

    init {
        val parts = pattern.matchEntire(input)!!.destructured
        name = parts.component1()
        constraint1 = parts.component2().toInt()..parts.component3().toInt()
        constraint2 = parts.component4().toInt()..parts.component5().toInt()
    }

    fun passes(num: Int): Boolean {
        return num in constraint1 || num in constraint2
    }

    override fun toString(): String {
        return "$name $constraint1 or $constraint2"
    }
}

private class RuleSet(rules: List<Rule>) {
    val rules = rules

    fun passes(num: Int): Boolean {
        return rules.fold(false) { pass, rule -> pass || rule.passes(num) }
    }

    fun validRules(num: Int): List<Int> {
        return rules.foldIndexed(mutableListOf<Int>()){ i, rulesPassed, rule ->
            if (rule.passes(num)) {
                rulesPassed.add(i)
            }
            rulesPassed
        }
    }
}

private fun execute(input: String): Int {
    val sections = input.split("\n\n")
    val ruleSet = RuleSet(sections[0].split("\n").map { Rule(it) })

    return sections[2]
        .substringAfter("\n")
        .split("\n")
        .sumBy { ticket ->
            ticket.split(",").sumBy { num ->
                //For each ticket number, check if it passes a rule
                when (ruleSet.passes(num.toInt())) {
                    true -> 0
                    false -> num.toInt()
                }
            }
        }
}

private fun execute2(input: String): Long {
    val sections = input.split("\n\n")
    val ruleSet = RuleSet(sections[0].split("\n").map { Rule(it) })

    val tickets = sections[2]
        .substringAfter("\n")
        .split("\n")
        .map { ticket ->
            ticket.split(",").map {
                it.toInt()
            }
        }
        .filter { ticket ->
            ticket.fold(true) { allPass, num ->
                allPass && ruleSet.passes(num)
            }
        }

    // Below, Map Key will be the row of each ticket
    // Map Value is a list of each number in the ticket
    // Each of those lists contain a list of which rules are valid for that number
    val validRules = mutableMapOf<Int, MutableList<List<Int>>>()
    for (i in tickets[0].indices) {
        val v = mutableListOf<List<Int>>()
        for (j in tickets.indices) {
            v.add(ruleSet.validRules(tickets[j][i]))
        }
        validRules[i] = v
    }

    // Below, Map Key will be the position of the numbers in the tickets
    // Map Value now a set of intersections of valid rules for that number position
    var intersections = validRules.map {
        it.value.fold(it.value[0].toSet()) { acc, list ->
            acc intersect list
        }
    }.toMutableList()

    // When a set has only 1 valid rule, we add that to the results and empty out the entry
    // We also need to remove that position from all of the other sets since it can't be used twice
    val results = mutableMapOf<Int, String>()
    while (results.keys.size < intersections.size) {
        val toFilter = mutableListOf<Int>()
        intersections.forEachIndexed { i, it ->
            if (it.size == 1) {
                results[i] = ruleSet.rules[it.first()].name
                intersections[i] = setOf()
                toFilter.add(it.first())
            }
        }
        toFilter.forEach{ num ->
            intersections = intersections.map { rule ->
                rule.filterNot{ it == num }.toSet()
            }.toMutableList()
        }
    }

    val myTicket = sections[1].substringAfter("\n").split(",")

    //Finally we filter down to the "departure" rules and multiply the values from our ticket
    return results.filter { it.value.startsWith("departure") }.keys.fold(1L) { acc, index ->
        acc * myTicket[index].toInt()
    }
}