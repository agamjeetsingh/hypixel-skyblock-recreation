@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Defense : StaticAttribute {
    override val attributeID: AttributeType = AttributeType.DEFENSE
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
    override val baseValue: Double = 0.0
    override val symbol: String = "❈"

    @Transient
    override val color: NamedTextColor = NamedTextColor.GREEN

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()
}
