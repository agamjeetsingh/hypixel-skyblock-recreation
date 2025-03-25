package firstplugin.skyblock.attributes

import net.kyori.adventure.text.format.NamedTextColor

interface Attribute {
    val baseValue: Double

    val value: Double
        get() {
            var computedValue = Math.max(0.0, baseValue + constantModifiers.sumOf { it.effect })

            val additiveMultiplier = 1.0 + additiveModifiers.sumOf { it.effect }
            computedValue *= (additiveMultiplier)

            val multiplier =
                if (multiplicativeModifiers.isEmpty()) {
                    1.0
                } else {
                    multiplicativeModifiers
                        .map { it.effect }
                        .reduce { acc, value -> acc * value }
                }
            computedValue *= multiplier

            return computedValue
        }

    val symbol: String

    val color: NamedTextColor

    /**
     * @see ConstantAttributeEffect
     * @see AdditiveAttributeEffect
     * @see MultiplicativeAttributeEffect
     */
    val constantModifiers: MutableList<ConstantAttributeEffect>
    val additiveModifiers: MutableList<AdditiveAttributeEffect>
    val multiplicativeModifiers: MutableList<MultiplicativeAttributeEffect>

    val attributeID: AttributeType

    val attributeCategory: AttributeCategory

    val visibleInSkyblockMenu: Boolean
        get() = true

    fun addEffect(effect: AttributeEffect) {
        when (effect) {
            is ConstantAttributeEffect -> constantModifiers.add(effect)
            is AdditiveAttributeEffect -> additiveModifiers.add(effect)
            is MultiplicativeAttributeEffect -> multiplicativeModifiers.add(effect)
        }
    }

    companion object {
        fun setupDefaultAttributes(): List<Attribute> = AttributeType.entries.map { it.createAttribute() }
    }
}
