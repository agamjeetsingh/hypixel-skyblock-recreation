package firstplugin.skyblock.minion

import firstplugin.skyblock.skill.Skill
import org.bukkit.Material

abstract class Minion(
    material: Material,
    val holder: MinionHolder,
) {
    /**
     * The in-game range of the minion. It is 5 by default but can be upgraded to 6 using
     * the Minion Expander `MinionUpgrade`.
     */
    var minionRange: Int = DEFAULT_MINION_RANGE
        protected set

    /**
     * Represents the current tier of the minion. The starting tier is `1` and it goes
     * up to [maxTier]. Must only be changed through the [upgrade] method.
     * Must be maintained between `1` and [maxTier] (inclusive).
     */
    var tier: Int = 1
        protected set

    /**
     * The maximum tier that a minion can reach. The maximum tier for most minions
     * is either `11` or `12`. Must be a positive integer.
     */
    abstract val maxTier: Int

    /**
     * Always call this method to increase the tier of the minion. This method will:
     * 1. Not allow you to upgrade an already maxed out minion.
     * 2. Create a new [MinionItem] using [newMinionItem] as a [MinionItem] is associated with a particular tier.
     */
    fun upgrade(): Boolean {
        if (tier > maxTier) return false
        tier++
        newMinionItem()
        return true
    }

    /**
     * We need to create a new [MinionItem] whenever we update the tier,
     * as a [MinionItem] has a fixed tier associated with it. This method must
     * be called **after** the tier is incremented.
     */
    protected abstract fun newMinionItem()

    /**
     * Every [Minion] has a particular [Skill] associated with it.
     * This must be the [Skill] of the [holder]. The [minionType] enum comes with
     * its own method that returns the right skill instance. Example:
     * ```
     * class CobblestoneMinion<T>(
     *     private val owner: T,
     * ) : Minion(Material.COBBLESTONE, owner) where T : ItemHolder, T : SkillHolder, T : MinionHolder {
     *     ...
     *     override val skill: Skill = minionType.getSkills(owner)
     * }
     * ```
     */
    abstract val skill: Skill

    /**
     * The [MinionType] of the minion.
     */
    abstract val minionType: MinionType

    /**
     * The primary material that the minion generates. For example, this would be gravel
     * for a Gravel Minion (and not flint).
     */
    abstract val generatesMaterial: Material

    /**
     * Stores the time between Minion actions in seconds for every tier.
     */
    protected abstract val timeBetweenActionsMapInSeconds: Map<Int, Int>

    /**
     * Stores the time between Minion actions in ticks for every tier.
     */
    private val timeBetweenActionsMap: Map<Int, Long>
        get() =
            timeBetweenActionsMapInSeconds.mapValues { (_, seconds) ->
                seconds.toLong() * 20
            }

    /**
     * The time between Minion actions for the current tier.
     * @throws NullPointerException if [tier] is `<= 0` or `> maxTier`
     */
    val timeBetweenActions: Long
        get() = timeBetweenActionsMap[tier]!!

    /**
     * The maximum amount of resources that the minion can keep in its storage.
     */
    val maxStorage: Int
        get() = maxStorageInStacks * generatesMaterial.maxStackSize

    /**
     * The maximum amount of stacks of resources that the minion can keep in its storage.
     *
     * @throws NullPointerException if [tier] is `<= 0` or `> maxTier`
     */
    private val maxStorageInStacks: Int
        get() = STORAGE_LEVEL_MAP[tier]!!

    /**
     * Stores the number of resources generated since the minion was created.
     */
    var resourcesGenerated: Int = 0
        protected set

    /**
     * Stores the current [MinionFuel]. It is `null` when there is no minion fuel.
     */
    var minionFuel: MinionFuel? = null

    // TODO - Might need custom setters so we can actually make the minion upgrades have their effects

    /**
     * The first slot for the [MinionUpgrade]. It is `null` when the slot is empty.
     */
    var minionUpgradeSlotOne: MinionUpgrade? = null

    /**
     * The second slot for the [MinionUpgrade]. It is `null` when the slot is empty.
     */
    var minionUpgradeSlotTwo: MinionUpgrade? = null

    /**
     * The item associated with this minion. It is updated every time the tier of the
     * minion is upgraded since `MinionItem` instances are associated with a particular tier.
     */
    abstract var minionItem: MinionItem
        protected set

    companion object {
        const val DEFAULT_MINION_RANGE = 5

        /**
         * Stores the storage capacity of the minion as a map.
         * Key denotes tier and the Value denotes the number of stacks in the storage.
         */
        val STORAGE_LEVEL_MAP: Map<Int, Int> =
            mapOf(
                1 to 1,
                2 to 3,
                3 to 3,
                4 to 6,
                5 to 6,
                6 to 9,
                7 to 9,
                8 to 12,
                9 to 12,
                10 to 15,
                11 to 15,
                12 to 15,
            )
    }
}
