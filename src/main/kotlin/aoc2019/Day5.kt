package aoc2019


fun main() {

    val codes = loadIntCodeInstructions("input.txt")

    fun diagnosticValuePart1() {
        intCodeProcessor(codes) {
            inputSupplier = { 1 }
        }.run()
    }

    fun diagnosticValuePart2() {
        intCodeProcessor(codes) {
            inputSupplier = { 5 }
        }.run()
    }

    diagnosticValuePart1()
    diagnosticValuePart2()
}