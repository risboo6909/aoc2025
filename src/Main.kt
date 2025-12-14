import calendar.Day1
import calendar.Day2
import kotlin.system.measureTimeMillis

fun main() {

    val solvers = mapOf(
        1 to Day1(),
        2 to Day2()
    )

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

}
