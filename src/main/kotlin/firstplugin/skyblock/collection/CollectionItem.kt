package firstplugin.skyblock.collection

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

interface CollectionItem {
    val collectionItemLore: Component
        get() =
            Component
                .text("Collection Item")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
}
