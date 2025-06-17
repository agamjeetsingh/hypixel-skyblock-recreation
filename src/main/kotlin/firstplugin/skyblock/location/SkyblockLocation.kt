package firstplugin.skyblock.location

import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.skill.Skill
import kotlin.reflect.KClass

/**
 * Refers to a particular world like "The Hub".
 */
abstract class SkyblockLocation {
    abstract val skillRequirements: List<Skill.Requirement<Skill>>

    fun canEnterLocation(player: SkyblockPlayer): Boolean {
        for (requirement in skillRequirements) {
            val skillClass: KClass<Skill> = requirement.skillClass.kotlin
            val playerSkill: Skill? = player.skills.find { skillClass.isInstance(it) }
            if (playerSkill == null || playerSkill.level < requirement.levelRequired) {
                return false
            }
        }
        return true
    }

    abstract val zones: List<Zone>

    // TODO - New area discovered reward message
}
