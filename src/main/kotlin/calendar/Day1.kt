package calendar

import Solver

class Day1: Solver {

    private data class RotationResult(
        val newPosition: Int,
        val passesZero: Int
    )

    private val startPoint = 50

    private fun rotate(direction: Char, magnitude: Int, currentPoint: Int): RotationResult {
        var magnitude = magnitude
        var currentPoint = currentPoint
        var passesZero = 0

        when (direction) {
            'L' -> {
                if (currentPoint > magnitude) {
                    currentPoint -= magnitude
                } else {
                    magnitude -= currentPoint
                    passesZero += (magnitude / 100) +
                            // if we start from zero point and move left, we don't count that as passing zero
                            if (currentPoint != 0) 1 else 0

                    magnitude %= 100
                    currentPoint = if (magnitude == 0) {
                        0
                    } else {
                        100 - magnitude
                    }
                }
            }
            'R' -> {
                if (currentPoint + magnitude < 100) {
                    currentPoint += magnitude
                } else {
                    magnitude -= 100 - currentPoint
                    passesZero += magnitude / 100 + 1
                    currentPoint = magnitude % 100
                }
            }
        }
        return RotationResult(currentPoint, passesZero)
    }

    private fun part1(input: List<Pair<Char, Int>>): Int {
        var currentPoint = startPoint
        var stopsOnZero = 0

        for ((direction, magnitude) in input) {
            rotate(direction, magnitude, currentPoint).let {
                currentPoint = it.newPosition
                if (currentPoint == 0) {
                    stopsOnZero += 1
                }
            }
        }
        return stopsOnZero
    }

    private fun part2(input: List<Pair<Char, Int>>): Int {
        var currentPoint = startPoint
        var passesZero = 0

        for ((direction, magnitude) in input) {
            rotate(direction, magnitude, currentPoint).let {
                currentPoint = it.newPosition
                passesZero += it.passesZero
            }
        }
        return passesZero
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(1)
        val parsed = rawInput.split("\n").map {
            val direction = it.trim()[0]
            val magnitude = it.trim().substring(1).toInt()
            Pair(direction, magnitude)
        }
        return listOf(
            part1(parsed).toString(),
            part2(parsed).toString()
        )
    }
}
