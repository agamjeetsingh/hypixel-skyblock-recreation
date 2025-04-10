package firstplugin.skyblock.menu

import firstplugin.skyblock.entity.SkyblockPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object MenuItem {
    fun createStarMenu(player: SkyblockPlayer): ItemStack {
        val item = ItemStack(Material.NETHER_STAR)
        val meta = item.itemMeta

        val itemDisplayName =
            Component
                .text("SkyBlock Menu")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    (Component.text(" (Click)"))
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                )
        meta.customName(itemDisplayName)

        // Set lore using Component API
        val lore =
            mutableListOf(
                Component
                    .text("View all of your Skyblock progress,")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
                Component
                    .text("including your Skills, Collections,")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
                Component
                    .text("Recipes, and more!")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
                Component
                    .text(""),
                Component
                    .text("Click to open!")
                    .color(NamedTextColor.YELLOW)
                    .decoration(TextDecoration.ITALIC, false),
            )
        meta.lore(lore)

        val container = meta.persistentDataContainer
        container.set(
            MenuManager.MENU_KEY,
            PersistentDataType.STRING,
            player.uniqueId.toString(),
        )

        item.itemMeta = meta
        return item
    }
}
