package aoc2019

import java.util.ArrayDeque
import java.util.Scanner

private object Day25 {
    
    private val codes = loadIntCodeInstructions("inputs/2019/25.txt")
    
    private fun String.expandCommand() = when(this) {
        "n" -> "north"
        "e" -> "east"
        "s" -> "south"
        "w" -> "west"
        else -> this
    }
    
    fun exploreShip() {
        val outputParser = OutputParser()
        val scanner = Scanner(System.`in`)
        val inputQueue = ArrayDeque<Int>()
        
        fun getNextCommand(): Long {
            
            if (inputQueue.isEmpty()) {
                
                println("""
                    room data:
                        room name: ${outputParser.roomName}
                        doors    : ${outputParser.doors}
                        items    : ${outputParser.items}
                """.trimIndent())
                
                if (outputParser.isNotEmpty()) {
                    outputParser.clear()
                }
                val command = scanner.nextLine().expandCommand().map { it.toInt() }
                inputQueue.apply { addAll(command); add(10) }
            }
            return inputQueue.poll().toLong()
        }
        
        fun processOutput(charCode: Long) {
            print(charCode.toChar())
            outputParser.append(charCode)
        }
        
        val processor = intCodeProcessor(codes) {
            inputSupplier = { getNextCommand() }
            outputConsumer = { processOutput(it) }
        }
        processor.run()
    }
    
    private class OutputParser {
        private val lineBuffer = mutableListOf<String>()
        private val buffer = StringBuilder()
        private enum class Mode { NONE, DOORS, ITEMS }
        private var mode = Mode.NONE
        var roomName = ""
        val items = mutableListOf<String>()
        val doors = mutableListOf<String>()
        
        fun append(code: Long) {
            val char = code.toChar()
            if (char == '\n') {
                val lastLine = buffer.toString()
                if (lastLine.isEmpty()) {
                    mode = Mode.NONE
                }
                
                when (mode) {
                    Mode.NONE -> {
                        if (lastLine.startsWith("==")) {
                            roomName = lastLine
                        }
                        mode = when (lastLine) {
                            "Doors here lead:" -> Mode.DOORS
                            "Items here:"      -> Mode.ITEMS
                            else               -> mode
                        }
                    }
                    Mode.DOORS -> doors.add(lastLine.substring(2))
                    Mode.ITEMS -> items.add(lastLine.substring(2))
                }
                
                if (lastLine.isNotBlank()) {
                    lineBuffer.add(lastLine)
                }
                buffer.clear()
            } else {
                buffer.append(char)
            }
        }
        
        fun isNotEmpty() = buffer.isNotEmpty() || lineBuffer.isNotEmpty()
        
        fun clear() {
            lineBuffer.clear()
            buffer.clear()
            doors.clear()
            items.clear()
        }
    }
}

fun main() {
    Day25.exploreShip()
}

