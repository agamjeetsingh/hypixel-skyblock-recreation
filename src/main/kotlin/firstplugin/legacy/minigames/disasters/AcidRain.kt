package firstplugin.legacy.minigames.disasters

import firstplugin.legacy.minigames.Game
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.Random

class AcidRain(
    override val game: Game,
) : Disaster {
    private val runnableAcidRain =
        object : BukkitRunnable() {
            override fun run() {
                world.setStorm(true)
                for (player in game.players) {
                    // Make players disconnect -> eliminated
                    if (player.location.world != world || !player.isOnline) continue
                    val highestBlock = world.getHighestBlockAt(player.location)
                    // The + 1 accounts for the possibility of the player being stuck in a block
                    // but with their head sticking out in the rain
                    if (highestBlock.location.y <= player.location.y + 1) {
                        player.damage(1.0)
                    }
                }

                val selectedRows = mutableListOf<Int>()
                for (x in -100..100) {
                    if (randomChance()) {
                        selectedRows.add(x)
                    }
                }
                for (permissibleRow in selectedRows) {
                    for (z in -100..100) {
                        if (randomChance()) {
                            val locationOfHighestBlock = Location(world, permissibleRow.toDouble(), 0.0, z.toDouble())
                            val highestBlock = world.getHighestBlockAt(locationOfHighestBlock)
                            highestBlock.type = Material.AIR
                            world.playSound(highestBlock.location, Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f)
                        }
                    }
                }
            }
        }

    private var task: BukkitTask? = null

    override fun start() {
        task = runnableAcidRain.runTaskTimer(plugin, 0L, 20L)
    }

    override fun stop() {
        world.setStorm(false)
        task?.cancel()
        task = null
    }

    private val rng = Random()

    // In percent
    private fun randomChance(chance: Int = 10): Boolean = rng.nextInt(100) < chance
}
