package firstplugin.skyblock.minion.minions.combat

import firstplugin.skyblock.minion.Minion
import firstplugin.skyblock.minion.MinionHolder
import org.bukkit.Material
import org.bukkit.entity.Mob

abstract class CombatMinion(
    material: Material,
    holder: MinionHolder,
) : Minion(material, holder) {
    abstract val mob: Mob

    /**
     * If [mob] is "Zombie Pigman" then this would be equal to "Zombie Pigmen".
     */
    abstract val pluralFormOfMob: String
}
