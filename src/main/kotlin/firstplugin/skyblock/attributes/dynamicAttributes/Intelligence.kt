@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.dynamicAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Intelligence : DynamicAttribute {
    override val attributeID: AttributeType = AttributeType.INTELLIGENCE
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
    override val baseValue: Double = 0.0
    override var current: Double = baseValue
    override val symbol: String = "✎"

    @Transient
    override val color: NamedTextColor = NamedTextColor.AQUA

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()

    val baseMana: Int = 100

    val currentMana: Int
        get() = baseMana + current.toInt()

    val maxMana: Int
        get() = baseMana + max.toInt()

    override fun addEffect(effect: AttributeEffect) {
        when (effect) {
            is ConstantAttributeEffect -> constantModifiers.add(effect)
            is AdditiveAttributeEffect -> additiveModifiers.add(effect)
            is MultiplicativeAttributeEffect -> multiplicativeModifiers.add(effect)
        }
    }
}
