@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.StaticAttribute
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Crit Damage indicates by how much % will the normal attack be increased
 * when a player lands a critical hit on the enemy (the odds of landing a critical hit
 * can be increased by Crit Chance)
 *
 * CritDamage is a percentage increase in damage. For example:
 * a 60% increase would be stored as 0.6.
 */
class CritDamage(
    @Transient
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 0.5,
) : StaticAttribute() {
    override val symbol: String = "☠"

    @Transient
    override val color: NamedTextColor = NamedTextColor.BLUE

    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
    override val prettyPrintValueForMenu: String = (String.format("%.4f", value).toDouble() * 100.0).toString() + "%"
}
