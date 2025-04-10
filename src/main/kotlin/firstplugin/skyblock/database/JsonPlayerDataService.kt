@file:Suppress("ktlint:standard:no-wildcard-imports")

package firstplugin.skyblock.database

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import firstplugin.skyblock.attributes.*
import firstplugin.skyblock.attributes.dynamicAttributes.Health
import firstplugin.skyblock.attributes.dynamicAttributes.Intelligence
import firstplugin.skyblock.attributes.staticAttributes.*
import firstplugin.skyblock.entity.SkyblockPlayer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Type
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

    // Type adapter for handling attribute polymorphism
    inner class AttributeTypeAdapter : JsonSerializer<Attribute>, JsonDeserializer<Attribute> {
        override fun serialize(src: Attribute, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val jsonObject = JsonObject()
            // Add type information
            jsonObject.addProperty("type", src.javaClass.name)
            
            // Serialize the rest of the object excluding attributeHolder field
            val objectJson = context.serialize(src).asJsonObject
            objectJson.entrySet().forEach { (key, value) ->
                if (key != "type" && key != "attributeHolder") {
                    jsonObject.add(key, value)
                }
            }
            
            return jsonObject
        }
        
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Attribute {
            val jsonObject = json.asJsonObject
            val type = jsonObject.get("type").asString
            
            try {
                val attributeClass = Class.forName(type)
                return context.deserialize(json, attributeClass)
            } catch (e: ClassNotFoundException) {
                plugin.logger.warning("Unknown attribute type: $type")
                throw JsonParseException("Unknown attribute type: $type", e)
            }
        }
    }
    
    // Type adapter for AttributeEffect
    inner class AttributeEffectTypeAdapter : JsonSerializer<AttributeEffect>, JsonDeserializer<AttributeEffect> {
        override fun serialize(src: AttributeEffect, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", src.javaClass.name)
            
            val objectJson = context.serialize(src).asJsonObject
            objectJson.entrySet().forEach { (key, value) ->
                if (key != "type") {
                    jsonObject.add(key, value)
                }
            }
            
            return jsonObject
        }
        
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AttributeEffect {
            val jsonObject = json.asJsonObject
            val type = jsonObject.get("type").asString
            
            try {
                val effectClass = Class.forName(type)
                return context.deserialize(json, effectClass)
            } catch (e: ClassNotFoundException) {
                plugin.logger.warning("Unknown attribute effect type: $type")
                throw JsonParseException("Unknown attribute effect type: $type", e)
            }
        }
    }
    
    // Create Gson instance with pretty printing and type adapters
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(Attribute::class.java, AttributeTypeAdapter())
        .registerTypeAdapter(AttributeEffect::class.java, AttributeEffectTypeAdapter())
        .create()

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
            playerFile.writeText(gson.toJson(playerData))

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
                plugin.logger.info("Found player file: ${playerFile.absolutePath}")
                val fileContent = playerFile.readText()
                plugin.logger.info("File content length: ${fileContent.length}")

                try {
                    val playerData = gson.fromJson(fileContent, PlayerData::class.java)
                    plugin.logger.info("Successfully decoded player data with ${playerData.attributes.size} attributes")
                    playerData.applyToPlayer(player)
                    plugin.logger.info("Loaded data for player ${player.name}")
                } catch (e: Exception) {
                    plugin.logger.severe("Error decoding player data: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                plugin.logger.info("No saved data found for player ${player.name}")
            }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Error loading player data: ${e.message}", e)
            e.printStackTrace()
        }
    }

    /**
     * Asynchronously loads player data and then applies it on the main thread.
     */
    fun loadPlayerDataAsync(player: SkyblockPlayer): CompletableFuture<Void> =
        CompletableFuture
            .supplyAsync({
                // Load data from file (does not modify player yet)
                println("Async: Loading data for player ${player.name}")
                val playerFile = getPlayerFile(player.uniqueId)
                println("Async: Player file path: ${playerFile.absolutePath}")

                if (playerFile.exists()) {
                    println("Async: Player file exists, reading content")
                    val content = playerFile.readText()
                    println("Async: Read ${content.length} characters from file")
                    try {
                        val data = gson.fromJson(content, PlayerData::class.java)
                        println("Async: Successfully decoded PlayerData with ${data.attributes.size} attributes")
                        data
                    } catch (e: Exception) {
                        println("Async: Error decoding player data: ${e.message}")
                        e.printStackTrace()
                        null
                    }
                } else {
                    println("Async: No player file found")
                    null
                }
            }, asyncExecutor)
            .thenAccept { playerData ->
                // Apply loaded data to player on the main thread
                println("Main thread: Applying player data for ${player.name}")
                plugin.server.scheduler.runTask(
                    plugin,
                    Runnable {
                        if (playerData != null) {
                            try {
                                println("Main thread: Player data available, applying to player")
                                playerData.applyToPlayer(player)
                                println("Main thread: Successfully loaded data for player ${player.name}")
                            } catch (e: Exception) {
                                println("Main thread: Error applying player data: ${e.message}")
                                e.printStackTrace()
                            }
                        } else {
                            println("Main thread: No saved data found for player ${player.name}")
                        }
                    },
                )
            }.exceptionally { e ->
                println("Exceptionally handler: Error loading player data: ${e.message}")
                e.printStackTrace()
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
     * Data class for player information.
     */
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
            println("applyToPlayer: Starting for player ${player.name}")

            // Set rank
            try {
                println("applyToPlayer: Setting rank to $rank")
                player.serverPlayer.rank = firstplugin.Rank.valueOf(rank)
            } catch (e: IllegalArgumentException) {
                println("applyToPlayer: Invalid rank: $rank, using default")
                // Use default rank if stored one is invalid
            }

            println("applyToPlayer: Player has ${player.attributes.size} attributes")
            println("applyToPlayer: Stored data has ${attributes.size} attributes")

            // For safety, ensure player attributes are initialized
            if (player.attributes.isEmpty()) {
                println("applyToPlayer: WARNING - Player attributes list is empty!")
                return
            }

            // For each stored attribute, find the matching attribute in the player and update it
            attributes.forEach { storedAttr ->
                try {
                    println("applyToPlayer: Processing stored attribute ${storedAttr::class.simpleName}")

                    // Find the matching attribute in the player by its class type
                    val playerAttr = player.attributes.find { it::class == storedAttr::class }

                    if (playerAttr != null) {
                        println("applyToPlayer: Found matching player attribute")

                        // Handle DynamicAttribute (update current value)
                        if (playerAttr is DynamicAttribute && storedAttr is DynamicAttribute) {
                            println("applyToPlayer: Updating DynamicAttribute current value")
                            playerAttr.current = storedAttr.current
                        }

                        // Handle Health attribute specially (update absorption)
                        if (playerAttr is Health && storedAttr is Health) {
                            println("applyToPlayer: Updating Health absorption")
                            playerAttr.absorption = storedAttr.absorption
                        }

                        // Copy modifiers (with defensive null checks)
                        println("applyToPlayer: Clearing and updating modifiers")

                        try {
                            playerAttr.constantModifiers.clear()
                            if (storedAttr.constantModifiers != null) {
                                playerAttr.constantModifiers.addAll(storedAttr.constantModifiers)
                            }
                        } catch (e: Exception) {
                            println("applyToPlayer: Error updating constant modifiers: ${e.message}")
                        }

                        try {
                            playerAttr.additiveModifiers.clear()
                            if (storedAttr.additiveModifiers != null) {
                                playerAttr.additiveModifiers.addAll(storedAttr.additiveModifiers)
                            }
                        } catch (e: Exception) {
                            println("applyToPlayer: Error updating additive modifiers: ${e.message}")
                        }

                        try {
                            playerAttr.multiplicativeModifiers.clear()
                            if (storedAttr.multiplicativeModifiers != null) {
                                playerAttr.multiplicativeModifiers.addAll(storedAttr.multiplicativeModifiers)
                            }
                        } catch (e: Exception) {
                            println("applyToPlayer: Error updating multiplicative modifiers: ${e.message}")
                        }
                    } else {
                        println("applyToPlayer: No matching player attribute found for ${storedAttr::class.simpleName}")
                    }
                } catch (e: Exception) {
                    // Skip invalid attributes and log the error
                    println("applyToPlayer: Error updating attribute ${storedAttr::class.simpleName}: ${e.message}")
                    e.printStackTrace()
                }
            }
            println("applyToPlayer: Completed for player ${player.name}")
        }
    }
}
