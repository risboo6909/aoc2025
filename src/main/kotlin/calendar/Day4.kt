package calendar

import Solver

class Day4: Solver {

    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
        0 to -1,           0 to 1,
        1 to -1,  1 to 0,  1 to 1
    )

    private fun countNeighbours(input: Array<CharArray>, rowIdx: Int, colIdx: Int): Int {
        var count = 0

        for ((dRow, dCol) in directions) {
            val newRow = rowIdx + dRow
            val newCol = colIdx + dCol

            if (newRow in input.indices && newCol in input[0].indices) {
                if (input[newRow][newCol] == '@') {
                    count++
                }
            }
        }
        return count
    }

    private fun part1(input: Array<CharArray>): Int {
        var total = 0
        input.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, c ->
                if (c == '@') {
                    val neighbours = countNeighbours(input, rowIdx, colIdx)
                    total += if (neighbours < 4) 1 else 0
                }
            }
        }
        return total
    }

    private fun part2(input: Array<CharArray>): Int {
        var total = 0
        do {
            var boardChanged = false
            input.forEachIndexed { rowIdx, row ->
                row.forEachIndexed { colIdx, c ->
                    if (c == '@') {
                        val neighbours = countNeighbours(input, rowIdx, colIdx)
                        total += if (neighbours < 4) {
                            input[rowIdx][colIdx] = 'x'
                            boardChanged = true
                            1
                        } else 0
                    }
                }
            }
        } while(boardChanged)

        return total
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(4).trim().split('\n')

        val rows = rawInput.size
        val cols = rawInput[0].trim().length
        val parsed = Array(rows) { CharArray(cols) }

        rawInput.forEachIndexed {
            rowIndex, line ->
            line.trim().forEachIndexed { colIndex, c ->
                parsed[rowIndex][colIndex] = c
            }
        }

        return listOf(
            part1(parsed).toString(),
            part2(parsed).toString()
        )
    }
}