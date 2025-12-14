package calendar

import Solver

class Day3 : Solver {

    private fun part1(input: String): Int {
        return 0
    }

    private fun part2(input: String): Int {
        return 1
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(3)
        return listOf(
            part1(rawInput).toString(),
            part2(rawInput).toString()
        )
    }

}