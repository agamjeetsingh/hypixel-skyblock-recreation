package firstplugin.skyblock.location

import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.skill.Skill
import kotlin.reflect.KClass

/**
 * Refers to a particular world like "The Hub" or "The Barn". Zones are areas *inside* a location.
 * Both locations and zones have "new area discovered!" texts but only locations have skill requirements.
 */
abstract class SkyblockLocation : SkyblockArea() {
    abstract val skillRequirements: List<Skill.Requirement<Skill>>

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
