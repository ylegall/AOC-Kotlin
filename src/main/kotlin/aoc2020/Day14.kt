package aoc2020

import java.io.File

private val maskPattern  = Regex("""mask = (\w+)""")
private val writePattern = Regex("""mem\[(\d+)\] = (\d+)""")

// TODO: clean up

private fun maskValue(value: Long, mask: String): Long {
    var result = value
    for (i in mask.indices) {
        when (mask[i]) {
            '1' -> result = result or (1L shl (36 - i - 1))
            '0' -> result = result and (1L shl (36 - i - 1)).inv()
        }
    }
    return result
}

private fun expandAddress(addressTemplate: String): List<String> {
    val results = mutableListOf<String>()

    fun expand(addressTemplate: String, address: String, pos: Int) {
        if (pos >= addressTemplate.length) {
            results.add(address)
            return
        }
        val char = addressTemplate[pos]
        if (char == 'X') {
            expand(addressTemplate, address + '1', pos + 1)
            expand(addressTemplate, address + '0', pos + 1)
        } else {
            expand(addressTemplate, address + char, pos + 1)
        }
    }
    expand(addressTemplate, "", 0)
    return results
}

private fun maskAddress(address: Long, mask: String): List<Long> {
    val addressString = Array(36) { '0' }
    var bit = 1L shl 35
    for (i in mask.indices) {
        when (mask[i]) {
            '1' -> addressString[i] = '1'
            '0' -> addressString[i] = if ((address and bit) != 0L) '1' else '0'
            'X' -> addressString[i] = 'X'
        }
        bit = bit shr 1
    }
    //println(addressString.contentToString())
    return expandAddress(String(addressString.toCharArray())).map { it.toLong(2) }
}

private fun sumMemeoryValues(lines: List<String>): Long {
    var mask = ""
    val memory = mutableMapOf<Long, Long>()

    for (line in lines) {
        val match = maskPattern.find(line)
        if (match != null) {
            mask = match.groupValues[1]
        } else {
            val result = writePattern.find(line) ?: throw Exception("invalid line: $line")
            val address = result.groupValues[1].toLong()
            val writeValue = result.groupValues[2].toLong()
            val maskedValue = maskValue(writeValue, mask)
            memory[address] = maskedValue
        }
    }
    return memory.entries.fold(0L) { acc, entry -> acc + entry.value }
}

private fun expandAndSumMemeoryValues(lines: List<String>): Long {
    var mask = ""
    val memory = mutableMapOf<Long, Long>()

    for (line in lines) {
        val match = maskPattern.find(line)
        if (match != null) {
            mask = match.groupValues[1]
        } else {
            val result = writePattern.find(line) ?: throw Exception("invalid line: $line")
            val address = result.groupValues[1].toLong()
            val writeValue = result.groupValues[2].toLong()
            val addresses = maskAddress(address, mask)
            for (address in addresses) {
                memory[address] = writeValue
            }
        }
    }
    return memory.entries.fold(0L) { acc, entry -> acc + entry.value }
}


fun main() {
    val input = File("inputs/2020/14.txt").readLines()
    println(sumMemeoryValues(input))

    println(expandAndSumMemeoryValues(input))
}