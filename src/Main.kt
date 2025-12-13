import calendar.Day1
import calendar.Day2

fun main() {

    val solvers = mapOf(
        1 to Day1(),
        2 to Day2()
    )

    for ((day, solver) in solvers) {
        println("Day $day:")
        val results = solver.run()

        println("  Part 1: ${results[0]}")
        println("  Part 2: ${results[1]}")
        println()
    }

}
