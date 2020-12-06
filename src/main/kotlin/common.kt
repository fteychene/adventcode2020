fun loadResource(path: String): String =
    object {}.javaClass.getResource(path).readText()