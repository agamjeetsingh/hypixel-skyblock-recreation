@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.attributes

import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.staticAttributes.*

/**
 * Gets the full Health attribute object (not just the current value)
 */
val SkyblockPlayer.sbHealth: Health
    get() = (getAttribute(AttributeType.HEALTH) as? Health)!!

val SkyblockPlayer.defense: Defense
    get() = (getAttribute(AttributeType.DEFENSE) as? Defense)!!

val SkyblockPlayer.speed: Speed
    get() = (getAttribute(AttributeType.SPEED) as? Speed)!!

val SkyblockPlayer.strength: Strength
    get() = (getAttribute(AttributeType.STRENGTH) as? Strength)!!

val SkyblockPlayer.intelligence: Intelligence
    get() = (getAttribute(AttributeType.INTELLIGENCE) as? Intelligence)!!

val SkyblockPlayer.healthRegen: HealthRegen
    get() = (getAttribute(AttributeType.HEALTH_REGEN) as? HealthRegen)!!

val SkyblockPlayer.trueDefense: TrueDefense
    get() = (getAttribute(AttributeType.TRUE_DEFENSE) as? TrueDefense)!!

val SkyblockPlayer.critDamage: CritDamage
    get() = (getAttribute(AttributeType.CRIT_DAMAGE) as? CritDamage)!!

val SkyblockPlayer.damage: Damage
    get() = (getAttribute(AttributeType.DAMAGE) as? Damage)!!
