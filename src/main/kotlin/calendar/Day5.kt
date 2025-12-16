package calendar

import Solver

class Day5: Solver {

    // all intervals are inclusive
    private data class Interval(
        val start: Long,
        val end: Long,
    )

    private fun isInsideInterval(interval: Interval, n: Long): Boolean {
        return n >= interval.start && n <= interval.end
    }

    private fun part1(intervals: List<Interval>, ingredients: Set<Long>): Int {
        var insideAnyIntervalCount = 0
        for (ingredient in ingredients) {
            for (interval in intervals) {
                if (isInsideInterval(interval, ingredient)) {
                    insideAnyIntervalCount++
                    break
                }
            }
        }
        return insideAnyIntervalCount
    }

    private fun part2(intervals: List<Interval>): Long {
        val intervalsSorted = intervals.sortedBy { it.start }
        val result = mutableListOf(intervalsSorted[0])

        for (curInterval in intervalsSorted) {

            val lastInterval = result.last()

            if (lastInterval.end >= curInterval.start) {
                // merge intervals
                result[result.size - 1] = Interval(
                    start = minOf(lastInterval.start, curInterval.start),
                    end = maxOf(lastInterval.end, curInterval.end)
                )
            } else {
                result.add(curInterval)
            }

        }

        return result.sumOf { it.end - it.start + 1 }
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(5).trim().split('\n')
        val intervals = mutableListOf<Interval>()
        val ingredients = mutableSetOf<Long>()

        var isIngredientsSection = false

        for (line in rawInput) {
            val line = line.trim()
            if (line.isEmpty()) {
                isIngredientsSection = true
                continue
            }
            if (isIngredientsSection) {
                // process ingredients
                ingredients.add(line.toLong())
            } else {
                // process intervals
                val (start, end) = line.split('-')
                intervals.add(Interval(start.toLong(), end.toLong()))
            }
        }

        return listOf(
            part1(intervals, ingredients).toString(),
            part2(intervals).toString()
        )
    }
}