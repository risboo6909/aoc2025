package calendar

import Solver

class Day7: Solver {

    private fun findStart(board: List<MutableList<Char>>): Triple<Int, Int, Long> {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == 'S') {
                    return Triple(i, j, 1)
                }
            }
        }
        throw IllegalArgumentException("Start position not found")
    }

    private fun part1(board: List<MutableList<Char>>): Int {
        var heads = mutableSetOf(findStart(board))
        var totalHits = 0

        do {
            val newHeads = mutableSetOf<Triple<Int, Int, Long>>()

            for (head in heads) {
                val (row, col) = head
                if (row >= board.size)
                    continue

                if (board[row][col] == '^' || board[row][col] == 'v') {
                    newHeads.add(Triple(row, col-1, 1))
                    newHeads.add(Triple(row, col+1, 1))

                    if (board[row][col] == '^') {
                        // flip splitter to avoid double counting
                        board[row][col] = 'v'
                        totalHits++
                    }
                } else {
                    newHeads.add(Triple(row+1, col, 1))
                }
            }
            heads = newHeads

        } while (heads.isNotEmpty())

        return totalHits
    }

    private fun part2(board: List<MutableList<Char>>): Long {
        var heads = mutableListOf(findStart(board))
        var totalPaths = 0L

        do {
            val newHeads = mutableListOf<Triple<Int, Int, Long>>()
            val table = mutableMapOf<Pair<Int, Int>, Long>()

            for (head in heads) {

                val (row, col) = head

                if (row >= board.size) {
                    totalPaths += head.third
                    continue
                }

                if (board[row][col] == '^') {
                    val left = Pair(row, col-1)

                    if (table.containsKey(left)) {
                        table[left] = table[left]!! + head.third
                    } else {
                        table[left] = head.third
                    }

                    val right = Pair(row, col+1)

                    if (table.containsKey(right)) {
                        table[right] = table[right]!! + head.third
                    } else {
                        table[right] = head.third
                    }
                } else {
                    newHeads.add(Triple(row+1, col, head.third))
                }
            }

            for (entry in table) {
                val (pos, count) = entry
                newHeads.add(Triple(pos.first, pos.second, count))
            }

            heads = newHeads

        } while (heads.isNotEmpty())

       return totalPaths
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(7)
            .trim()
            .split('\n')

        val board = mutableListOf<List<Char>>()
        rawInput.forEach { line ->
            board.add(line.trim().toMutableList())
        }

        return listOf(
            part1(board.map{it.toMutableList()}.toList()).toString(),
            part2(board.map{it.toMutableList()}.toList()).toString()
        )
    }
}
