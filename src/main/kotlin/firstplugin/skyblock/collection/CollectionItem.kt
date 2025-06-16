package firstplugin.skyblock.collection

import firstplugin.skyblock.items.SkyblockItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

/**
 * Represents those [SkyblockItem]s that have their own collections. Only classes
 * that extend [SkyblockItem] must implement this interface.
 */
interface CollectionItem {
    val collection: Collection

    val collectionItemLore: Component
        get() =
            Component
                .text("Collection Item")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
}
