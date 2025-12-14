package calendar

import Solver

class Day2 : Solver {

    private fun hasRepeatingSubstring(number: Long, splitHalvesOnly: Boolean): Boolean {
        if (number < 10) {
            return false
        }

        val numberAsString = number.toString()

        if (splitHalvesOnly && numberAsString.length % 2 != 0) {
            return false
        }

        val startIndex = if (splitHalvesOnly) {
            numberAsString.length / 2
        } else {
            1
        }

        for (i in startIndex until (numberAsString.length / 2) + 1) {
            val chunked = numberAsString.chunked(i)
            if (chunked.toSet().size == 1) {
                return true
            }
        }
        return false
    }

    private fun solve(input: List<Pair<Long, Long>>, splitHalvesOnly: Boolean): Long {
        var total: Long = 0
        for (pair in input) {
            for (num in pair.first..pair.second) {
                if (hasRepeatingSubstring(num, splitHalvesOnly)) {
                    total += num
                }
            }
        }
        return total
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(2)
        val parsed = rawInput.trim().split(",").map {
            val tmp = it.split("-")
            tmp[0].toLong() to tmp[1].toLong()
        }
        return listOf(
            solve(parsed, splitHalvesOnly = true).toString(),
            solve(parsed, splitHalvesOnly = false).toString()
        )
    }
}
