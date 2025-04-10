package firstplugin.skyblock.items.reforge

import firstplugin.skyblock.items.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

interface Reforgeable {
    var reforge: Reforge?

    val isReforged: Boolean
        get() = reforge != null

    fun applyRandomReforge() {
        reforge = Reforge.randomReforge()
    }

    val reforgeableLore: Component
        get() =
            Component
                .text("This item can be reforged!")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)

    // The item's name, used for generating the reforged name
    val itemName: String

    // The item's rarity for coloring
    val itemRarity: Rarity
}
