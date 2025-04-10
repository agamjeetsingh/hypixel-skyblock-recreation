@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.entity

import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.getAttribute
import firstplugin.skyblock.attributes.staticAttributes.*

/**
 * Gets the full Health attribute object (not just the current value)
 */
val CombatEntity.sbHealth: Health
    get() {
        val attr = attributes.getAttribute<Health>()
        return attr ?: throw IllegalStateException("Health attribute not found")
    }

val CombatEntity.defense: Defense
    get() {
        val attr = attributes.getAttribute<Defense>()
        return attr ?: throw IllegalStateException("Defense attribute not found")
    }

val CombatEntity.speed: Speed
    get() {
        val attr = attributes.getAttribute<Speed>()
        return attr ?: throw IllegalStateException("Speed attribute not found")
    }

val CombatEntity.strength: Strength
    get() {
        val attr = attributes.getAttribute<Strength>()
        return attr ?: throw IllegalStateException("Strength attribute not found")
    }

val CombatEntity.intelligence: Intelligence
    get() {
        val attr = attributes.getAttribute<Intelligence>()
        return attr ?: throw IllegalStateException("Intelligence attribute not found")
    }

val CombatEntity.healthRegen: HealthRegen
    get() {
        val attr = attributes.getAttribute<HealthRegen>()
        return attr ?: throw IllegalStateException("Health Regen attribute not found")
    }

val CombatEntity.trueDefense: TrueDefense
    get() {
        val attr = attributes.getAttribute<TrueDefense>()
        return attr ?: throw IllegalStateException("True Defense attribute not found")
    }

val CombatEntity.critDamage: CritDamage
    get() {
        val attr = attributes.getAttribute<CritDamage>()
        return attr ?: throw IllegalStateException("Crit Damage attribute not found")
    }

val CombatEntity.damage: Damage
    get() {
        val attr = attributes.getAttribute<Damage>()
        return attr ?: throw IllegalStateException("Damage attribute not found")
    }
