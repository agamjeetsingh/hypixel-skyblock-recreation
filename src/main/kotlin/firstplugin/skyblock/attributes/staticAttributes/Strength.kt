@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Strength(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 0.0,
) : StaticAttribute() {
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT

    override val symbol: String = "❁"

    @Transient
    override val color: NamedTextColor = NamedTextColor.RED

    override val prettyPrintValueForMenu: String = String.format("%.2f", value)
}
