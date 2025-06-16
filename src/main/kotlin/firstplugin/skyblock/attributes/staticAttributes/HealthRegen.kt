@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import net.kyori.adventure.text.format.NamedTextColor

class HealthRegen(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 100.0,
) : StaticAttribute() {
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
    override val symbol: String = "❣"

    @Transient
    override val color: NamedTextColor = NamedTextColor.RED

    var regenIntervalInTicks: Int = 40

    override val prettyPrintValueForMenu: String = value.toInt().toString()
}
