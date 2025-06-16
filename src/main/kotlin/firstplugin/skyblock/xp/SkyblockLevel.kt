package firstplugin.skyblock.xp

import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.Strength
import firstplugin.skyblock.entity.SkyblockPlayer

class SkyblockLevel(
    val holder: SkyblockPlayer,
) {
    var xp: Int = 0
        private set
    val progress: Int
        get() = xp % 100
    val level: Int
        get() = xp / 100

    fun gainXP(amount: Int): Int {
        if (amount <= 0) return xp
        val oldLevel = level
        xp += amount
        if (level > oldLevel) {
            // +5 Health for every level
            holder.attributes.getAttribute<Health>()!!.addEffect(
                ConstantAttributeEffect(5.0),
            )
            // +1 Strength for every 5 levels
            if (level % 5 == 0) {
                holder.attributes.getAttribute<Strength>()!!.addEffect(
                    ConstantAttributeEffect(1.0),
                )
            }
        }
        return xp
    }
}
