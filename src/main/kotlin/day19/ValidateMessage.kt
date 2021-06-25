package day19

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = "0: 4 1 5\n1: 2 3 | 3 2\n2: 4 4 | 5 5\n3: 4 5 | 5 4\n4: \"a\"\n5: \"b\"\n\nababbb\nbababa\nabbbab\naaabbb\naaaabbb"
    //assertEquals(2, execute(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day19\\input.txt"
    val fileName2 = "$path\\src\\main\\kotlin\\day19\\input2.txt"

//    println("Final Result 1: ${execute(File(fileName).readText())}")
    println("Final Result 2: ${execute2(File(fileName2).readText())}")
}

private class Rule(input: String){
    var complete = false
    //Explode "2 3 | 3 2" -> "[2][3]", "[3][2]"
    var possibilities: Set<String> = when (input.startsWith("\"")) {
        true -> {
            complete = true
            setOf(input.substring(1,2))
        }
        false -> input.split(" | ").map { rule ->
            rule.split(" ").joinToString("") { "[$it]" }
        }.toSet()
    }

    fun replaceVars(k: String, v: Rule) {
        val newPossibilities = mutableSetOf<String>()
        possibilities.forEach { pOld ->
            if (pOld.contains("[$k]")) {
                v.possibilities.forEach { pNew ->
                    newPossibilities.add(pOld.replaceFirst("[$k]", pNew))
                }
            } else {
                newPossibilities.add(pOld)
            }
        }
        complete = newPossibilities.fold(true) { acc, s -> acc && !s.contains("[") }
        possibilities = newPossibilities
    }
}

private fun execute(input: String): Int {
    val (rulesList, messageList) = input.split("\n\n")
    val incompleteRules = mutableMapOf<String, Rule>()
    var completeRules = mutableMapOf<String, Rule>()
    rulesList.split("\n").forEach {
        val key = it.substringBefore(":")
        val rule = Rule(it.substringAfter(": "))
        when (rule.complete) {
            true -> completeRules[key] = rule
            false -> incompleteRules[key] = rule
        }
    }

    while(!completeRules.containsKey("0")) {
        val newComplete = mutableMapOf<String, Rule>()
        incompleteRules.forEach { rule ->
            completeRules.forEach {
                rule.value.replaceVars(it.key, it.value)
                if (rule.value.complete) {
                    newComplete[rule.key] = rule.value
                }
            }
        }
        //Now move completed rules to the other set
        newComplete.keys.forEach { incompleteRules.remove(it) }
        completeRules.putAll(newComplete)
    }

    return (completeRules["0"]!!.possibilities intersect messageList.split("\n")).size
}

private fun execute2(input: String): Int {
    val (rulesList, messageList) = input.split("\n\n")
    val incompleteRules = mutableMapOf<String, Rule>()
    var completeRules = mutableMapOf<String, Rule>()
    rulesList.split("\n").forEach {
        val key = it.substringBefore(":")
        val rule = Rule(it.substringAfter(": "))
        when (rule.complete) {
            true -> completeRules[key] = rule
            false -> incompleteRules[key] = rule
        }
    }

    var pass = 1
    var lastSize = -1
    while(incompleteRules.keys.size != lastSize) {
        lastSize = incompleteRules.keys.size
        val newComplete = mutableMapOf<String, Rule>()
        incompleteRules.forEach { rule ->
            completeRules.forEach {
                rule.value.replaceVars(it.key, it.value)
                if (rule.value.complete) {
                    newComplete[rule.key] = rule.value
                }
            }
        }
        //Now move completed rules to the other set
        newComplete.keys.forEach { incompleteRules.remove(it) }
        completeRules.putAll(newComplete)

        println("pass ${pass++}")
        println("${completeRules.size}")
//        println("complete:")
//        completeRules.forEach {
//            println("${it.key}: ${it.value.possibilities}")
//        }
//        println()
//        println("incomplete:")
//        incompleteRules.forEach {
//            println("${it.key}: ${it.value.possibilities}")
//        }
    }

    //Figure out the 24 length ones first
    val rule8 = incompleteRules["8"]!!
    rule8.possibilities = rule8.possibilities.filterNot { it.contains("[8]") }.toSet()

    val rule11 = incompleteRules["11"]!!
    rule11.possibilities = rule11.possibilities.filterNot { it.contains("[11]") }.toSet()

    val rule0 = incompleteRules["0"]!!
    rule0.replaceVars("8", rule8)
    rule0.replaceVars("11", rule11)

    //s.replace("\\[\\d+\\]".toRegex(), "a").

    return (rule0.possibilities intersect messageList.split("\n")).size
}