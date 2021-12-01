package aoc2020

import java.io.File

// TODO: clean up
private object Day16 {

    private val fieldPattern = Regex("""(.+): (\d+)-(\d+) or (\d+)-(\d+)""")

    data class FieldRange(
            val range1: IntRange,
            val range2: IntRange
    ) {
        operator fun contains(value: Int) = value in range1 || value in range2
    }

    class TicketData(
            val ticket: List<Int>,
            val otherTickets: List<List<Int>>,
            val fields: Map<String, FieldRange>
    )

    private fun parseField(line: String): Pair<String, FieldRange> {
        val match = fieldPattern.find(line)
        val (name, low1, high1, low2, high2) = match!!.destructured
        return name to FieldRange(low1.toInt() .. high1.toInt(), low2.toInt() .. high2.toInt())
    }

    fun parseInput(lines: List<String>): TicketData {
        var mode = 0
        var ticket = emptyList<Int>()
        val fields = mutableMapOf<String, FieldRange>()
        val otherTickets = mutableListOf<List<Int>>()

        for (line in lines) {
            if (line.isBlank()) continue
            when {
                line.isBlank() -> continue
                line == "your ticket:" || line == "nearby tickets:" -> {
                    mode++
                    continue
                }
            }
            when (mode) {
                0 -> {
                    val (field, range) = parseField(line)
                    fields[field] = range
                }
                1 -> ticket = line.split(',').map { it.toInt() }
                2 -> otherTickets.add(line.split(',').map { it.toInt() })
            }
        }
        return TicketData(ticket, otherTickets, fields)
    }

    private fun isValueInvalid(value: Int, fields: Map<String, FieldRange>): Boolean {
        return fields.values.none { field -> value in field }
    }

    private fun isTicketValid(ticket: List<Int>, fields: Map<String, FieldRange>): Boolean {
        return ticket.all { value ->
            fields.values.any { value in it }
        }
    }

    fun totalScanningError(ticketData: TicketData): Int {
        var sum = 0
        for (ticket in ticketData.otherTickets) {
            for (value in ticket) {
                if (isValueInvalid(value, ticketData.fields)) {
                    sum += value
                }
            }
        }
        return sum
    }

    private fun isValueValidForField(value: Int, field: String, fields: Map<String, FieldRange>): Boolean {
        return value in fields[field]!!
    }

    private fun fieldAssignmentMatchRate(field: String, index: Int, data: TicketData): Int {
        return data.otherTickets.sumBy { ticket ->
            if (isValueValidForField(ticket[index], field, data.fields)) 1 else 0
        }
    }

    fun departureFieldsProduct(ticketData: TicketData): Long {
        val validOtherTickets = ticketData.otherTickets.filter { ticket -> isTicketValid(ticket, ticketData.fields) }
        val data = TicketData(ticketData.ticket, validOtherTickets, ticketData.fields)

        val numFields = data.otherTickets.size
        val fieldMap = mutableMapOf<String, Int>()
        val remainingIndices = (0 until data.fields.size).toMutableSet()
        val usedIndices = mutableSetOf<Int>()

        while (remainingIndices.isNotEmpty()) {
            val remainingFields = data.fields.keys.filter { it !in fieldMap }
            for (field in remainingFields) {
                val sortedMatches = remainingIndices.map { idx -> idx to fieldAssignmentMatchRate(field, idx, data) }
                        .sortedByDescending { it.second }
                //println(sortedMatches)
                if (sortedMatches.size == 1) {
                    val bestIndex = sortedMatches.first().first
                    fieldMap[field] = bestIndex
                    usedIndices.add(bestIndex)
                    remainingIndices.remove(bestIndex)
                } else if (sortedMatches.first().second == numFields && sortedMatches[1].second != numFields) {
                    val bestIndex = sortedMatches.first().first
                    fieldMap[field] = bestIndex
                    usedIndices.add(bestIndex)
                    remainingIndices.remove(bestIndex)
                } else {
                    continue
                }
            }
        }

        //println(fieldMap)

        var prod = 1L
        for (fieldName in data.fields.keys) {
            if (fieldName.startsWith("departure")) {
                val index = fieldMap[fieldName]!!
                println("$fieldName: ticket[$index] = ${data.ticket[index]}")
                prod *= data.ticket[index]
                println(prod)
            }
        }

        return prod
    }
}

fun main() {
    val lines = File("inputs/2020/16.txt").readLines()
    val data = Day16.parseInput(lines)
    println(Day16.totalScanningError(data))
    println(Day16.departureFieldsProduct(data))
}