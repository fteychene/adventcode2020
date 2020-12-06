fun main() {
    val rawInput = loadResource("input1")
    val values = rawInput.split("\n")
        .map { it.toInt() }.asSequence()

    val result = values
        .combine(values) { x, y -> x to y }
        .combine(values) { (x, y), z -> Triple(x, y, z) }
        .filter { (x, y, z) -> x != y && x != z }
        .filter { (x, y, z) -> x + y + z == 2020 }
        .first()

    println("Found ${result.first} + ${result.second} + ${result.third} == ${result.first + result.second + result.third}")
    println("Combine ${result.first * result.second * result.third}")
}

fun <A, B, C> Sequence<A>.combine(other: Sequence<B>, operator : (A, B) -> C): Sequence<C> =
    flatMap { x -> other.map { y -> operator(x, y)} }