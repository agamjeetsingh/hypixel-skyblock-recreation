package firstplugin.legacy.test

import firstplugin.legacy.hotPotato.HotPotatoManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * Command to create a test dummy that acts like a player
 */
class TestCommand(
    private val plugin: JavaPlugin,
    private val gameManager: HotPotatoManager,
) : BukkitCommand("testgame") {
    init {
        description = "Test the hot potato game with dummy players"
        usageMessage = "/testgame [start|stop|hit]"
    }

    // Store our test dummies
    private val dummies = mutableListOf<Zombie>()

    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>,
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("This command can only be used by players").color(NamedTextColor.RED))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(Component.text("Usage: /testgame [start|stop|hit]").color(NamedTextColor.RED))
            return true
        }

        when (args[0].lowercase()) {
            "start" -> startTest(sender)
            "stop" -> stopTest(sender)
            "hit" -> simulateHit(sender)
            else -> {
                sender.sendMessage(Component.text("Unknown subcommand: ${args[0]}").color(NamedTextColor.RED))
                sender.sendMessage(Component.text("Usage: /testgame [start|stop|hit]").color(NamedTextColor.RED))
            }
        }

        return true
    }

    private fun startTest(player: Player) {
        // Clean up any existing dummies
        stopTest(player)

        // Create 3 test dummies near the player
        for (i in 0 until 3) {
            val dummyLocation = player.location.clone().add(2.0 + i, 0.0, 0.0)
            createDummy(dummyLocation, "TestPlayer${i + 1}")
        }

        // Add the player and dummies to game
        player.sendMessage(
            Component.text("Created 3 test dummies. Join the game with /joingame").color(NamedTextColor.GREEN),
        )
    }

    private fun createDummy(
        location: Location,
        name: String,
    ): Zombie {
        val dummy = location.world.spawnEntity(location, EntityType.ZOMBIE) as Zombie

        // Setup the dummy to look more like a player
        dummy.setAI(false) // Disable AI so it doesn't move
        dummy.isSilent = true
        dummy.customName(Component.text(name))
        dummy.isCustomNameVisible = true

        // Make it not burn in daylight
        dummy.setAdult()
        dummy.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 1, false, false))

        // Mark as a test dummy
        dummy.setMetadata("testDummy", FixedMetadataValue(plugin, name))

        // Add to our list
        dummies.add(dummy)

        // Auto-join the game
        simulateJoinGame(dummy, name)

        return dummy
    }

    private fun simulateJoinGame(
        dummy: Zombie,
        playerName: String,
    ) {
        // Tell our game manager to simulate adding a test player
        gameManager.simulateAddTestPlayer(playerName)
    }

    private fun stopTest(player: Player) {
        // Remove all dummies
        dummies.forEach { it.remove() }
        dummies.clear()

        player.sendMessage(Component.text("Removed all test dummies").color(NamedTextColor.GREEN))
    }

    private fun simulateHit(player: Player) {
        // Find closest dummy
        val closestDummy = dummies.minByOrNull { it.location.distanceSquared(player.location) }

        if (closestDummy == null) {
            player.sendMessage(
                Component.text("No test dummies found. Create them with /testgame start").color(NamedTextColor.RED),
            )
            return
        }

        // Simulate the player hitting the dummy
        val event =
            EntityDamageByEntityEvent(
                player,
                closestDummy,
                DamageCause.ENTITY_ATTACK,
                1.0,
            )

        // Call the event manually
        Bukkit.getPluginManager().callEvent(event)

        // Just use the name as a string without trying to extract content
        player.sendMessage(Component.text("Simulated hitting dummy").color(NamedTextColor.GREEN))
    }
}
