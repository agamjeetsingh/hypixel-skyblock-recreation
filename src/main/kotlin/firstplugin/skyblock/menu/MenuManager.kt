package firstplugin.skyblock.menu

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.SkyblockPlayer
import firstplugin.skyblock.menu.profile.ProfileItem
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory

class MenuManager(
    val plugin: SkyblockPlugin,
) : Listener {
    companion object {
        val MENU_KEY = NamespacedKey("firstplugin", "skyblock_menu")
        private val MENU_TITLE = Component.text("SkyBlock Menu")
    }

    fun initialize() {
        plugin.server.pluginManager.registerEvents(MenuListener(this), plugin)
    }

    fun openMainMenu(player: SkyblockPlayer) {
        val inventory: Inventory = Bukkit.createInventory(null, 54, Component.text("SkyBlock Menu"))

        val skyblockProfile = ProfileItem.createSkyblockProfile(player)
        inventory.setItem(13, skyblockProfile)

        player.openInventory(inventory)
    }

    // Too Ad hoc

    fun hasMenuTag(inventory: Inventory): Boolean =
        inventory.getHolder(false) == null &&
            inventory.getViewers().isNotEmpty() &&
            inventory.getSize() == 54
    // TODO()
}
