@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Speed : StaticAttribute {
    override val attributeID: AttributeType = AttributeType.SPEED
    override val attributeCategory: AttributeCategory = AttributeCategory.MISC
    override val baseValue: Double = 100.0
    override val symbol: String = "✦"

    @Transient
    override val color: NamedTextColor = NamedTextColor.WHITE

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()
}
