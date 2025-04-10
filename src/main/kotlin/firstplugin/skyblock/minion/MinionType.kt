package firstplugin.skyblock.minion

import firstplugin.skyblock.skill.Skill
import firstplugin.skyblock.skill.SkillHolder

enum class MinionType(
    /**
     * Returns the skill of the [SkillHolder] associated with the [MinionType].
     */
    val getSkills: (SkillHolder) -> Skill,
) {
    MINING({ it1 -> it1.skills.find { it2 -> it2 is firstplugin.skyblock.skill.MiningSkill }!! }),

//    FORAGING(TODO()),
//    FISHING(TODO()),
//    FARMING(TODO()),
    COMBAT({ it1 -> it1.skills.find { it2 -> it2 is firstplugin.skyblock.skill.CombatSkill }!! }),
//    MISCELLANEOUS(TODO()),
}
