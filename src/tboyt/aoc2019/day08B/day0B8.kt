package tboyt.aoc2019.day08B

import tboyt.aoc2019.util.expectResult
import tboyt.aoc2019.util.readDataFile

fun main() {
    expectResult(getLayers(3, 2, "123456789012"), listOf(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 0, 1, 2)))
    expectResult(printImage(2, 2, "0222112222120000"), " 1\n1 ")
    val inputData = readDataFile("day08")[0]
    println(printImage(25, 6, inputData))
}

fun getLayers(width: Int, height: Int, data: String): List<List<Int>> {
    val dataByLayer = data.chunked(width * height)
    return dataByLayer.map { layerData -> layerData.toList().map { it.toString().toInt() }}
}

fun decodeImage(width: Int, height: Int, data: String): List<Int> {
    val layers = getLayers(width, height, data)
    val image = arrayOfNulls<Int>(data.length)

    for (layer in layers) {
        for ((index, value) in layer.withIndex()) {
            if (image[index] != 0 && image[index] != 1) {
                image[index] = value
            }
        }
    }

    return image.filterNotNull()
}

fun printImage(width: Int, height: Int, data: String): String {
    val image = decodeImage(width, height, data).map { if (it == 0) { " " } else { it } }
    return image.chunked(width).map { it.joinToString("") }.joinToString("\n")
}
