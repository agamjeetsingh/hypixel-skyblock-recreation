@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.database

import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.attributes.*
import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.staticAttributes.Defense
import firstplugin.skyblock.attributes.staticAttributes.HealthRegen
import firstplugin.skyblock.attributes.staticAttributes.Speed
import firstplugin.skyblock.attributes.staticAttributes.Strength
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.logging.Level

/**
 * Service class for handling player data persistence using simple JSON files.
 * Each player's data is stored in a separate JSON file.
 */
class JsonPlayerDataService(
    private val plugin: JavaPlugin,
) {
    private val dataDir: File = File(plugin.dataFolder, "player-data")
    private val asyncExecutor = Executors.newSingleThreadExecutor()

    // JSON formatter with pretty printing and polymorphic serialization for attributes
    private val json: Json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true // Helps with backward compatibility if class structure changes
            serializersModule =
                SerializersModule {
                    // Register all attribute classes for polymorphic serialization
                    polymorphic(Attribute::class) {
                        subclass(Health::class)
                        subclass(Defense::class)
                        subclass(Speed::class)
                        subclass(Strength::class)
                        subclass(Intelligence::class)
                        subclass(HealthRegen::class)
                    }
                    // Register attribute effect classes
                    polymorphic(AttributeEffect::class) {
                        subclass(BaseAttributeEffect::class)
                        subclass(ConstantAttributeEffect::class)
                        subclass(AdditiveAttributeEffect::class)
                        subclass(MultiplicativeAttributeEffect::class)
                    }
                }
        }

    init {
        // Create player data directory if it doesn't exist
        dataDir.mkdirs()
        plugin.logger.info("Initialized JSON player data service at: ${dataDir.absolutePath}")
    }

    /**
     * Asynchronously saves a player's data to a JSON file.
     */
    fun savePlayerDataAsync(player: SkyblockPlayer): CompletableFuture<Void> =
        CompletableFuture
            .runAsync({
                savePlayerData(player)
            }, asyncExecutor)
            .exceptionally { e ->
                plugin.logger.log(Level.SEVERE, "Error saving player data: ${e.message}", e)
                null
            }

    /**
     * Saves a player's data to a JSON file.
     */
    fun savePlayerData(player: SkyblockPlayer) {
        try {
            // Create a serializable data representation
            val playerData: JsonPlayerDataService.PlayerData = PlayerData.fromSkyblockPlayer(player)

            // Save to file
            val playerFile: File = getPlayerFile(player.uniqueId)
            playerFile.writeText(json.encodeToString(playerData))

            plugin.logger.info("Saved data for player ${player.name}")
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Error saving player data: ${e.message}", e)
        }
    }

    /**
     * Loads a player's data and applies it to the provided SkyblockPlayer instance.
     */
    fun loadPlayerData(player: SkyblockPlayer) {
        try {
            val playerFile = getPlayerFile(player.uniqueId)

            if (playerFile.exists()) {
                val playerData = json.decodeFromString<PlayerData>(playerFile.readText())
                playerData.applyToPlayer(player)
                plugin.logger.info("Loaded data for player ${player.name}")
            } else {
                plugin.logger.info("No saved data found for player ${player.name}")
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Error loading player data: ${e.message}", e)
        }
    }

    /**
     * Asynchronously loads player data and then applies it on the main thread.
     */
    fun loadPlayerDataAsync(player: SkyblockPlayer): CompletableFuture<Void> =
        CompletableFuture
            .supplyAsync({
                // Load data from file (does not modify player yet)
                val playerFile = getPlayerFile(player.uniqueId)
                if (playerFile.exists()) {
                    json.decodeFromString<PlayerData>(playerFile.readText())
                } else {
                    null
                }
            }, asyncExecutor)
            .thenAccept { playerData ->
                // Apply loaded data to player on the main thread
                plugin.server.scheduler.runTask(
                    plugin,
                    Runnable {
                        if (playerData != null) {
                            playerData.applyToPlayer(player)
                            plugin.logger.info("Loaded data for player ${player.name}")
                        } else {
                            plugin.logger.info("No saved data found for player ${player.name}")
                        }
                    },
                )
            }.exceptionally { e ->
                plugin.logger.severe("Error loading player data: ${e.message}")
                null
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
     * Gets the file for a player's data.
     */
    private fun getPlayerFile(uuid: UUID): File = File(dataDir, "$uuid.json")

    /**
     * Closes the executor service.
     * Should be called when the plugin is disabled.
     */
    fun shutdown() {
        try {
            asyncExecutor.shutdown()
            plugin.logger.info("JSON player data service shutdown")
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Error shutting down player data service: ${e.message}", e)
        }
    }

    /**
     * Serializable data class for player information.
     */
    @Serializable
    data class PlayerData(
        val uuid: String,
        val name: String,
        val rank: String,
        val attributes: List<Attribute>,
    ) {
        companion object {
            fun fromSkyblockPlayer(player: SkyblockPlayer): PlayerData =
                PlayerData(
                    uuid = player.uniqueId.toString(),
                    name = player.name,
                    rank =
                        (player.serverPlayer as? firstplugin.ServerPlayer)?.rank?.name ?: firstplugin.Rank.DEFAULT.name,
                    attributes = player.attributes,
                )
        }

        /**
         * Applies this stored data to a SkyblockPlayer instance.
         */
        fun applyToPlayer(player: SkyblockPlayer) {
            // Set rank
            try {
                player.serverPlayer.rank = firstplugin.Rank.valueOf(rank)
            } catch (e: IllegalArgumentException) {
                // Use default rank if stored one is invalid
            }

            // For each stored attribute, find the matching attribute in the player and update it
            attributes.forEach { storedAttr ->
                try {
                    // Find the matching attribute in the player
                    val playerAttr = player.getAttribute(storedAttr.attributeID)

                    if (playerAttr != null) {
                        // Handle DynamicAttribute (update current value)
                        if (playerAttr is DynamicAttribute && storedAttr is DynamicAttribute) {
                            playerAttr.current = storedAttr.current
                        }

                        // Handle Health attribute specially (update absorption)
                        if (playerAttr is Health && storedAttr is Health) {
                            playerAttr.absorption = storedAttr.absorption
                        }

                        // Copy modifiers
                        playerAttr.constantModifiers.clear()
                        playerAttr.constantModifiers.addAll(storedAttr.constantModifiers)

                        playerAttr.additiveModifiers.clear()
                        playerAttr.additiveModifiers.addAll(storedAttr.additiveModifiers)

                        playerAttr.multiplicativeModifiers.clear()
                        playerAttr.multiplicativeModifiers.addAll(storedAttr.multiplicativeModifiers)
                    }
                } catch (e: Exception) {
                    // Skip invalid attributes
                }
            }
        }
    }
}
