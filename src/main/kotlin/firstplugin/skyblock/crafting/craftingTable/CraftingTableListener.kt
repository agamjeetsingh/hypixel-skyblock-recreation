package firstplugin.skyblock.crafting.craftingTable

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.crafting.craftingTable.CraftingTableMenu.Companion.CLOSE_ITEM_SLOT_NUMBER
import firstplugin.skyblock.crafting.craftingTable.CraftingTableMenu.Companion.CRAFTED_ITEM_SLOT_NUMBER
import firstplugin.skyblock.crafting.craftingTable.CraftingTableMenu.Companion.CRAFTING_SLOT_NUMBERS
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent

class CraftingTableListener(
    val plugin: SkyblockPlugin,
) : Listener {
    @EventHandler
    fun onCraftingTableClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val clickedBlock = event.clickedBlock ?: return
        if (clickedBlock.type != Material.CRAFTING_TABLE) {
            return
        }

        // Cancel the default vanilla behavior
        event.isCancelled = true

        val skyblockPlayer = plugin.skyblockManager.getPlayer(event.player.name) ?: return

        // Open the crafting table menu
        val craftingTable = CraftingTable(skyblockPlayer)
        craftingTable.openCraftingMenu()
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: return
        val craftingTableMenu =
            inventory.getHolder(false) as? CraftingTableMenu ?: return

        val player = event.whoClicked as? Player ?: return
        val skyblockPlayer = plugin.skyblockManager.getPlayer(player.name) ?: return

        // Only cancel clicks in the top inventory (crafting table)
        // Allow clicks in the player's own inventory
        event.isCancelled = event.clickedInventory != player.inventory

        val clickedSlot = event.slot

        if (clickedSlot in CRAFTING_SLOT_NUMBERS) {
            event.isCancelled = false
            craftingTableMenu.updateCraftingSlot(plugin)
        } else if (clickedSlot == CRAFTED_ITEM_SLOT_NUMBER) {
            // TODO - Only allow if its not a barrier block roughly speaking
            event.isCancelled = false
        } else if (clickedSlot == CLOSE_ITEM_SLOT_NUMBER) {
            // TODO - Return the player's items in their inventory or drop them
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
        }
    }
}
