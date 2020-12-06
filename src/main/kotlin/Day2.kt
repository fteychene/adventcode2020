fun main() {
    val rawInput = loadResource("input2")
    val values = rawInput.split("\n")

    val matches = values.map { v -> """(\d+)-(\d+) (\w): (\w+)""".toRegex().matchEntire(v)!! }
        .map { mr ->
            Triple(
                Pair(mr.groupValues[1].toInt() - 1, mr.groupValues[2].toInt() - 1),
                mr.groupValues[3],
                mr.groupValues[4]
            )
        }
        .filter { (b, c, p) ->
            (p[b.first] == c[0] && p[b.second] != c[0]) ||
                    (p[b.first] != c[0] && p[b.second] == c[0])
        }
        .count()

    println("Number of matches: $matches")
}