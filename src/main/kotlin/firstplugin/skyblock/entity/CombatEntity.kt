@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.entity

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.Attributable
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.Attribute.Companion.setupDefaultPlayerAttributes
import firstplugin.skyblock.attributes.damage.DamageType
import firstplugin.skyblock.attributes.damage.DealtDamage
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

open class CombatEntity(
    val entity: LivingEntity,
) : SkyblockEntity(entity),
    Attributable,
    ItemHolder {
    override val attributes: List<Attribute> by lazy {
        setupDefaultPlayerAttributes(this)
    }

    @Transient
    var attributesInitialized: Boolean = false

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
    open fun heal(sbHealAmt: Double) {
        sbHealth.increase(sbHealAmt)
        entity.heal(
            (sbHealth.current) * (entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH)!!.value) /
                (sbHealth.max) -
                entity.health,
        )
    }

    protected open val healthRegenValue: Double
        get() = 0.0

    /**
     * A `BukkitRunnable` task for health regeneration of the player.
     */
    @Transient
    private val healthRegenRunnable: BukkitRunnable =
        object : BukkitRunnable() {
            override fun run() {
                heal(healthRegenValue)
            }
        }

    /**
     * Starts player regeneration. Cancels the previous regeneration process.
     * Does not start a new regeneration process if `healthRegenValue` is `0.0`
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
        if (healthRegenValue == 0.0) {
            return
        }
        healthRegenRunnable.runTaskTimer(plugin, 0L, healthRegen.regenIntervalInTicks.toLong())
    }

    /**
     * Used when our `SkyblockPlayer` deals melee damage to another `SkyblockPlayer`.
     *
     * Damage dealt would be of type `DamageType.NORMAL.MELEE`
     * @see DealtDamage
     */
    fun dealDamageMelee(
        critHit: Boolean,
        damageVictim: CombatEntity,
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
                (5.0 + baseDamage) * (1.0 + (strength.value / 100.0)) * additiveMultiplier *
                    multiplicativeMultiplier +
                    bonusModifiers
            ) * (1.0 + (critDamage / 100.0))
        return DealtDamage(DamageType.NORMAL.MELEE, critHit, damageAmt, this, damageVictim)
    }

    /**
     * Used when our `SkyblockPlayer` deals ability damage to a `CombatEntity`.
     *
     * Damage dealt would be of type `DamageType.MAGIC`
     *
     * TODO, INCOMPLETE
     * @see DealtDamage
     */

    fun dealDamageAbility(damageVictim: CombatEntity): DealtDamage {
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

        // Only taking into account (for now), NORMAL subtypes, TRUE, FIRE
        // MAGIC (??), FALL, POISON, WITHER are TODO
        if (dealtDamage.damageType == DamageType.TRUE || dealtDamage.damageType == DamageType.FIRE) {
            actualDamageReceived = damageReceived * (1.0 - (trueDefense.value / (trueDefense.value + 100.0)))
        } else {
            actualDamageReceived = damageReceived * (1.0 - (defense.value / (defense.value + 100.0)))
        }

        // TODO - Actually deal the actualDamageReceived
    }

    var notEnoughMana: Boolean = false
}
