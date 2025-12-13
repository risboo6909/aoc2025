package calendar

import Solver

class Day2: Solver {

    private fun part1(input: String): Int {
        return 1
    }

    private fun part2(input: String): Int {
        return 2
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(1)
        return listOf(
            part1(rawInput).toString(),
            part2(rawInput).toString()
        )
    }
}