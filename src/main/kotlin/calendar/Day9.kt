package calendar

import kotlin.math.abs
import java.lang.Long.max
import java.lang.Long.min
import Solver

class Day9 : Solver {

    private data class Point(val x: Long, val y: Long)
    private data class Segment(val start: Point, val end: Point)

    private fun getRectArea(point1: Point, point2: Point): Long {
        val width = abs(point1.x - point2.x) + 1
        val height = abs(point1.y - point2.y) + 1
        return width * height
    }

    private fun isVertical(seg: Segment): Boolean {
        return seg.start.x == seg.end.x
    }

    private fun segmentSegmentIntersection(seg1: Segment, seg2: Segment): Boolean {
        // check if two segments intersect
        if (isVertical(seg1)  && !isVertical(seg2)) {
            val intersectsX = seg1.start.x >= min(seg2.start.x, seg2.end.x) &&
                              seg1.start.x <= max(seg2.start.x, seg2.end.x)
            val intersectsY = seg2.start.y > min(seg1.start.y, seg1.end.y) &&
                              seg2.start.y < max(seg1.start.y, seg1.end.y)
            return intersectsX && intersectsY
        } else if (!isVertical(seg1) && isVertical(seg2)) {
            val intersectsX = seg2.start.x > min(seg1.start.x, seg1.end.x) &&
                              seg2.start.x < max(seg1.start.x, seg1.end.x)
            val intersectsY = seg1.start.y >= min(seg2.start.y, seg2.end.y) &&
                              seg1.start.y <= max(seg2.start.y, seg2.end.y)
            return intersectsX && intersectsY
        }
        return false
    }

    private fun generateRectSegments(point1: Point, point2: Point): List<Segment> {
        val topLeft = Point(
            minOf(point1.x, point2.x),
            minOf(point1.y, point2.y)
        )
        val bottomRight = Point(
            maxOf(point1.x, point2.x),
            maxOf(point1.y, point2.y)
        )

        val topRight = Point(bottomRight.x, topLeft.y)
        val bottomLeft = Point(topLeft.x, bottomRight.y)

        return listOf(
            Segment(topLeft, topRight),
            Segment(topRight, bottomRight),
            Segment(bottomRight, bottomLeft),
            Segment(bottomLeft, topLeft)
        )
    }

    private fun solve(input: List<Point>, dontCrossBorders: Boolean): Long {

        val outerSegments = mutableSetOf<Segment>()
        if (dontCrossBorders) {
            // compute border segments by following the points from input
            for (idx in input.indices) {
                val from = input[idx]
                val to = input[(idx + 1) % input.size]
                outerSegments.add(
                    Segment(from, to)
                )
            }
        }

        var maxArea = 0L

        for (i in input.indices) {

            nextPoint@for (j in i + 1 until input.size) {
                val point1 = input[i]
                val point2 = input[j]
                val area = getRectArea(point1, point2)

                if (area > maxArea) {

                    if (dontCrossBorders) {
                        for (borderSeg in generateRectSegments(point1, point2)) {
                            for (outerSeg in outerSegments) {
                                if (segmentSegmentIntersection(borderSeg, outerSeg))
                                   continue@nextPoint
                            }
                        }
                    }

                    maxArea = area
                }
            }
        }

        return maxArea
    }

    private fun part1(input: List<Point>): Long {
        return solve(input, false)
    }

    private fun part2(input: List<Point>): Long {
        return solve(input, true)
    }

    override fun run(): List<String> {
        val parsed = getRawInput(9)
            .trim()
            .split('\n')
            .map {
                val (x, y) = it
                    .trim()
                    .split(',')
                    .map { number -> number.toLong() }
                Point(x, y)
            }

        return listOf(
            part1(parsed).toString(),
            part2(parsed).toString()
        )
    }
}