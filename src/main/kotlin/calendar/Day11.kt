package calendar

import Solver

class Day11 : Solver {

    private fun part1(parsed: List<String>): String {
        return "Result Part 1"
    }

    override fun run(): List<String> {
        val parsed = getRawInput(11)
            .trim()
            .split('\n')

        return listOf(
            part1(parsed),
            "Result Part 2"
        )
    }
}
