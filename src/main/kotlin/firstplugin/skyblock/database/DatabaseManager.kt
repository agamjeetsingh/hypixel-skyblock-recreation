package firstplugin.skyblock.database

import firstplugin.Rank
import firstplugin.ServerPlayer
import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.DynamicAttribute
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID
import java.util.logging.Level

/**
 * Manages SQLite database connections and operations for the Skyblock plugin.
 */
class DatabaseManager(
    private val plugin: JavaPlugin,
) {
    private var connection: Connection? = null
    private val dbFile: File = File(plugin.dataFolder, "skyblock.db")

    init {
        plugin.dataFolder.mkdirs()
        initializeDatabase()
    }

    /**
     * Initializes the SQLite database and creates necessary tables if they don't exist.
     */
    private fun initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")

            // Create tables if they don't exist
            createTables()
            plugin.logger.info("Successfully connected to the database.")
        } catch (e: SQLException) {
            plugin.logger.log(Level.SEVERE, "Failed to initialize database: ${e.message}", e)
        } catch (e: ClassNotFoundException) {
            plugin.logger.log(Level.SEVERE, "SQLite JDBC driver not found: ${e.message}", e)
        }
    }

    /**
     * Creates the necessary database tables if they don't exist.
     */
    private fun createTables() {
        connection?.createStatement()?.use { statement ->
            // Players table
            statement.execute(
                """
                CREATE TABLE IF NOT EXISTS players (
                    uuid TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    rank TEXT NOT NULL
                )
            """,
            )

            // Attributes table
            statement.execute( // REMOVE base_value TODO()
                """
                CREATE TABLE IF NOT EXISTS player_attributes (
                    uuid TEXT NOT NULL,
                    attribute_id TEXT NOT NULL,
                    current_value REAL NOT NULL,
                    max_value REAL NOT NULL,
                    PRIMARY KEY (uuid, attribute_id),
                    FOREIGN KEY (uuid) REFERENCES players(uuid) ON DELETE CASCADE
                )
            """,
            )

            plugin.logger.info("Database tables created or already exist.")
        }
    }

    /**
     * Closes the database connection.
     */
    fun closeConnection() {
        try {
            connection?.close()
            plugin.logger.info("Database connection closed.")
        } catch (e: SQLException) {
            plugin.logger.log(Level.WARNING, "Error closing database connection: ${e.message}", e)
        }
    }

    /**
     * Saves a player's basic information and rank to the database.
     */
    fun savePlayerBasic(player: SkyblockPlayer) {
        try {
            connection
                ?.prepareStatement(
                    """
                INSERT OR REPLACE INTO players (uuid, name, rank) 
                VALUES (?, ?, ?)
            """,
                )?.use { statement ->
                    statement.setString(1, player.uniqueId.toString())
                    statement.setString(2, player.name)
                    statement.setString(3, (player.serverPlayer as? ServerPlayer)?.rank?.name ?: Rank.DEFAULT.name)
                    statement.executeUpdate()
                }
        } catch (e: SQLException) {
            plugin.logger.log(Level.WARNING, "Error saving player ${player.name}: ${e.message}", e)
        }
    }

    /**
     * Saves a player's attribute to the database.
     */
    fun savePlayerAttribute(
        playerUUID: UUID,
        attribute: Attribute,
    ) {
        try {
            connection
                ?.prepareStatement(
                    """
                INSERT OR REPLACE INTO player_attributes (uuid, attribute_id, current_value, max_value)
                VALUES (?, ?, ?, ?)
            """,
                )?.use { statement ->
                    statement.setString(1, playerUUID.toString())
                    statement.setString(2, attribute.attributeID)

                    // Handle current and max values differently for dynamic attributes
                    val currentValue = if (attribute is DynamicAttribute) attribute.current else attribute.value
                    val maxValue = if (attribute is DynamicAttribute) attribute.max else attribute.value

                    statement.setDouble(3, currentValue)
                    statement.setDouble(4, maxValue)
                    statement.executeUpdate()
                }
        } catch (e: SQLException) {
            plugin.logger.log(
                Level.WARNING,
                "Error saving attribute ${attribute.attributeID} for player $playerUUID: ${e.message}",
                e,
            )
        }
    }

    /**
     * Saves all attributes for a Skyblock player.
     */
    fun savePlayerAttributes(player: SkyblockPlayer) {
        player.attributes.forEach { attribute ->
            savePlayerAttribute(player.uniqueId, attribute)
        }
    }

    /**
     * Loads a player's rank from the database.
     * Returns the default rank if the player is not found.
     */
    fun loadPlayerRank(playerUUID: UUID): Rank {
        try {
            connection?.prepareStatement("SELECT rank FROM players WHERE uuid = ?")?.use { statement ->
                statement.setString(1, playerUUID.toString())
                val result = statement.executeQuery()

                if (result.next()) {
                    val rankStr = result.getString("rank")
                    return try {
                        Rank.valueOf(rankStr)
                    } catch (e: IllegalArgumentException) {
                        plugin.logger.warning("Invalid rank '$rankStr' stored for player $playerUUID, using default.")
                        Rank.DEFAULT
                    }
                }
            }
        } catch (e: SQLException) {
            plugin.logger.log(Level.WARNING, "Error loading rank for player $playerUUID: ${e.message}", e)
        }

        return Rank.DEFAULT
    }

    /**
     * Loads a player's attributes from the database.
     * Returns a map of attribute ID to attribute values.
     */
    fun loadPlayerAttributes(playerUUID: UUID): Map<String, AttributeData> {
        val attributes = mutableMapOf<String, AttributeData>()

        try {
            connection?.prepareStatement("SELECT * FROM player_attributes WHERE uuid = ?")?.use { statement ->
                statement.setString(1, playerUUID.toString())
                val result = statement.executeQuery()

                while (result.next()) {
                    val attributeId = result.getString("attribute_id")
                    val currentValue = result.getDouble("current_value")
                    val maxValue = result.getDouble("max_value")

                    attributes[attributeId] = AttributeData(currentValue, maxValue)
                }
            }
        } catch (e: SQLException) {
            plugin.logger.log(Level.WARNING, "Error loading attributes for player $playerUUID: ${e.message}", e)
        }

        return attributes
    }

    /**
     * Deletes a player from the database completely.
     */
    fun deletePlayer(playerUUID: UUID): Boolean {
        try {
            connection?.prepareStatement("DELETE FROM players WHERE uuid = ?")?.use { statement ->
                statement.setString(1, playerUUID.toString())
                val rowsAffected = statement.executeUpdate()
                return rowsAffected > 0
            }
        } catch (e: SQLException) {
            plugin.logger.log(Level.WARNING, "Error deleting player $playerUUID: ${e.message}", e)
        }

        return false
    }

    /**
     * Data class to hold attribute values loaded from the database.
     */
    data class AttributeData(
        val currentValue: Double,
        val maxValue: Double,
    )
}
