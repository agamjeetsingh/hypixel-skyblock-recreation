package firstplugin.plugin

import firstplugin.Rank
import firstplugin.ServerPlayer
import firstplugin.skyblock.SkyblockManager
import firstplugin.skyblock.attributes.ConstantAttributeEffect
import firstplugin.skyblock.display.ActionBarManager
import firstplugin.skyblock.entity.speed
import firstplugin.skyblock.items.weapons.swords.DiamondSword
import firstplugin.skyblock.items.weapons.swords.RogueSword
import firstplugin.skyblock.minion.minions.mining.CobblestoneMinion
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class SkyblockPlugin :
    JavaPlugin(),
    Listener {
    lateinit var skyblockManager: SkyblockManager
        private set
    private lateinit var actionBarManager: ActionBarManager
    private val shutdownHooks = mutableListOf<() -> Unit>()

    override fun onEnable() {
        logger.info("RPG Plugin has been enabled!")

        server.pluginManager.registerEvents(this, this)

        skyblockManager = SkyblockManager(this)
        skyblockManager.initialize()

        // Initialize the action bar manager
        actionBarManager = ActionBarManager(this)
        actionBarManager.initialize()

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

    /**
     * Gives a Rogue Sword to the specified player
     */
    fun giveRogueSword(player: Player) {
        // Get SkyblockPlayer instance
        val skyblockPlayer = skyblockManager.getPlayer(player.name) ?: return

        // Create a new RogueSword with the player as the holder
        val rogueSword = RogueSword(skyblockPlayer)

        // Add the item directly to player's inventory
        player.inventory.addItem(rogueSword)
        player.inventory.addItem(DiamondSword(skyblockPlayer))

        player.sendMessage(
            Component
                .text("You received a Rogue Sword!")
                .color(NamedTextColor.GREEN),
        )
    }
    
    /**
     * Gives a Cobblestone Minion to the specified player
     */
    fun giveCobblestoneMinion(player: Player) {
        // Get SkyblockPlayer instance
        val skyblockPlayer = skyblockManager.getPlayer(player.name) ?: return
        
        // Create a new CobblestoneMinion with the player as the owner
        val cobblestoneMinion = CobblestoneMinion(skyblockPlayer)
        
        // Add the minion item to player's inventory
        player.inventory.addItem(cobblestoneMinion.minionItem)
        
        player.sendMessage(
            Component
                .text("You received a Cobblestone Minion!")
                .color(NamedTextColor.GREEN),
        )
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendMessage(
            Component
                .text("Welcome to the server, ${player.name}!")
                .color(NamedTextColor.GREEN),
        )

        // Give player items with a small delay to ensure everything is loaded
        server.scheduler.runTaskLater(
            this,
            Runnable {
                giveRogueSword(player)
                giveCobblestoneMinion(player)
            },
            20L,
        )

        val skyblockPlayer = skyblockManager.getPlayer(player.name) ?: return
        skyblockPlayer.speed.constantModifiers.add(ConstantAttributeEffect(1000.0))
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
