package firstplugin.skyblock.skill

import firstplugin.skyblock.xp.SkyblockLevel

interface SkillHolder {
    val skills: List<Skill>

    val skyblockLevel: SkyblockLevel
}
