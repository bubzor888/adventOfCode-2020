package day25

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("")
    assertEquals(5764801, transform(7, 8))
    assertEquals(17807724, transform(7, 11))
    assertEquals(14897079, transform(17807724, 8))
    assertEquals(14897079, transform(5764801, 11))

    println("reverse")

    assertEquals(8, reverse(14897079, 17807724))
    assertEquals(11, reverse(14897079, 5764801))

    println("Tests passed, attempting input")

    assertEquals(14897079, execute(17807724, 14897079))

    println("Final Result 1: ${execute(16915772, 18447943)}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun transform(subject: Long, loop: Int): Long {
    var result = 1L
    for (i in 0 until loop) {
        result *= subject
        result %= 20201227
    }

    return result
}

private fun reverse(secret: Long, public: Long): Int {
    var loop = 1
    while (transform(public, loop) != secret) {
        loop++
    }
    return loop
}

private fun execute(key1: Long, key2: Long): Long {
    var subject = 7L
    var s1 = mutableListOf<Long>()
    var s2 = mutableListOf<Long>()

    var i = 1
    while ((s1 intersect s2).isEmpty()) {
        s1.add(transform(key1, i))
        s2.add(transform(key2, i))
        i++
    }

    println("Common: ${s1 intersect s2}")
    return (s1 intersect s2).first()
}