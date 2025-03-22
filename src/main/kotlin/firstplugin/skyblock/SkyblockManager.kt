package firstplugin.skyblock

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.database.PlayerDataService
import firstplugin.skyblock.menu.MenuManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * The SkyblockManager class stores all the properties of online players in
 * the `player` list, including the player's rank and attributes like health
 * and defense.
 */
class SkyblockManager(
    private val plugin: SkyblockPlugin,
) : Listener {
    private val playerDataService = PlayerDataService(plugin)
    private val menuManager = MenuManager(plugin)

    // Map of online SkyblockPlayer
    private val players = mutableMapOf<String, SkyblockPlayer>()

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
}
