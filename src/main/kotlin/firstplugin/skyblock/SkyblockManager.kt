package firstplugin.skyblock

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.sbHealth
import firstplugin.skyblock.database.JsonPlayerDataService
import firstplugin.skyblock.menu.MenuManager
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

/**
 * The SkyblockManager class stores all the properties of online players in
 * the `player` list, including the player's rank and attributes like health
 * and defense.
 */
class SkyblockManager(
    private val plugin: SkyblockPlugin,
) : Listener {
    private val playerDataService = JsonPlayerDataService(plugin)
    private val menuManager = MenuManager(plugin)

    // Map of online SkyblockPlayer
    private val players = mutableMapOf<String, SkyblockPlayer>()

    // Get all online SkyblockPlayers
    val onlinePlayers: Collection<SkyblockPlayer>
        get() = players.values

    fun initialize() {
        // Register this manager as an event listener
        plugin.server.pluginManager.registerEvents(this, plugin)

        // Initialize menu system
        menuManager.initialize()

        // Register shutdown hook for database cleanup
        plugin.registerShutdownHook {
            saveAllPlayers()
            playerDataService.shutdown()
        }
    }

    /**
     * Gets a SkyblockPlayer instance for a player name
     */
    fun getPlayer(playerName: String): SkyblockPlayer? = players[playerName]

    /**
     * Handles player join event
     */
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = SkyblockPlayer(event.player)
        players[player.name] = player

        // Load player data from database
        playerDataService.loadPlayerDataAsync(player)

        player.startRegen(plugin)
    }

    /**
     * Handles player quit event
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = players.remove(event.player.name) ?: return

        // Save player data to database
        playerDataService.savePlayerDataAsync(player)
    }

    /**
     * Saves all online players' data
     */
    fun saveAllPlayers() {
        playerDataService.saveAllPlayers(players.values)
    }

    @EventHandler
    fun onPlayerEatGoldenApple(event: PlayerItemConsumeEvent) {
        val player = players[event.player.name]!!
        if (event.item == ItemStack(Material.GOLDEN_APPLE)) {
            player.sbHealth.absorption += 10.0
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val entity: Entity = event.entity
        if (entity is Player) {
            val player = players[entity.name]!!
            // TODO - It would be more natural to have player.damage()
            player.sbHealth.damage(event.damage * 5.0)
            player.bukkitPlayer.absorptionAmount = player.sbHealth.absorption / 5.0
            player.bukkitPlayer.health =
                (player.sbHealth.current) * (player.bukkitPlayer.maxHealth) / (player.sbHealth.max)
        }
    }
}
