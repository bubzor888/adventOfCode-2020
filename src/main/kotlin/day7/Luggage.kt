package day7

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf(
        "light red bags contain 1 bright white bag, 2 muted yellow bags.",
        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.",
        "bright white bags contain 1 shiny gold bag.",
        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.",
        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.",
        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.",
        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.",
        "faded blue bags contain no other bags.",
        "dotted black bags contain no other bags."
    )
    assertEquals(4, execute(test1))

    assertEquals(32, execute2(test1))
    val test2 = listOf(
        "shiny gold bags contain 2 dark red bags.",
        "dark red bags contain 2 dark orange bags.",
        "dark orange bags contain 2 dark yellow bags.",
        "dark yellow bags contain 2 dark green bags.",
        "dark green bags contain 2 dark blue bags.",
        "dark blue bags contain 2 dark violet bags.",
        "dark violet bags contain no other bags."
    )
    assertEquals(126, execute2(test2))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day7\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")
}

private val pattern = """(\d+) (\p{Print}+) (bag)(s)?(\.)?""".toRegex()

private fun makeRules(input: List<String>): HashMap<String, ArrayList<Pair<String, Int>>> {
    val rules = HashMap<String, ArrayList<Pair<String, Int>>>()
    input.forEach { line ->
        val (container, bags) = line.split(" bags contain ")
        val bagList = ArrayList<Pair<String, Int>>()
        if ("no other bags." != bags) {
            bags.split(", ").forEach { bag ->
                val (count, color) = pattern.matchEntire(bag)!!.destructured
                bagList.add(Pair(color, count.toInt()))
            }
        }
        rules[container] = bagList
    }
    return rules
}

private fun findBag(rules: HashMap<String, ArrayList<Pair<String, Int>>>, targetColor: String): Set<String> {
    val result = HashSet<String>()
    rules.forEach{ rule ->
        rule.value.forEach{
            if (targetColor == it.first) {
                result.add(rule.key)
                result.addAll(findBag(rules, rule.key))
            }
        }
    }
    return result
}

private fun countBags(rules: HashMap<String, ArrayList<Pair<String, Int>>>, targetColor: String): Int {
    var count = 0
    rules.forEach{ rule ->
        if (targetColor == rule.key && rule.value.isNotEmpty()) {
            rule.value.forEach {
                count += it.second + it.second * countBags(rules, it.first)
            }
        }
    }
    return count
}

private fun execute(input: List<String>): Int {
    return findBag(makeRules(input), "shiny gold").size
}

private fun execute2(input: List<String>): Int {
    return countBags(makeRules(input), "shiny gold")
}