@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import net.kyori.adventure.text.format.NamedTextColor

class BonusAttackSpeed(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 0.0,
) : StaticAttribute() {
    override val symbol: String = "⚔"

    @Transient
    override val color: NamedTextColor = NamedTextColor.YELLOW

    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT

    override val cap: Double = 100.0

    override val visibleInSkyblockMenu: Boolean = false

    override val prettyPrintValueForMenu: String = value.toString()
}
