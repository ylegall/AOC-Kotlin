package aoc2019

private fun getBoostKeycode(codes: List<Long>) {
    intCodeProcessor(codes) {
        inputSupplier = { 1 }
    }.run()
}

private fun findDistressCoordinates(codes: List<Long>) {
    intCodeProcessor(codes) {
        inputSupplier = { 2 }
    }.run()
}

fun main() {
    val codes = loadIntCodeInstructions("inputs/2019/9.txt").toList()
    getBoostKeycode(codes)
    findDistressCoordinates(codes)
}