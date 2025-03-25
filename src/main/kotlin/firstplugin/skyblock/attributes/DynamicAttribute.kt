package firstplugin.skyblock.attributes

interface DynamicAttribute : Attribute {
    var current: Double

    override val value: Double
        get() = max

    val max: Double
        get() {
            var result = baseValue

            // Constant increase like +10 Health
            val constantSum = constantModifiers.sumOf { it.effect }
            result += constantSum

            // Percentage increases like +5%
            val additiveSum = 1.0 + additiveModifiers.sumOf { it.effect }
            result *= additiveSum

            // Multipliers like x1.1, x1.3, etc.
            val multiplier =
                if (multiplicativeModifiers.isEmpty()) {
                    1.0
                } else {
                    multiplicativeModifiers
                        .map { it.effect }
                        .reduce { acc, value -> acc * value }
                }
            result *= multiplier

            return result
        }
}
