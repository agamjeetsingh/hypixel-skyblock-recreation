@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.dynamicAttributes

import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.AttributeCategory
import firstplugin.skyblock.attributes.DynamicAttribute
import net.kyori.adventure.text.format.NamedTextColor

class Health(
    override val attributeHolder: Attributable? = null,
    override val baseValue: Double = 100.0,
) : DynamicAttribute() {
    override fun syncWithBukkit() {
        val bukkitEntity = attributeHolder as? org.bukkit.entity.LivingEntity ?: return

        bukkitEntity.health = (current / max) *
            bukkitEntity
                .getAttribute(
                    org.bukkit.attribute.Attribute.MAX_HEALTH,
                )!!
                .value
    }

    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT

    /**
     * Represents current health. This does not take into account absorption.
     * Must be maintained between (inclusive) 0.0 and max.
     */
    override var current: Double = baseValue

    override val prettyPrintValueForMenu: String = value.toInt().toString()

    /**
     * Represents absorption health.
     */
    var absorption: Double = 0.0

    override val symbol: String = "❤"

    override val color: NamedTextColor = NamedTextColor.RED

    override fun decrease(decreaseAmt: Double) {
        if (decreaseAmt <= 0.0) {
            return
        }
        val oldAbsorption = absorption
        var dmgToDeal = decreaseAmt
        absorption = Math.max(0.0, absorption - dmgToDeal)
        dmgToDeal -= (oldAbsorption - absorption)
        if (dmgToDeal > 0.0) {
            current = Math.max(0.0, current - dmgToDeal)
        }
    }

    /**
     * A function to reduce absorption hearts if possible.
     * Used when absorption hearts are given for a set time amount of time (e.g. Golden Apple)
     *
     * TODO - Make it so that absorption hearts get updated live too
     */
    fun reduceAbsorption(reductionAmt: Double) {
        absorption = Math.max(0.0, absorption - reductionAmt)
    }
}
