fun main() {

    val rawInput = loadResource("input4")
    val passports = rawInput.split("\n\n")

    val count = passports.map {
        it.split("""[\s\n]""".toRegex())
            .map { it.split(":").let { values -> values[0] to values[1] } }
    }
        .onEach { println("$it") }
        .mapNotNull {
            getPassport(it.mapNotNull(::getField))
        }.count()

    println("Number of valid passports: $count")
}

fun getPassport(fields: List<Field>): Passport? =
    mapN(
        fields.firstInstance<BirthYear>(),
        fields.firstInstance<IssueYear>(),
        fields.firstInstance<ExpirationYear>(),
        fields.firstInstance<Height>(),
        fields.firstInstance<HairColor>(),
        fields.firstInstance<EyeColor>(),
        fields.firstInstance<PassportId>()
    ) { birthYear, issueYear, expirationYear, height, hairColor, eyeColor, passportId ->
        Passport(
            birthYear,
            issueYear,
            expirationYear,
            height,
            hairColor,
            eyeColor,
            passportId,
            fields.firstInstance<CountryId>()
        )
    }

fun getField(pair: Pair<String, String>): Field? = when (pair.first) {
    "byr" -> "\\d{4}".toRegex().matchEntire(pair.second)?.run {
        pair.second.toIntOrNull()?.filter { it in 1920..2002 }?.let(::BirthYear)
    }
    "iyr" -> "\\d{4}".toRegex().matchEntire(pair.second)?.run {
        pair.second.toIntOrNull()?.filter { it in 2010..2020 }?.let(::IssueYear)
    }
    "eyr" -> "\\d{4}".toRegex().matchEntire(pair.second)?.run {
        pair.second.toIntOrNull()?.filter { it in 2020..2030 }?.let(::ExpirationYear)
    }
    "hgt" -> "([\\d]+)cm".toRegex().matchEntire(pair.second)?.run {
        groupValues[1].toInt().filter { it in 120..193  }?.let { value -> Height(value, HeightType.CM) }
    } ?: "([\\d]+)in".toRegex().matchEntire(pair.second)?.run {
        groupValues[1].toInt().filter { it in 59..76  }?.let { value -> Height(value, HeightType.IN) }
    }
    "hcl" -> "#[0-9a-f]{6}".toRegex().matchEntire(pair.second)?.run {
        HairColor(pair.second)
    }
    "ecl" -> pair.second.filter { it == "amb" || it == "blu" || it == "brn" || it == "gry" || it == "grn" || it == "hzl" || it == "oth" }?.let(::EyeColor)
    "pid" -> "\\d{9}".toRegex().matchEntire(pair.second)?.run {
        PassportId(pair.second)
    }
    "cid" -> CountryId(pair.second)
    else -> null
}

data class Passport(
    val birthYear: BirthYear,
    val issueYear: IssueYear,
    val expirationYear: ExpirationYear,
    val height: Height,
    val hairColor: HairColor,
    val eyeColor: EyeColor,
    val passportId: PassportId,
    val countryId: CountryId?
)

enum class HeightType {
    IN, CM
}

sealed class Field()
data class BirthYear(val value: Int) : Field()
data class IssueYear(val value: Int) : Field()
data class ExpirationYear(val value: Int) : Field()
data class Height(val value: Int, val type: HeightType) : Field()
data class HairColor(val value: String) : Field()
data class EyeColor(val value: String) : Field()
data class PassportId(val value: String) : Field()
data class CountryId(val value: String) : Field()

inline fun <reified B> List<*>.firstInstance(): B? =
    filterIsInstance<B>().firstOrNull()

private fun <A> A?.filter(predicate: (A) -> Boolean): A? = this?.let { value -> if (predicate(value)) value else null }

fun <A, B, C, D, E, F, G, R> mapN(
    a: A?,
    b: B?,
    c: C?,
    d: D?,
    e: E?,
    f: F?,
    g: G?,
    action: (A, B, C, D, E, F, G) -> R
): R? =
    a?.let { a ->
        b?.let { b ->
            c?.let { c ->
                d?.let { d ->
                    e?.let { e ->
                        f?.let { f ->
                            g?.let { g ->
                                action(a, b, c, d, e, f, g)
                            }
                        }
                    }
                }
            }
        }
    }

