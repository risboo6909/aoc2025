package calendar

import Solver
import java.lang.Integer.max

class Day6: Solver {

    val splitRegex = Regex("\\s+")

    private fun part1(rawInput: List<String>): Long {
        val series = mutableListOf<List<Long>>()
        var ops = emptyList<String>()

        for (line in rawInput) {
            if (!line.contains("+") && !line.contains("*")) {
                series.add(
                    line
                        .split(splitRegex)
                        .filter { it.isNotBlank() }
                        .map { it.toLong() }
                )
            } else {
                ops = line.split(splitRegex)
            }
        }

        var result = 0L
        for (colIdx in ops.indices) {
            if (ops[colIdx] == "+") {
                result += series.sumOf { it[colIdx] }
            } else if (ops[colIdx] == "*") {
                result += series.map { it[colIdx] }.reduce { acc, n -> acc * n }
            }
        }
        return result
    }

    private fun part2(rawInput: List<String>): Long {

        // calculate max digits per number per bucket
        val numDigits = mutableMapOf<Int, Int>()
        for (lineIdx in rawInput.dropLast(1).indices) {
            val line = rawInput[lineIdx].split(splitRegex)
            line.forEachIndexed { bucketIdx, numStr ->
                numDigits[bucketIdx] = max(
                    numDigits.getOrDefault(bucketIdx, 0),
                    numStr.length
                )
            }
        }

        // parse operations
        val ops = mutableMapOf<Int, Char>()
        rawInput.takeLast(1)[0]
            .trim()
            .split(splitRegex)
            .forEachIndexed { idx, opStr ->
                ops[idx] = opStr[0]
            }

        // align numbers by digits
        val alignedMatrix = mutableListOf<List<List<Int?>>>()

        rawInput.dropLast(1).forEach { line ->
            val alignedRow = mutableListOf<List<Int?>>()
            val currentNumber = mutableListOf<Int?>()

            var bucketIdx = 0
            var seenDigit = false

            for (c in "$line ") {
                if (c == ' ') {
                   if (!seenDigit) {
                        // put zero instead of leading space
                        currentNumber.add(null)
                   } else {
                       // end of number
                       if (numDigits[bucketIdx]!! > currentNumber.size) {
                           // add one trailing zero if needed to be aligned with max digits
                           currentNumber.add(null)
                           seenDigit = true
                       } else {
                           alignedRow.add(currentNumber.toList()) // .toList() to copy
                           currentNumber.clear()
                           bucketIdx++
                           seenDigit = false
                       }
                   }
                } else if (c.isDigit()) {
                    seenDigit = true
                    currentNumber.add(c.toString().toInt())
                }
            }

            // add last number if needed
            if (currentNumber.isNotEmpty())
                alignedRow.add(currentNumber.toList())

            alignedMatrix.add(alignedRow)
        }

        // alignedMatrix =
        // [
        //   [ [1,2,3], [4,5,6] ],   // first line
        //   [ [7,8,9], [0,1,2] ],
        //   ...
        // ]

        // compute results
        var totalResult = 0L
        (0 until alignedMatrix[0].size).forEach { bucketIdx ->
            val bucketSize = alignedMatrix[0][bucketIdx].size
            val op = ops[bucketIdx]!!

            var bucketResult = if (op == '+') 0L else 1L

            for (digitIdx in 0 until bucketSize) {

                var number = 0
                alignedMatrix.forEach { row ->
                    val digit = row[bucketIdx][digitIdx]
                    if (digit != null) {
                        number = number * 10 + digit
                    }
                }

                if (op == '+') {
                    bucketResult += number
                } else if (op == '*') {
                    bucketResult *= number
                }

            }
            totalResult += bucketResult
        }
        return totalResult
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(6)
            .trim()
            .split('\n')

        return listOf(
            part1(rawInput).toString(),
            part2(rawInput).toString()
        )
    }
}
