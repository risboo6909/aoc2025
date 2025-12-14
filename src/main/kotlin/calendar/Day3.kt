package calendar

import Solver

class Day3 : Solver {

    val inversePowerOfTenTable: DoubleArray = doubleArrayOf(
        1.0 / 100_000_000_000.0,
        1.0 / 10_000_000_000.0,
        1.0 / 1_000_000_000.0,
        1.0 / 100_000_000.0,
        1.0 / 10_000_000.0,
        1.0 / 1_000_000.0,
        1.0 / 100_000.0,
        1.0 / 10_000.0,
        1.0 / 1_000.0,
        1.0 / 100.0,
        1.0 / 10.0,
        1.0
    )

    private fun part1(input: List<List<Int>>): Int {
        var total = 0
        for (line in input) {
            val firstDigit = line
                .dropLast(1)
                .withIndex()
                .maxBy {(_, value) -> value}
            val secondDigit = line.subList(firstDigit.index + 1, line.size)
                .max()

            val number = 10 * firstDigit.value + secondDigit
            total += number
        }
        return total
    }

    private fun part2(input: List<List<Int>>): Long {
        var best: Long = 0
        var total: Long = 0

        fun helper(line: List<Int>, acc: Long, pos: Int, digit: Int) {
            if (digit == 12) {
                if (acc > best) {
                    best = acc
                }
                return
            }

            var biggestDigit = 0
            for (i in pos until line.size) {
                if (line[i] < biggestDigit) {
                    continue
                }
                biggestDigit = line[i]

                val tmp = best * inversePowerOfTenTable[digit]
                val newAcc = 10 * acc + line[i]
                if (newAcc < tmp) {
                    continue
                }

                helper(line, newAcc, i + 1, digit + 1)
            }
        }
        for (line in input) {
            helper(line, 0, 0, 0)
            total += best
            best = 0
        }
        return total
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(3).trim().split('\n')
        val parsed = rawInput.map {
            line -> line.toList().map { it.digitToInt() }
        }
        return listOf(
            part1(parsed).toString(),
            part2(parsed).toString()
        )
    }
}
