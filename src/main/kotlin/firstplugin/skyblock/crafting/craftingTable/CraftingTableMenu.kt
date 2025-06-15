package firstplugin.skyblock.crafting.craftingTable

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.crafting.CraftingSlot
import firstplugin.skyblock.crafting.EmptyCraftingSlot
import firstplugin.skyblock.crafting.recipe.CraftingRecipeRegistry
import firstplugin.skyblock.crafting.recipe.SkyblockRecipe
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.items.SkyblockItem
import firstplugin.skyblock.items.SkyblockItemRegistry
import firstplugin.skyblock.utils.ItemUtils
import firstplugin.skyblock.utils.SkyblockLore
import firstplugin.skyblock.utils.SkyblockLore.Companion.MENU_MAX_LINE_LENGTH
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class CraftingTableMenu(
    val player: SkyblockPlayer,
) : InventoryHolder {
    private val inventory: Inventory = createMenu()

    override fun getInventory(): Inventory = inventory

    private val craftingSlots: List<CraftingSlot>
        get() {
            return CRAFTING_SLOT_NUMBERS.map {
                val itemStack = inventory.getItem(it) ?: return@map EmptyCraftingSlot()
                val quantity = itemStack.amount
                CraftingSlot(
                    // TODO - Maybe let the item holder be dummy? And change the item when it enters the inventory
                    // TODO - Or change it when the player shift clicks or clicks on it (change item on cursor)
                    SkyblockItemRegistry.itemStackToSkyblockItem(itemStack, player) ?: return@map EmptyCraftingSlot(),
                    quantity,
                )
            }
        }

    fun updateCraftingSlot(plugin: SkyblockPlugin) {
        val recipe: SkyblockRecipe? = CraftingRecipeRegistry.getRecipe(craftingSlots, plugin)
        if (recipe == null) {
            inventory.setItem(CRAFTED_ITEM_SLOT_NUMBER, createRecipeRequiredItem())
            return
        }
        val constructor =
            recipe.craftedItemClass.constructors.firstOrNull {
                it.parameters.size == 1
            }
        if (constructor == null) {
            inventory.setItem(CRAFTED_ITEM_SLOT_NUMBER, createRecipeRequiredItem())
            return
        }
        val skyblockItem: SkyblockItem = constructor.call(player)
        skyblockItem.amount = recipe.craftedItemQuantity
        inventory.setItem(CRAFTED_ITEM_SLOT_NUMBER, skyblockItem)
    }

    private fun createMenu(): Inventory {
        val inventory: Inventory = Bukkit.createInventory(this, 54, Component.text("Craft Item"))

        val emptyGrayGlassPane = ItemUtils.createEmptyNameItem(Material.GRAY_STAINED_GLASS_PANE)
        val emptyRedGlassPane = ItemUtils.createEmptyNameItem(Material.RED_STAINED_GLASS_PANE)

        // Setting Gray and Red Glass Panes
        (0..8).forEach { inventory.setItem(it, emptyGrayGlassPane) }
        listOf(9, 13, 14, 15, 17).forEach { inventory.setItem(it, emptyGrayGlassPane) }
        listOf(18, 22, 24, 26).forEach { inventory.setItem(it, emptyGrayGlassPane) }
        listOf(27, 31, 32, 33, 35).forEach { inventory.setItem(it, emptyGrayGlassPane) }
        (36..44).forEach { inventory.setItem(it, emptyGrayGlassPane) }
        (45..48).forEach { inventory.setItem(it, emptyRedGlassPane) }
        (50..53).forEach { inventory.setItem(it, emptyRedGlassPane) }

        // Slot where crafted items appear
        inventory.setItem(CRAFTED_ITEM_SLOT_NUMBER, createRecipeRequiredItem())

        // Menu Closing Item
        inventory.setItem(CLOSE_ITEM_SLOT_NUMBER, createCloseItem())

        return inventory
    }

    private fun createRecipeRequiredItem(): ItemStack {
        val item = ItemStack(Material.BARRIER)
        val meta = item.itemMeta

        meta.displayName(
            Component
                .text("Recipe Required")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false),
        )

        val skyblockLore = SkyblockLore(MENU_MAX_LINE_LENGTH)
        skyblockLore.addLore(
            Component
                .text("Add the items for a valid recipe in the crafting grid to the left!")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false),
        )
        meta.lore(skyblockLore.lore)

        item.itemMeta = meta
        return item
    }

    private fun createCloseItem(): ItemStack {
        val item = ItemStack(Material.BARRIER)
        val meta = item.itemMeta

        meta.customName(
            Component
                .text("Close")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false),
        )

        item.itemMeta = meta
        return item
    }

    companion object {
        const val CRAFTED_ITEM_SLOT_NUMBER = 23

        const val CLOSE_ITEM_SLOT_NUMBER = 49

        val CRAFTING_SLOT_NUMBERS = listOf(10, 11, 12, 19, 20, 21, 28, 29, 30)
    }
}
