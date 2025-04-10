package firstplugin.skyblock.minion.minions.mining

import firstplugin.skyblock.entity.ItemHolder
import firstplugin.skyblock.minion.Minion
import firstplugin.skyblock.minion.MinionHolder
import firstplugin.skyblock.minion.MinionItem
import firstplugin.skyblock.minion.MinionType
import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.skill.SkillHolder
import org.bukkit.Material

class CobblestoneMinion<T>(
    private val owner: T,
) : Minion(Material.COBBLESTONE, owner) where T : ItemHolder, T : SkillHolder, T : MinionHolder {
    override val maxTier: Int = 12

    override val minionType: MinionType = MinionType.MINING

    override val skill: Skill = minionType.getSkills(owner)

    override val generatesMaterial: Material = Material.COBBLESTONE

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
    
    // Initialize this last, after all properties it depends on are initialized
    override var minionItem: MinionItem = CobblestoneMinionItem(owner, this)

    override fun newMinionItem() {
        minionItem = CobblestoneMinionItem(owner, this)
    }
}
