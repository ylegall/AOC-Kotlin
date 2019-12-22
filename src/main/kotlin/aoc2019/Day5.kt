package aoc2019

private object Day5 {

    fun diagnosticValuePart1() {
        val codes = loadIntCodeInstructions("inputs/2019/5.txt")
        intCodeProcessor(codes) {
            inputSupplier = { 1 }
        }.run()
    }

    fun diagnosticValuePart2() {
        val codes = loadIntCodeInstructions("inputs/2019/5.txt")
        intCodeProcessor(codes) {
            inputSupplier = { 5 }
        }.run()
    }
}

fun main() {
    Day5.diagnosticValuePart1()
    Day5.diagnosticValuePart2()
}