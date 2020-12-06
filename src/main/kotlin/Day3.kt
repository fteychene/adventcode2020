fun main() {
    val rawInput = loadResource("input3")
    val lines = rawInput.split("\n")

    val count = listOf(
        howManyTrees(lines, 1),
        howManyTrees(lines, 3),
        howManyTrees(lines, 5),
        howManyTrees(lines, 7),
        howManyTrees(lines.filterIndexed { i, _ -> i % 2 == 0 }, 1)
    ).fold(1L) { x, y -> x * y }

    println("Found $count trees")
}

fun howManyTrees(lines: List<String>, step: Int) =
    lines.asSequence().zip(generateSequence(0) { it + step })
        .map { (line, index) -> line[index % line.length] }
        .filter { it == '#' }
        .count()