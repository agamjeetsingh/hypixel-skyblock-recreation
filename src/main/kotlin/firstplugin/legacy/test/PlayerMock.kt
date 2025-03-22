package firstplugin.legacy.test

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

/**
 * A more advanced approach for testing would be to create a player mock
 * Note: For full implementation, consider using a library like MockBukkit
 */
class PlayerMock(
    private val plugin: JavaPlugin,
    private val name: String,
) {
    // This is a simplified mock - for serious testing you'd use a full mocking library
    private val uuid = UUID.randomUUID()
    private var zombie: Zombie? = null

    // Spawn a zombie to represent this "player" in the world
    fun spawn(location: Location): Zombie {
        // Remove any existing zombie
        remove()

        // Create a new zombie
        val newZombie = location.world.spawnEntity(location, EntityType.ZOMBIE) as Zombie
        zombie = newZombie

        // Configure the zombie
        newZombie.setAI(false)
        newZombie.isSilent = true
        newZombie.customName(
            net.kyori.adventure.text.Component
                .text(name),
        )
        newZombie.isCustomNameVisible = true

        // Mark it as our mock
        newZombie.setMetadata("playerMock", FixedMetadataValue(plugin, uuid.toString()))

        return newZombie
    }

    // Remove the zombie from the world
    fun remove() {
        zombie?.remove()
        zombie = null
    }

    // This method simulates joining the game
    fun simulateJoinGame() {
        // In a full mock, you'd do:
        // 1. Create a real Player mock object (requires more advanced mocking)
        // 2. Call the game manager with that mock

        // For now just log the attempt
        plugin.logger.info("Mock player $name would join the game")
    }

    // Get the zombie entity
    fun getEntity(): Zombie? = zombie
}
