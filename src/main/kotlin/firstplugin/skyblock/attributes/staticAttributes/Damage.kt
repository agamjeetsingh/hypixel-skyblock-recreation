@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Damage : StaticAttribute {
    override val baseValue: Double = 5.0

    override val symbol: String = "❁"

    @Transient
    override val color: NamedTextColor = NamedTextColor.RED

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()

    override val attributeID: AttributeType = AttributeType.DAMAGE
    override val attributeCategory: AttributeCategory = AttributeCategory.OTHER

    override val visibleInSkyblockMenu: Boolean = false
}
