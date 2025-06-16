package firstplugin.skyblock.mining

import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.entity.miningSpeed
import firstplugin.skyblock.items.SkyblockItem
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

abstract class SkyblockBlock : Block {
    val breakingPower: Int = TODO()

    val blockStrength: BlockStrength = TODO()

    val miningSpeedToInstaBreak: Int
        get() = 1 + blockStrength.blockStrength * 60

    override fun getDestroySpeed(itemStack: ItemStack): Float {
        val skyblockItem = itemStack as? SkyblockItem ?: return 1.0f
        val skyblockPlayer: SkyblockPlayer = skyblockItem.holder as SkyblockPlayer
        skyblockPlayer.miningSpeed
        TODO()
    }
}
