package firstplugin.skyblock.database

import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.attributes.DynamicAttribute
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

/**
 * Service class for handling player data persistence operations.
 * Uses async operations to prevent blocking the main thread.
 */
class PlayerDataService(
    private val plugin: JavaPlugin,
) {
    private val dbManager = DatabaseManager(plugin)
    private val asyncExecutor = Executors.newSingleThreadExecutor()

    /**
     * Asynchronously saves a player's data to the database.
     */
    fun savePlayerDataAsync(player: SkyblockPlayer): CompletableFuture<Void> =
        CompletableFuture
            .runAsync({
                // Save basic player info
                dbManager.savePlayerBasic(player)

                // Save all attributes
                dbManager.savePlayerAttributes(player)

                plugin.logger.info("Saved data for player ${player.name}")
            }, asyncExecutor)
            .exceptionally { e ->
                plugin.logger.severe("Error saving player data: ${e.message}")
                null
            }

    /**
     * Loads a player's data and applies it to the provided SkyblockPlayer instance.
     */
    fun loadPlayerData(player: SkyblockPlayer) {
        val uuid = player.uniqueId

        // Load player's rank
        val rank = dbManager.loadPlayerRank(uuid)

        player.serverPlayer.rank = rank

        // Load player's attributes
        val attributeData = dbManager.loadPlayerAttributes(uuid)

        // Apply loaded attribute data to player attributes
        attributeData.forEach { (attributeId, data) ->
            // Get the attribute instance from player
            val attribute = player.getAttribute(attributeId) ?: return@forEach // TODO

            // For dynamic attributes, set current value
            if (attribute is DynamicAttribute) {
                attribute.current = data.currentValue
            }
        }

        plugin.logger.info("Loaded data for player ${player.name}")
    }

    /**
     * Asynchronously loads player data and then applies it on the main thread.
     */
    fun loadPlayerDataAsync(player: SkyblockPlayer): CompletableFuture<Void> {
        return CompletableFuture
            .supplyAsync({
                // Load data from database (does not modify player yet)
                val uuid = player.uniqueId
                val rank = dbManager.loadPlayerRank(uuid)
                val attributeData = dbManager.loadPlayerAttributes(uuid)
                Pair(rank, attributeData)
            }, asyncExecutor)
            .thenAccept { (rank, attributeData) ->
                // Apply loaded data to player on the main thread
                plugin.server.scheduler.runTask(
                    plugin,
                    Runnable {
                        player.serverPlayer.rank = rank

                        attributeData.forEach { (attributeId, data) ->
                            val attribute = player.getAttribute(attributeId) ?: return@forEach

                            // For dynamic attributes, only update current value
                            if (attribute is DynamicAttribute) {
                                attribute.current = data.currentValue
                            }
                        }

                        plugin.logger.info("Loaded data for player ${player.name}")
                    },
                )
            }.exceptionally { e ->
                plugin.logger.severe("Error loading player data: ${e.message}")
                null
            }
    }

    /**
     * Saves data for all online players.
     */
    fun saveAllPlayers(players: Collection<SkyblockPlayer>) {
        players.forEach { player ->
            savePlayerDataAsync(player)
        }
    }

    /**
     * Closes the database connection properly.
     * Should be called when the plugin is disabled.
     */
    fun shutdown() {
        try {
            asyncExecutor.shutdown()
            dbManager.closeConnection()
        } catch (e: Exception) {
            plugin.logger.severe("Error shutting down PlayerDataService: ${e.message}")
        }
    }
}
