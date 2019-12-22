package aoc2015

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File

private val integerPattern = Regex("-?\\d+")

private fun jsonSum(string: String): Int {
    val root = JsonParser().parse(string)
    return jsonSum(root)
}

private fun jsonSum(node: JsonElement): Int {
    return when {
        node.isJsonPrimitive -> try { node.asInt } catch (e: Exception) { 0 }
        node.isJsonArray -> node.asJsonArray.map { jsonSum(it) }.sum()
        node.isJsonObject -> {
            node.asJsonObject.takeIf {
                it.entrySet().none { it.value.isRed() }
            }?.entrySet()?.map {
                jsonSum(it.value)
            }?.sum() ?: 0
        }
        else -> throw Exception("bad node type")
    }
}

private fun JsonElement.isRed() = this.isJsonPrimitive && this.asString == "red"

fun main() {
    val input = File("inputs/2015/12.txt").useLines { it.toList().joinToString("") }

    println(integerPattern.findAll(input).mapNotNull { it.value.toInt() }.sum())

    val totalWithoutRed = jsonSum(input)
    println(totalWithoutRed)
}