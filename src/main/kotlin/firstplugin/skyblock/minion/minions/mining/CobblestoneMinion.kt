package firstplugin.skyblock.minion.minions.mining

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.items.DummyItemHolder
import firstplugin.skyblock.items.vanilla.Cobblestone
import firstplugin.skyblock.minion.Minion
import firstplugin.skyblock.minion.MinionHolder
import firstplugin.skyblock.minion.MinionItem
import firstplugin.skyblock.minion.MinionType
import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.skill.SkillHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

class CobblestoneMinion<T>(
    private val owner: T,
) : Minion(Cobblestone(DummyItemHolder()), owner) where T : ItemHolder, T : SkillHolder, T : MinionHolder {
    override val maxTier: Int = 12

    override val minionType: MinionType = MinionType.MINING

    override val skill: Skill = minionType.getSkills(owner)

    // Define this first since MinionItem depends on it
    override val timeBetweenActionsMapInSeconds: Map<Int, Int> =
        mapOf(
            1 to 14,
            2 to 14,
            3 to 12,
            4 to 12,
            5 to 10,
            6 to 10,
            7 to 9,
            8 to 9,
            9 to 8,
            10 to 8,
            11 to 7,
            12 to 6,
        )

    // Initialise this last, after all properties it depends on are initialized
    override var minionItem: MinionItem = CobblestoneMinionItem(owner, tier)

    override fun newMinionItem() {
        minionItem = CobblestoneMinionItem(owner, tier)
    }

    private inner class CobblestoneMinionItem<T>(
        override val holder: T,
        tier: Int,
    ) : MinionItem(Material.PLAYER_HEAD) where T : ItemHolder, T : MinionHolder, T : SkillHolder {
        override val minion: Minion = this@CobblestoneMinion
        override val tier: Int = 1.coerceAtLeast(tier).coerceAtMost(maxTier)

        override val placeThisMinionComponent: Component =
            Component
                .text("Place this minion and it will start generating and mining cobblestone!")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
        override val textures: List<String> =
            listOf(
                "eyJ0aW1lc3RhbXAiOjE1NTc5MzQ1NTE5MTYsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNGUwMWY1NTI1NDkwMzdhZTg4ODc1NzA3MDBlNzRkYjIwYzZmMDI2YTY1MGFlZWM1ZDljOGVjNTFiYTNmNTE1In19fQ==",
            )

        override val internalID: String = "COBBLESTONE_GENERATOR_${this.tier}"

        init {
            initialiseMinionItem()
        }
    }
}
