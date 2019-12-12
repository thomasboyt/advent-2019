package tboyt.aoc2019.day08A

import tboyt.aoc2019.util.expectResult
import tboyt.aoc2019.util.readDataFile

fun main() {
    expectResult(getLayers(3, 2, "123456789012"), listOf(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 0, 1, 2)))
    val inputData = readDataFile("day08")[0]
    println(analyzeImage(25, 6, inputData))
}

fun getLayers(width: Int, height: Int, data: String): List<List<Int>> {
    val dataByLayer = data.chunked(width * height)
    return dataByLayer.map { layerData -> layerData.toList().map { it.toString().toInt() }}
}

fun countDigit(layer: List<Int>, digit: Int): Int {
    return layer.filter { it == digit }.size
}

fun analyzeImage(width: Int, height: Int, data: String): Int {
    val layers = getLayers(width, height, data)
    // find layer with fewest 0 digits
    // number of 1 digits * number of 2 digits
    val fewestZeroLayer = layers.reduce { acc, layer ->
        if (countDigit(acc, 0) > countDigit(layer, 0)) { layer } else { acc }
    }
    return countDigit(fewestZeroLayer, 1) * countDigit(fewestZeroLayer, 2)
}