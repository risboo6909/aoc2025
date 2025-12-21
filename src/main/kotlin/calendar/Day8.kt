package calendar

import Solver

class Day8 : Solver {

    private data class Point(val x: Long, val y: Long, val z: Long)

    private fun squaredDist(point1: Point, point2: Point): Long {
        val deltaX = (point1.x - point2.x)
        val deltaY = (point1.y - point2.y)
        val deltaZ = (point1.z - point2.z)
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ
    }

    private fun connectCircuits(
        nodeToCircuit: MutableMap<Point, Int>,
        circuitToNodes: MutableMap<Int, MutableSet<Point>>,
        point1: Point,
        point2: Point
    ) {
        val circuitId1 = nodeToCircuit[point1]!!
        val circuitId2 = nodeToCircuit[point2]

        if (circuitId1 != circuitId2) {
            // merge circuits
            circuitToNodes[circuitId2]!!.forEach {
                nodeToCircuit[it] = circuitId1
                circuitToNodes[circuitId1]!!.add(it)
            }
            circuitToNodes.remove(circuitId2)
        }
    }

    private fun solve(input: List<Point>, iters: Int): Pair<Long, Long> {
        val nodeToCircuit = mutableMapOf<Point, Int>()
        val circuitToNodes = mutableMapOf<Int, MutableSet<Point>>()
        val visited = mutableSetOf<Pair<Point, Point>>()

        var solution1 = 0L
        var solution2: Long

        input.forEachIndexed { idx, p ->
            nodeToCircuit[p] = idx
            circuitToNodes[idx] = mutableSetOf(p)
        }

        var iter = 0

        outer@while (true) {
            var src = input[0]
            var dest = input[0]

            var minDist = Long.MAX_VALUE

            for (idx1 in 0 until input.size) {

                val point1 = input[idx1]

                for (idx2 in idx1 + 1 until input.size) {

                    val point2 = input[idx2]
                    val dist = squaredDist(point1, point2)

                    if (minDist > dist) {
                        if (visited.contains(Pair(point1, point2))) {
                            continue
                        }

                        // found new minimum
                        minDist = dist
                        src = point1
                        dest = point2
                    }

                }

            }

            if (circuitToNodes.size == 2 &&
                circuitToNodes.values.first().size + circuitToNodes.values.last().size == input.size &&
                nodeToCircuit[src] != nodeToCircuit[dest]
            ) {
                solution2 = src.x * dest.x
                break@outer
            }

            connectCircuits(
                nodeToCircuit,
                circuitToNodes,
                src,
                dest
            )

            visited.add(Pair(src, dest))

            if (solution1 == 0L && iter++ >= iters) {
                solution1 = nodeToCircuit
                    .values
                    .groupBy { it }
                    .entries
                    .sortedByDescending { (_, v) -> v.size }
                    .take(3)
                    .fold(1) { acc, (_, v) -> acc * v.size }
            }
        }

        return Pair(solution1, solution2)
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(8)
            .trim()
            .split('\n')

        val points: List<Point> = rawInput.map { line ->
            val parts = line.trim().split(",")
            Point(parts[0].toLong(), parts[1].toLong(), parts[2].toLong())
        }

        val (part1, part2) = solve(points, 1000)
        return listOf(
            part1.toString(),
            part2.toString()
        )
    }
}
