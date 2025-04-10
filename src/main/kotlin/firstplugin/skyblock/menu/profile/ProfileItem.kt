package firstplugin.skyblock.menu.profile

import firstplugin.skyblock.attributes.Attribute.Companion.friendlierAttributeName
import firstplugin.skyblock.entity.SkyblockPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

private val PROFILE_KEY = NamespacedKey("firstplugin", "skyblock_menu_profile")

object ProfileItem {
    fun createSkyblockProfile(player: SkyblockPlayer): ItemStack {
        val playerHead = ItemStack(Material.PLAYER_HEAD)
        val skullMeta: SkullMeta = playerHead.itemMeta as SkullMeta
        // setOwningPlayer works for Player objects and not for SkyblockPlayer
        skullMeta.setOwningPlayer(player.serverPlayer.bukkitPlayer)
        skullMeta.displayName(
            Component
                .text("Your Skyblock Profile")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false),
        )

        val lore =
            mutableListOf<Component>(
                Component
                    .text("View your equipment, stats, and more!")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false),
                Component
                    .text(""),
            )

        player.attributes.forEach {
            val component =
                Component
                    .text(" ${it.symbol} ${friendlierAttributeName(it)} ")
                    .color(it.color)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(
                        Component
                            .text(it.prettyPrintValueForMenu)
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false),
                    )
            lore.add(component)
        }

        skullMeta.lore(lore)

        val container = skullMeta.persistentDataContainer
        container.set(
            PROFILE_KEY,
            PersistentDataType.STRING,
            player.uniqueId.toString(),
        )

        playerHead.itemMeta = skullMeta
        return playerHead
    }
}
