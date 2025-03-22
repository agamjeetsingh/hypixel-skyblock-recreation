package firstplugin.skyblock.attributes

import net.kyori.adventure.text.format.NamedTextColor

interface Attribute {
    val baseValue: Double

    val value: Double
        get() = baseValue + modifiers.sumOf { it.effect }

    val symbol: String

    val color: NamedTextColor

    val modifiers: List<AttributeEffect>

    val attributeID: String

    fun addEffect(attributeEffect: AttributeEffect)

    fun removeEffect(attributeEffect: AttributeEffect): Boolean

    companion object {
        fun setupDefaultAttributes(): List<Attribute> {
            val attributes = mutableListOf<Attribute>()

            attributes.add(StaticAttribute.Defense())
            attributes.add(DynamicAttribute.Health())

            return attributes.toList()
        }
    }
}
