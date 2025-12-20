import calendar.Day1
import calendar.Day2
import calendar.Day3
import calendar.Day4
import calendar.Day5
import calendar.Day6
import calendar.Day7
import kotlin.system.measureTimeMillis

fun main() {

    val solvers = mapOf(
        1 to Day1(),
        2 to Day2(),
        3 to Day3(),
        4 to Day4(),
        5 to Day5(),
        6 to Day6(),
        7 to Day7(),
    )

    try {
        val totalTime = measureTimeMillis {
            for ((day, solver) in solvers) {
                println("Day $day:")

                var results: List<Any>
                val time = measureTimeMillis {
                    results = solver.run()
                }

                println("  Part 1: ${results[0]}")
                println("  Part 2: ${results[1]}")
                println("  Time: ${time}ms")
                println()
            }
        }

        println("Total time: ${totalTime}ms")
    } catch (e: Exception) {
        println("Error occurred: ${e.message}")
        e.printStackTrace()
        throw e
    }
}
