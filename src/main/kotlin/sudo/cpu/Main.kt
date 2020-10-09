package sudo.cpu

import java.util.Random
import kotlin.math.pow

fun main(args: Array<String>) {
    val size = 1
    sumMatrices(size, size)
    linearSearch(size)
    multiplyMatrices(size, size, size)
    radixSort(size)
    bubbleSort(size)
}

fun sumMatrices(row: Int, column: Int) {
    val random = Random()
    val augend = Array(row) { FloatArray(column) { random.nextFloat() } }
    val addend = Array(row) { FloatArray(column) { random.nextFloat() } }
    val summary = Array(row) { FloatArray(column) { random.nextFloat() } }
    val startTime = System.currentTimeMillis()
    for (index in 0 until row) {
        for (innerIndex in 0 until column) {
            summary[index][innerIndex] = augend[index][innerIndex] + addend[index][innerIndex]
        }
    }
    println("Total time: ${System.currentTimeMillis() - startTime}")
}

fun linearSearch(size: Int) {
    val buffer = FloatArray(size)
    val startTime = System.currentTimeMillis()
    val found = buffer.any { it == 1234f }
    println("Total time: ${System.currentTimeMillis() - startTime}")
}

fun multiplyMatrices(row: Int, columnFirst: Int, columnSecond: Int) {
    val random = Random()
    val first = FloatArray(columnFirst * row) { random.nextFloat() }
    val second = FloatArray(row * columnSecond) { random.nextFloat() }
    val result = FloatArray(columnFirst * columnSecond)
    val startTime = System.currentTimeMillis()
    for (i in 0 until row)
        for (j in 0 until columnSecond) {
            var value = 0f
            for (k in 0 until columnFirst)
                value += first[k + i * columnFirst] * second[k * columnSecond + j]
            result[i * columnFirst + j] = value
        }
    println("Total time: ${System.currentTimeMillis() - startTime}")
}

fun radixSort(size: Int) {
    val random = Random()
    val first = IntArray(size) { random.nextInt() }
    val second: Array<Array<Int?>> = Array(19) { Array(size) { null } }
    var continueTask = true
    var loopCount = 0.0
    var currentIndex = 0
    val startTime = System.currentTimeMillis()
    while (continueTask) {
        continueTask = false
        for (index in first.indices) {
            val rem = (first[index] / 10.0.pow(loopCount).toInt()) % 10 + 9
            second[rem][index] = first[index]
            if (rem != 9) {
                continueTask = true
            }
        }
        for (index in second.indices) {
            for (innerIndex in 0 until size) {
                if (second[index][innerIndex] != null) {
                    first[currentIndex] = second[index][innerIndex]!!
                    second[index][innerIndex] = null
                    currentIndex++
                }
            }
        }
        currentIndex = 0
        loopCount += 1.0
    }
    println("Total time: ${System.currentTimeMillis() - startTime}")
}

fun bubbleSort(size: Int) {
    val random = Random()
    val first = IntArray(size) { random.nextInt() }
    val startTime = System.currentTimeMillis()
    for (i in first.indices) {
        for (j in 0 until i) {
            if (first[j] < first[i]) {
                val temp = first[j]
                first[j] = first[i]
                first[i] = temp
            }
        }
    }
    println("Total time: ${System.currentTimeMillis() - startTime}")
}