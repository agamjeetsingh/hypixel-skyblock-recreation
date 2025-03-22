package firstplugin.skyblock.attributes

import net.kyori.adventure.text.format.NamedTextColor

interface StaticAttribute : Attribute {
    class Defense : StaticAttribute {
        private val _modifiers: MutableList<AttributeEffect> = mutableListOf()

        override val modifiers: List<AttributeEffect>
            get() = _modifiers.toList()

        override val attributeID: String = "defense"

        override val baseValue: Double = 0.0

        override val symbol: String = "❈"

        override val color: NamedTextColor = NamedTextColor.GREEN

        override fun addEffect(attributeEffect: AttributeEffect) {
            _modifiers.add(attributeEffect)
        }

        override fun removeEffect(attributeEffect: AttributeEffect): Boolean =
            _modifiers.removeAll {
                it.id == attributeEffect.id
            }
    }

    class Strength : StaticAttribute {
        override val baseValue: Double = 0.0

        override val symbol: String = "❁"

        override val color: NamedTextColor = NamedTextColor.RED

        private val _modifiers: MutableList<AttributeEffect> = mutableListOf()
        override val modifiers: List<AttributeEffect>
            get() = _modifiers.toList()

        override val attributeID: String = "strength"

        override fun addEffect(attributeEffect: AttributeEffect) {
            _modifiers.add(attributeEffect)
        }

        override fun removeEffect(attributeEffect: AttributeEffect): Boolean =
            _modifiers.removeAll {
                it.id == attributeEffect.id
            }
    }
}
