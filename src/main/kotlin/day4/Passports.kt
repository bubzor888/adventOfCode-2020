package day4

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val test1 = listOf(
        "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd",
        "byr:1937 iyr:2017 cid:147 hgt:183cm",
        "",
        "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884",
        "hcl:#cfa07d byr:1929",
        "",
        "hcl:#ae17e1 iyr:2013",
        "eyr:2024",
        "ecl:brn pid:760753108 byr:1931",
        "hgt:179cm",
        "",
        "hcl:#cfa07d eyr:2025 pid:166559648",
        "iyr:2011 ecl:brn hgt:59in")
    assertEquals(2, execute(test1))

    val test2 = listOf(
        "eyr:1972 cid:100",
        "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926",
        "",
        "iyr:2019",
        "hcl:#602927 eyr:1967 hgt:170cm",
        "ecl:grn pid:012533040 byr:1946",
        "",
        "hcl:dab227 iyr:2012",
        "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277",
        "",
        "hgt:59cm ecl:zzz",
        "eyr:2038 hcl:74454a iyr:2023",
        "pid:3556412378 byr:2007",
        "",
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980",
        "hcl:#623a2f",
        "",
        "eyr:2029 ecl:blu cid:129 byr:1989",
        "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm",
        "",
        "hcl:#888785",
        "hgt:164cm byr:2001 iyr:2015 cid:88",
        "pid:545766238 ecl:hzl",
        "eyr:2022",
        "",
        "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
    )
    assertEquals(4, execute2(test2))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day4\\input.txt"

    println("Final Result Part 1: ${execute(File(fileName).readLines())}")

    println("Final Result Part 2: ${execute2(File(fileName).readLines())}")
}

class Passport() {
    private val credentials = hashMapOf<String, String>(
        "byr" to "",
        "iyr" to "",
        "eyr" to "",
        "hgt" to "",
        "hcl" to "",
        "ecl" to "",
        "pid" to "")

    fun addData(data: String) {
        data.split(" ").forEach {
            val (key, value) = it.split(":")
            credentials[key] = value
        }
    }

    fun isValidBasic(): Boolean {
        return credentials.map {
            (key, value) -> value.isNotBlank()
        }.reduce { result, isValid -> result && isValid }
    }

    fun isValidStrong(): Boolean {
        return credentials.map {
            (key, value) -> when(key) {
                "byr" -> """^\d{4}$""".toRegex().containsMatchIn(value) &&
                        value.toInt() >= 1920 && value.toInt() <= 2002
                "iyr" -> """^\d{4}$""".toRegex().containsMatchIn(value) &&
                        value.toInt() >= 2010 && value.toInt() <= 2020
                "eyr" -> """^\d{4}$""".toRegex().containsMatchIn(value) &&
                        value.toInt() >= 2020 && value.toInt() <= 2030
                "hgt" -> ("""^[\d]+(cm)""".toRegex().containsMatchIn(value) &&
                            value.substringBefore("cm").toInt() >= 150 &&
                            value.substringBefore("cm").toInt() <= 193) ||
                        ("""^[\d]+(in)""".toRegex().containsMatchIn(value) &&
                            value.substringBefore("in").toInt() >= 59 &&
                            value.substringBefore("in").toInt() <= 76)
                "hcl" -> """^#[0-9a-f]{6}$""".toRegex().containsMatchIn(value)
                "ecl" -> """^(amb|blu|brn|gry|grn|hzl|oth)$""".toRegex().containsMatchIn(value)
                "pid" -> """^\d{9}$""".toRegex().containsMatchIn(value)
                else -> true
            }
        }.reduce { result, isValid -> result && isValid }
    }
}

private fun execute(input: List<String>): Int {
    val passports = mutableListOf<Passport>(Passport())
    input.forEach { data ->
        when(data.isNotBlank()) {
            true -> passports.last().addData(data)
            false -> passports.add(Passport())
        }
    }

    return passports.fold(0) { total, passport ->
        when(passport.isValidBasic()) {
            true -> total + 1
            false -> total
        }
    }
}

private fun execute2(input: List<String>): Int {
    val passports = mutableListOf<Passport>(Passport())
    input.forEach { data ->
        when(data.isNotBlank()) {
            true -> passports.last().addData(data)
            false -> passports.add(Passport())
        }
    }

    return passports.fold(0) { total, passport ->
        when(passport.isValidStrong()) {
            true -> total + 1
            false -> total
        }
    }
}