package firstplugin.skyblock.minion.minions.mining

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.minion.Minion
import firstplugin.skyblock.minion.MinionHolder
import firstplugin.skyblock.minion.MinionItem
import firstplugin.skyblock.skill.SkillHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class CobblestoneMinionItem<T>(
    override val holder: T,
    override val minion: Minion,
) : MinionItem(Material.PLAYER_HEAD) where T : ItemHolder, T : MinionHolder, T : SkillHolder {
    override val tier: Int = minion.tier
    override val placeThisMinionComponent: Component =
        Component
            .text("Place this minion and it will start generating and mining cobblestone!")
            .color(NamedTextColor.GRAY)
            .decoration(TextDecoration.ITALIC, false)
    override val textures: List<String> =
        listOf(
            "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1NTE5MTYsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNGUwMWY1NTI1NDkwMzdhZTg4ODc1NzA3MDBlNzRkYjIwYzZmMDI2YTY1MGFlZWM1ZDljOGVjNTFiYTNmNTE1In19fQ==",
        )

    init {
        initializeMinionItem()
    }
}
