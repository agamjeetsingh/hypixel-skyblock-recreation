@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.staticAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Crit Damage indicates by how much % will the normal attack be increased
 * when a player lands a critical hit on the enemy (the odds of landing a critical hit
 * can be increased by Crit Chance)
 */
@Serializable
class CritDamage : StaticAttribute {
    /**
     * CritDamage is a percentage increase in damage. For example:
     * a 60% increase would be stored as 0.6.
     */
    override val baseValue: Double = 0.5

    override val symbol: String = "☠"

    @Transient
    override val color: NamedTextColor = NamedTextColor.BLUE

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()

    override val attributeID: AttributeType = AttributeType.CRIT_DAMAGE
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
}
