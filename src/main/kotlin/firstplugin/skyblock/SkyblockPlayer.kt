@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock

import firstplugin.ServerPlayer
import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.*
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultAttributes
import firstplugin.skyblock.attributes.damage.DamageType
import firstplugin.skyblock.attributes.damage.DealtDamage
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

/**
 * SkyblockPlayer class
 *
 * NOTE: The health property of SkyblockPlayer refers to vanilla bukkit player health.
 * The skyblock health property is named `sbHealth`.
 */
class SkyblockPlayer(
    val serverPlayer: ServerPlayer,
) : Player by serverPlayer {
    constructor(player: Player) : this(ServerPlayer(player))

    /**
     * Map of all player attributes by ID
     */
    val attributes: List<Attribute> = setupDefaultAttributes()

    /**
     * Gets the player's bukkit player instance
     */
    val bukkitPlayer: Player
        get() = serverPlayer.bukkitPlayer

    /**
     * A getter for attributes of a player.
     * @param id The attribute type to be returned
     * @return The attribute object of type Attribute?. Returns null if `id` is invalid.
     * @see firstplugin.skyblock.attributes.sbHealth Type-safe attribute extension properties
     */
    fun getAttribute(id: AttributeType): Attribute? = attributes.find { it.attributeID == id }

    /**
     * A computed property that stores the effective health of a player.
     *
     * NOTE: Effective health is a derived property and hence should not / cannot be updated
     * to account for damage or healing. All updated should be made to `sbHealth`.
     * This helper function is useful for displaying the effective health to players in menus
     * or to check if a player will die, given some damage.
     */
    val effectiveHealth: Double
        get() = (sbHealth.value) * (1 + (defense.value) / 100.0)

    /**
     * A computed property that is true if and only if the player should be alive.
     * This property should only be used when dealing damage to a player.
     */
    val isAlive: Boolean
        get() = sbHealth.absorption + sbHealth.current > 0.0

    /**
     * A computed property that is true if and only if the player has positive absorption hearts.
     */
    val hasAbsorption: Boolean
        get() = sbHealth.absorption > 0.0

    /**
     * Heals the player.
     *
     * Note: We always do all health calculations in terms of the skyblock health.
     * Vanilla health is only used to sync the vanilla health with the skyblock health.
     *
     * @param sbHealAmt Heals both the `SkyblockPlayer` as well as the `bukkitPlayer` by this amount.
     */
    override fun heal(sbHealAmt: Double) {
        sbHealth.heal(sbHealAmt)
        bukkitPlayer.heal((sbHealth.current) * (bukkitPlayer.maxHealth) / (sbHealth.max) - bukkitPlayer.health)
    }

    private val healthRegenValue: Double
        get() = (1.5 + (sbHealth.max / 100.0)) * (healthRegen.value / 100.0)

    /**
     * A `BukkitRunnable` task for health regeneration of the player.
     */
    private val healthRegenRunnable: BukkitRunnable =
        object : BukkitRunnable() {
            override fun run() {
                heal(healthRegenValue)
            }
        }

    /**
     * Starts player regeneration. Cancels the previous regeneration process.
     *
     * **Note:** Changes in `healthRegen.value` are reflected immediately and do not require calling `startRegen()`.
     * However, a change in `healthRegen.regenIntervalInTicks` does require calling this function for the change to appear.
     */
    fun startRegen(plugin: SkyblockPlugin) {
        try {
            healthRegenRunnable.cancel()
        } catch (_: IllegalStateException) {
            // healthRegenRunnable wasn't running.
        }
        healthRegenRunnable.runTaskTimer(plugin, 0L, healthRegen.regenIntervalInTicks.toLong())
    }

    /**
     * Used when our `SkyblockPlayer` deals melee damage to another `SkyblockPlayer`.
     *
     * Damage dealt would be of type `DamageType.NORMAL`
     * @see DealtDamage
     */
    fun dealDamageMelee(
        critHit: Boolean,
        damageVictim: SkyblockPlayer,
    ): DealtDamage {
        val baseDamage: Double = damage.baseValue + damage.constantModifiers.sumOf { it.effect }
        val additiveMultiplier: Double = 1.0 + damage.additiveModifiers.sumOf { it.effect }
        val multiplicativeMultiplier: Double =
            if (damage.multiplicativeModifiers.isEmpty()) {
                1.0
            } else {
                damage.multiplicativeModifiers
                    .map { it.effect }
                    .reduce { acc, value -> acc * value }
            }
        val critDamage: Double = if (critHit) critDamage.value else 0.0
        val bonusModifiers: Double = 0.0 // Fine for now. Later TODO
        val damageAmt: Double =
            (
                (5.0 + baseDamage) * (1.0 + (strength.value / 100.0)) * additiveMultiplier * multiplicativeMultiplier +
                    bonusModifiers
            ) * (1.0 + (critDamage / 100.0))
        return DealtDamage(DamageType.NORMAL, critHit, damageAmt, this, damageVictim)
    }

    /**
     * Used when our `SkyblockPlayer` deals ability damage to another `SkyblockPlayer`.
     *
     * Damage dealt would be of type `DamageType.MAGIC`
     *
     * TODO, INCOMPLETE
     * @see DealtDamage
     */

    fun dealDamageAbility(damageVictim: SkyblockPlayer): DealtDamage {
        val baseAbilityDamage: Double = 0.0 // TODO
        val abilityScaling: Double = 1.0 // TODO
        val bonusModifiers: Double = 0.0 // Fine for now. Later TODO
        val additiveMultiplier: Double = 1.0 + damage.additiveModifiers.sumOf { it.effect } // Not sure
        val multiplicativeMultiplier: Double = // Not sure
            if (damage.multiplicativeModifiers.isEmpty()) {
                1.0
            } else {
                damage.multiplicativeModifiers
                    .map { it.effect }
                    .reduce { acc, value -> acc * value }
            }
        val damageAmt: Double =
            (
                baseAbilityDamage * (1 + (intelligence.value / 100.0) * abilityScaling) * additiveMultiplier *
                    multiplicativeMultiplier
            ) + bonusModifiers
        return DealtDamage(DamageType.MAGIC, false, damageAmt, this, damageVictim)
    }

    fun damageReceived(dealtDamage: DealtDamage) {
        val damageReceived: Double = dealtDamage.damageAmt
        val actualDamageReceived: Double

        // Only taking into account (for now), NORMAL, TRUE, FIRE
        // MAGIC (??), FALL, POISON, WITHER are TODO
        if (dealtDamage.damageType == DamageType.TRUE || dealtDamage.damageType == DamageType.FIRE) {
            actualDamageReceived = damageReceived * (1.0 - (trueDefense.value / (trueDefense.value + 100.0)))
        } else {
            actualDamageReceived = damageReceived * (1.0 - (defense.value / (defense.value + 100.0)))
        }
    }
}
