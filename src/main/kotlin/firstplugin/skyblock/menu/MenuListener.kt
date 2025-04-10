package firstplugin.skyblock.menu

import firstplugin.skyblock.entity.SkyblockPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class MenuListener(
    private val manager: MenuManager,
) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        // Make ServerPlayer and SkyBlock using some database
        val item = event.item ?: return

        if (!event.action.isLeftClick && !event.action.isRightClick) return

        val meta = item.itemMeta ?: return
        val container = meta.persistentDataContainer

        if (container.has(MenuManager.MENU_KEY, PersistentDataType.STRING)) {
            val value = container.get(MenuManager.MENU_KEY, PersistentDataType.STRING)
            if (value != player.uniqueId.toString()) return
            event.isCancelled = true
            manager.openMainMenu(SkyblockPlayer(player))
        }
    }

    @EventHandler
    fun onClickInMenu(event: InventoryClickEvent) {
        val inventory = event.inventory

        if (manager.hasMenuTag(inventory)) {
            event.isCancelled = true

            // Perform actions based on clicked item
            // (Can be expanded later for more menu functionality)
        }
    }

    @EventHandler
    fun onClickInPlayerInventory(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as? Player ?: return

        val clickedItem = event.currentItem ?: return
        val meta = clickedItem.itemMeta ?: return
        val container = meta.persistentDataContainer
        if (container.has(MenuManager.MENU_KEY, PersistentDataType.STRING)) {
            val value = container.get(MenuManager.MENU_KEY, PersistentDataType.STRING)
            if (value == player.uniqueId.toString()) {
                event.isCancelled = true
                player.itemOnCursor = ItemStack(Material.AIR)
                manager.openMainMenu(SkyblockPlayer(player))
            }
        }
    }

    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        val droppedItem = event.itemDrop
        val meta = droppedItem.itemStack.itemMeta ?: return
        val container = meta.persistentDataContainer

        if (container.has(MenuManager.MENU_KEY, PersistentDataType.STRING)) {
            val value = container.get(MenuManager.MENU_KEY, PersistentDataType.STRING)
            if (value == event.player.uniqueId.toString()) {
                event.isCancelled = true
                manager.openMainMenu(SkyblockPlayer(event.player))
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        setMenuItemForPlayer(SkyblockPlayer(event.player))
    }

    private fun setMenuItemForPlayer(player: SkyblockPlayer) {
        val inventory = player.inventory
        val menuStar = MenuItem.createStarMenu(player)
        inventory.setItem(8, menuStar)
    }
}
