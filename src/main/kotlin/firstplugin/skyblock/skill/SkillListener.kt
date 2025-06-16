package firstplugin.skyblock.skill

import firstplugin.plugin.SkyblockPlugin
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class SkillListener(
    val plugin: SkyblockPlugin,
) : Listener {
    @EventHandler
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        val skyblockPlayer = plugin.skyblockManager.getPlayer(event.player.name)!!

        if (event.block.type == Material.STONE) {
            skyblockPlayer.skills.find { it is MiningSkill }!!.gainXP(1000000.0, plugin)
        }
    }
}
