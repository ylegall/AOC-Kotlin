package aoc2019

import java.io.File

private const val WIDTH = 25
private const val HEIGHT = 6
private const val LAYER_SIZE = WIDTH * HEIGHT

private fun productOfOneAndTwoDigits(layerData: List<List<Int>>) {
    val layerDigitCounts = layerData.map { layer -> layer.groupingBy { it }.eachCount().withDefault { 0 } }
    val product = layerDigitCounts.minByOrNull { it.getValue(0) }?.let { it.getValue(1) * it.getValue(2) }
    println(product)
}

private fun compileLayers(layerData: List<List<Int>>): List<List<Int>> {
    return (0 until HEIGHT).map { row ->
        (0 until WIDTH).map { col ->
            layerData.imagePixelValue(row, col)
        }
    }
}

private fun List<List<Int>>.imagePixelValue(row: Int, col: Int) =
        dropWhile { it.layerPixelValue(row, col) == 2 }.firstOrNull()?.layerPixelValue(row, col) ?: 2

private fun List<Int>.layerPixelValue(row: Int, col: Int) = this[row * WIDTH + col]

private fun printImage(finalImage: List<List<Int>>) {
    println(
        finalImage.joinToString("\n") { row ->
            row.joinToString("") { if (it == 1) "#" else " " }
        }
    )
}

private fun parseInput() = File("inputs/2019/8.txt").readText().trim().map { it - '0' }.chunked(LAYER_SIZE)

fun main() {
    val layerData = parseInput()
    productOfOneAndTwoDigits(layerData)
    printImage(compileLayers(layerData))
}