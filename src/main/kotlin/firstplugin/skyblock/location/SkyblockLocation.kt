package firstplugin.skyblock.location

import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.skill.Skill
import kotlin.reflect.KClass

/**
 * Refers to a particular world like "The Hub" or "The Barn". Zones are areas *inside* a location.
 * Both locations and zones have "new area discovered!" texts but only locations have skill requirements.
 *
 * @property skillRequirements The list of skill requirements a player must pass before entering the location.
 * @property zones The list of zones inside this location.
 */
abstract class SkyblockLocation : SkyblockArea() {
    abstract val skillRequirements: List<Skill.Requirement<Skill>>

    /**
     * This function can be called to check whether a player can enter the location or not (i.e. whether they satisfy
     * the skill requirements)
     *
     * @param player The player that is attempting to enter the location.
     */
    fun canEnterLocation(player: SkyblockPlayer): Boolean {
        for (requirement in skillRequirements) {
            val skillClass: KClass<Skill> = requirement.skillClass
            val playerSkill: Skill? = player.skills.find { skillClass.isInstance(it) }
            if (playerSkill == null || playerSkill.level < requirement.levelRequired) {
                return false
            }
        }
        return true
    }

    abstract val zones: List<Zone>
}
