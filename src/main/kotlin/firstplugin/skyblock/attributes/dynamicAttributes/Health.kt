@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes.dynamicAttributes

import firstplugin.skyblock.attributes.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.text.format.NamedTextColor

@Serializable
class Health : DynamicAttribute {
    override val attributeID: AttributeType = AttributeType.HEALTH
    override val attributeCategory: AttributeCategory = AttributeCategory.COMBAT
    override val baseValue: Double = 100.0

    /**
     * Represents current health. This does not take into account absorption.
     * Must be maintained between (inclusive) 0.0 and max.
     */
    override var current: Double = baseValue

    /**
     * Represents absorption health.
     */
    var absorption: Double = 0.0

    override val symbol: String = "❤"

    @Transient
    override val color: NamedTextColor = NamedTextColor.RED

    override val constantModifiers = mutableListOf<ConstantAttributeEffect>()
    override val additiveModifiers = mutableListOf<AdditiveAttributeEffect>()
    override val multiplicativeModifiers = mutableListOf<MultiplicativeAttributeEffect>()

    override fun addEffect(effect: AttributeEffect) {
        when (effect) {
            is ConstantAttributeEffect -> constantModifiers.add(effect)
            is AdditiveAttributeEffect -> additiveModifiers.add(effect)
            is MultiplicativeAttributeEffect -> multiplicativeModifiers.add(effect)
        }
    }

    fun damage(dmg: Double) {
        if (dmg <= 0.0) {
            return
        }
        val oldAbsorption = absorption
        var dmgToDeal = dmg
        absorption = Math.max(0.0, absorption - dmgToDeal)
        dmgToDeal -= (oldAbsorption - absorption)
        if (dmgToDeal > 0.0) {
            current = Math.max(0.0, current - dmgToDeal)
        }
    }

    fun heal(healAmt: Double) {
        current = Math.min(max, current + healAmt)
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
