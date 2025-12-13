interface Solver {

    fun run(): List<String>

    fun getRawInput(day: Int): String {
        val inputStream = this::class.java.getResourceAsStream("/day$day.input")
            ?: throw IllegalArgumentException("Input file for day $day not found.")
        return inputStream.bufferedReader().use { it.readText() }
    }
}
