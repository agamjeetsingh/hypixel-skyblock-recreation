package firstplugin.skyblock.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Utility functions for working with ItemStacks
 */
object ItemUtils {
    /**
     * Creates an item with an empty name.
     *
     * @param material The material to use
     * @return ItemStack with the specified material and empty name
     */
    fun createEmptyNameItem(material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta

        // Set an empty display name
        meta.displayName(Component.text("").decoration(TextDecoration.ITALIC, false))

        item.itemMeta = meta
        return item
    }
}
