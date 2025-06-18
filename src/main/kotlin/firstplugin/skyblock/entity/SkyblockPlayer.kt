@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.entity

import firstplugin.ServerPlayer
import firstplugin.skyblock.collection.Collection
import firstplugin.skyblock.collection.CollectionHolder
import firstplugin.skyblock.crafting.Crafter
import firstplugin.skyblock.crafting.CrafterDelegate
import firstplugin.skyblock.economy.BankAccount
import firstplugin.skyblock.economy.Coins
import firstplugin.skyblock.economy.MoneyHolder
import firstplugin.skyblock.location.Explorer
import firstplugin.skyblock.location.ExplorerDelegate
import firstplugin.skyblock.location.SkyblockLocation
import firstplugin.skyblock.location.Zone
import firstplugin.skyblock.minion.MinionHolder
import firstplugin.skyblock.minion.MinionHolderDelegate
import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.skill.SkillHolder
import firstplugin.skyblock.xp.SkyblockLevel
import org.bukkit.entity.Player

/**
 * SkyblockPlayer class
 *
 * NOTE: The health property of SkyblockPlayer refers to vanilla bukkit player health.
 * The skyblock health property is named `sbHealth`.
 */
class SkyblockPlayer(
    val serverPlayer: ServerPlayer,
) : CombatEntity(serverPlayer),
    Player by serverPlayer,
    ItemHolder,
    MoneyHolder,
    SkillHolder,
    MinionHolder by MinionHolderDelegate(),
    Explorer,
    Crafter by CrafterDelegate(),
    CollectionHolder {
    constructor(player: Player) : this(ServerPlayer(player))

    /**
     * Gets the player's bukkit player instance
     */
    val bukkitPlayer: Player
        get() = serverPlayer.bukkitPlayer

    /**
     * `SkyblockPlayer` inherits the heal method from two sources: `CombatEntity` and `Player`.
     * The heal method provided by `CombatEntity` is the intended one. The method also heals the `Player`.
     */
    override fun heal(sbHealAmt: Double) {
        super<CombatEntity>.heal(sbHealAmt)
    }

    /**
     * Default regeneration for a `CombatEntity` is `0.0`. We override it for custom player regeneration.
     */
    override val healthRegenValue: Double
        get() = (1.5 + (sbHealth.max / 100.0)) * (healthRegen.value / 100.0)

    override val purse: Coins = Coins()

    override val bankAccount: BankAccount = BankAccount(this)

    override val skills: List<Skill> = Skill.setupSkills(this)

    override val skyblockLevel = SkyblockLevel(this)

    override val collections: List<Collection> = Collection.setupCollections(this)

    private val explorerDelegate = ExplorerDelegate(this)
    override val locationsDiscovered: List<SkyblockLocation> = explorerDelegate.locationsDiscovered
    override val zonesDiscovered: List<Zone> = explorerDelegate.zonesDiscovered

    override fun discoverLocation(location: SkyblockLocation) = explorerDelegate.discoverLocation(location)

    override fun discoverZone(zone: Zone) = explorerDelegate.discoverZone(zone)
}
