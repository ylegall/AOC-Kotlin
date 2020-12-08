package aoc2020

import aoc2020.Day4.hasAllRequiredFields
import aoc2020.Day4.validPassport
import java.io.File

private object Day4 {

    private val hairColorPattern = Regex("""#[a-f0-9]{6}""")
    private val idPattern = Regex("""\d{9}""")

    private val requiredFields = listOf(
            "byr", // (Birth Year)
            "iyr", // (Issue Year)
            "eyr", // (Expiration Year)
            "hgt", // (Height)
            "hcl", // (Hair Color)
            "ecl", // (Eye Color)
            "pid", // (Passport ID)
    )

    private val validEyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

    fun Map<String, String>.hasAllRequiredFields(): Boolean {
        return requiredFields.all { it in this }
    }

    fun validPassport(passport: Map<String, String>): Boolean {

        if (requiredFields.any { it !in passport }) return false

        if (passport["byr"]!!.toInt() !in 1920 .. 2002) return false
        if (passport["iyr"]!!.toInt() !in 2010 .. 2020) return false
        if ((passport["eyr"]!!).toInt() !in 2020 .. 2030) return false

        val height = passport["hgt"]!!
        if (height.endsWith("cm")) {
            if (height.dropLast(2).toInt() !in 150 .. 193) return false
        } else if (height.endsWith("in")) {
            if (height.dropLast(2).toInt() !in 59 .. 76) return false
        } else {
            return false
        }

        if (!hairColorPattern.matches(passport["hcl"]!!)) return false
        if (passport["ecl"] !in validEyeColors) return false
        if (!idPattern.matches(passport["pid"]!!)) return false

        return true
    }

    fun parseLine(line: String): Map<String, String> {
        val fields = mutableMapOf<String, String>()
        val tokens = line.split(' ', ':')
        for (i in tokens.indices step 2) {
            fields[tokens[i]] = tokens[i + 1]
        }
        return fields
    }

    fun parseLines(lines: List<String>): List<Map<String, String>> {
        val passports = mutableListOf<Map<String, String>>()
        var passport = mutableMapOf<String, String>()
        for (line in lines) {
            if (line.isBlank()) {
                passports.add(passport)
                passport = mutableMapOf()
            } else {
                passport.putAll(parseLine(line))
            }
        }
        return passports + passport
    }

}

fun main() {
    val lines = File("inputs/2020/4.txt").readLines()
    val passports = Day4.parseLines(lines)
    println(passports.count { it.hasAllRequiredFields() })
    println(passports.count { validPassport(it) })
}