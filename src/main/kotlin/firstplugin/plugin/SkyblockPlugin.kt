package firstplugin.plugin

import firstplugin.Rank
import firstplugin.ServerPlayer
import firstplugin.skyblock.SkyblockManager
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class SkyblockPlugin :
    JavaPlugin(),
    Listener {
    private lateinit var skyblockManager: SkyblockManager
    private val shutdownHooks = mutableListOf<() -> Unit>()

    override fun onEnable() {
        logger.info("RPG Plugin has been enabled!")

        server.pluginManager.registerEvents(this, this)

        skyblockManager = SkyblockManager(this)
        skyblockManager.initialize()

        saveDefaultConfig()
    }

    override fun onDisable() {
        // Run all registered shutdown hooks
        shutdownHooks.forEach { hook ->
            try {
                hook()
            } catch (e: Exception) {
                logger.severe("Error executing shutdown hook: ${e.message}")
            }
        }
        
        logger.info("RPG Plugin has been disabled!")
    }
    
    /**
     * Register a shutdown hook to be executed when the plugin is disabled
     */
    fun registerShutdownHook(hook: () -> Unit) {
        shutdownHooks.add(hook)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendMessage(
            Component
                .text("Welcome to the server, ${player.name}!")
                .color(NamedTextColor.GREEN),
        )
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val player = event.player
        val serverPlayer = ServerPlayer(player, Rank.MVP_PLUS)

        event.renderer { _, _, message, _ ->
            serverPlayer.rank
                .formatted()
                .append(Component.text(player.name).color(serverPlayer.rank.colorOfRank))
                .append(Component.text(": ").color(serverPlayer.rank.chatColor))
                .append(message.color(serverPlayer.rank.chatColor))
        }
    }
}
