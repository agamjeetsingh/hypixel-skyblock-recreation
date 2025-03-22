package firstplugin.skyblock.attributes

import net.kyori.adventure.text.format.NamedTextColor

interface DynamicAttribute : Attribute {
    var current: Double

    val max: Double
        get() = value

    class Health : DynamicAttribute {
        private val _modifiers: MutableList<AttributeEffect> = mutableListOf()

        override val modifiers: List<AttributeEffect>
            get() = _modifiers.toList()

        override val attributeID: String = "health"

        override val baseValue: Double = 100.0

        override var current: Double = baseValue

        override val symbol: String = "❤"

        override val color: NamedTextColor = NamedTextColor.RED

        override fun addEffect(attributeEffect: AttributeEffect) {
            _modifiers.add(attributeEffect)
        }

        override fun removeEffect(attributeEffect: AttributeEffect): Boolean =
            _modifiers.removeAll {
                it.id == attributeEffect.id
            }
    }
}
