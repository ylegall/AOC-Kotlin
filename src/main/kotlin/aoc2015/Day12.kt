package aoc2015

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File


fun main() {
    val integerPattern = Regex("""-?\d+""")

    val input = File("input.txt").readText()

    fun part1() {
        println(integerPattern.findAll(input).sumOf { it.value.toInt() })
    }

    fun JsonElement.isRed() = this.isJsonPrimitive && this.asString == "red"

    fun jsonSum(node: JsonElement): Int {
        return when {
            node.isJsonPrimitive -> try { node.asInt } catch (e: Exception) { 0 }
            node.isJsonArray -> node.asJsonArray.sumOf { jsonSum(it) }
            node.isJsonObject -> {
                val obj = node.asJsonObject
                if (obj.entrySet().any { it.value.isRed() }) {
                    0
                } else {
                    obj.entrySet().sumOf { jsonSum(it.value) }
                }
            }
            else -> throw Exception("bad node type")
        }
    }

    fun part2() {
        val root = JsonParser.parseString(input)
        println(jsonSum(root))
    }

    part1()
    part2()
}