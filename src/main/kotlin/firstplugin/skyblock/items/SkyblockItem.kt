package firstplugin.skyblock.items

import org.bukkit.Material

class SkyblockItem(
    val material: Material,
) {
    fun x() {
        if (material == Material.AIR) {
            return
        }
    }
}
