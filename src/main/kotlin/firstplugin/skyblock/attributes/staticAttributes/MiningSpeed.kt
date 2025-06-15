package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import net.kyori.adventure.text.format.NamedTextColor

class MiningSpeed(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 0.0,
) : StaticAttribute() {
    override val symbol: String = "⸕"

    override val color: NamedTextColor = NamedTextColor.GOLD

    override val attributeCategory: AttributeCategory = AttributeCategory.GATHERING

    override val prettyPrintValueForMenu: String = value.toInt().toString()
}
