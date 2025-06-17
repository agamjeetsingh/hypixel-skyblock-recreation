/**
 * Package for skill-related functionality in the Skyblock game
 */
package firstplugin.skyblock.skill

import firstplugin.plugin.SkyblockPlugin
import firstplugin.skyblock.attributes.Attribute
import firstplugin.skyblock.attributes.AttributeEffect
import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.utils.sendMessage
import firstplugin.skyblock.xp.SkyblockXPReward
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import java.util.*

/**
 * Maximum level that a skill can reach
 */
const val MAX_SKILL_LEVEL: Int = 60

/**
 * Maximum number of border symbols to display in a single chat line
 */
const val MAX_BORDERS_IN_A_SINGLE_LINE: Int = 53

/**
 * Symbol used for chat border decoration
 */
const val CHAT_BORDER_SYMBOL: String = "▬"

/**
 * Arrow symbol used to indicate level-up in chat messages
 */
const val SKILL_LEVEL_UP_ARROW: String = "➜"

/**
 * Abstract base class for all skills in Skyblock.
 *
 * Skills are player abilities that can level up through gaining experience,
 * provide various rewards and stat bonuses upon leveling up, and contribute
 * to a player's overall progression in the game.
 *
 * @property player The SkyblockPlayer who owns this skill
 */
abstract class Skill(
    val player: SkyblockPlayer,
) {
    /**
     * The name of the skill (e.g., "Mining", "Combat", etc.)
     */
    abstract val skillName: String

    /**
     * The special display name for this skill, used in level-up messages (e.g., "Warrior", "Spelunker", etc.)
     */
    abstract val specialSkillName: String

    /**
     * The primary stat for this skill. Some skills like Taming do not have a `primaryStat`
     * but a custom buff. In those cases, this property is null.
     */
    abstract val primaryStat: Attribute?

    /**
     * The secondary stat for this skill.
     */
    abstract val secondaryStat: Attribute

    /**
     * Represents the reward to be added to `primaryStat`. It can depend on the current value of `level`.
     *
     * It is null if and only if primaryStat is null.
     */
    abstract val primaryStatReward: AttributeEffect?

    /**
     * Represents the reward to be added to `secondaryStat`. It can depend on the current value of `level`.
     */
    abstract val secondaryStatReward: AttributeEffect

    /**
     * Handles applying attribute bonuses when the skill levels up. Used in the `reward()` method.
     * @see reward
     */
    protected open fun statReward() {
        primaryStatReward?.let { primaryStat?.addEffect(it) }
        secondaryStat.addEffect(secondaryStatReward)
    }

    /**
     * The material icon representing this skill in UIs
     */
    abstract val icon: Material

    /**
     * The current level of this skill.
     * Starts with 0, can be changed by subclasses
     */
    open var level = 0
        protected set

    /**
     * Experience progress toward the next level
     */
    var progress: Double = 0.0
        private set

    /**
     * The total XP accumulated in this skill across all levels
     */
    val xp: Double
        get() {
            if (level == 0) {
                return progress
            }
            var totalXP = 0.0
            (1..level).forEach {
                totalXP += xpRequiredForLevelUp[it]!!
            }
            totalXP += progress
            return totalXP
        }

    /**
     * Adds experience points to the skill and handles level-ups automatically.
     *
     * This method will:
     * 1. Add the specified amount of XP to the skill
     * 2. Check if the player has enough XP to level up
     * 3. If they do, increase the level and carry over remaining XP
     * 4. Repeat until all XP is consumed or max level is reached
     *
     * @param amount The amount of XP to add
     */
    fun gainXP(
        amount: Double,
        plugin: SkyblockPlugin,
    ) {
        var xpToBeGained = amount
        while (xpToBeGained > 0) {
            if (level >= MAX_SKILL_LEVEL) {
                return
            }
            if (xpRequiredForLevelUp[level + 1]!! <= xpToBeGained + progress) {
                xpToBeGained -= xpRequiredForLevelUp[level + 1]!! - progress
                progress = 0.0
                level++
                reward()
            } else {
                progress += xpToBeGained
                xpToBeGained = 0.0
            }
        }
        player.sendMessage(
            Component
                .text("+$amount")
                .color(NamedTextColor.DARK_AQUA),
        )
        val pitch: Float = (2 - Random().nextDouble() / 10).toFloat()

        player.playSound(player.location, org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, pitch)

        val actionBarManager = plugin.actionBarManager
        actionBarManager.skillXPGain(
            Component
                .text("+$amount")
                .color(NamedTextColor.DARK_AQUA),
        )
    }

    /**
     * Override this method in subclasses to provide skill-specific rewards
     * beyond the standard coin and stat rewards.
     */
    open fun otherRewards() {
        return
    }

    /**
     * Applies all rewards for leveling up this skill.
     *
     * Note: Should only be run **after** the level-up occurs (i.e. after `level` has been incremented).
     *
     * This method:
     * 1. Gives coin rewards
     * 2. Applies stat bonuses specific to the skill
     * 3. Gives SkyBlock level XP
     * 4. Applies any other skill-specific rewards
     * 5. Sends a congratulatory message to the player
     */
    private fun reward() {
        coinReward()

        statReward()

        skyblockLevelReward()

        otherRewards()

        player.sendMessage(SkillRewardMessage(this).congratulationsMessage())
    }

    /**
     * Awards coins to the player based on the current skill level
     */
    private fun coinReward() {
        player.purse.add(coinReward[level]!!.toLong())
    }

    /**
     * Awards Skyblock level XP based on the current skill level.
     * Higher skill levels award more Skyblock XP
     */
    private fun skyblockLevelReward() {
        skyblockLevelXPReward.applyRewardTo(player)
    }

    val skyblockLevelXPReward
        get() = SkyblockXPReward(skyblockLevelRewardAmount(level))

    abstract fun primarySkillRewardMessage(): List<TextComponent>?

    open fun alternativePrimaryRewardMessage(): TextComponent? = null

    class Requirement<T : Skill>(
        val skillClass: Class<T>,
        val levelRequired: Int = 0,
    )

    open fun otherRewardsMessage(): List<TextComponent> = listOf()

    /**
     * Companion object containing static data and utility methods for all skills
     */
    companion object {
        /**
         * Initializes all skills in the game
         * TODO: Implement this method
         */
        fun setupSkills(player: SkyblockPlayer): List<Skill> {
            val listOfSkills: MutableList<Skill> = mutableListOf()
            listOfSkills.add(CombatSkill(player))
            listOfSkills.add(FarmingSkill(player))
            listOfSkills.add(MiningSkill(player))
            return listOfSkills
        }

        fun skyblockLevelRewardAmount(level: Int): Int =
            when {
                level <= 10 -> 5
                level <= 25 -> 10
                level <= 50 -> 20
                level <= 60 -> 30
                // Shouldn't happen but must add an else block
                else -> 0
            }

        /**
         * Map containing the XP required to level up to each level.
         * Value is the XP required to reach the level denoted by the Key.
         * For example, `xpRequiredForLevelUp[1]` is the XP needed to go from level 0 to level 1.
         */
        val xpRequiredForLevelUp: Map<Int, Int> =
            mapOf(
                1 to 50,
                2 to 125,
                3 to 200,
                4 to 300,
                5 to 500,
                6 to 750,
                7 to 1_000,
                8 to 1_500,
                9 to 2_000,
                10 to 3_500,
                11 to 5_000,
                12 to 7_500,
                13 to 10_000,
                14 to 15_000,
                15 to 20_000,
                16 to 30_000,
                17 to 50_000,
                18 to 75_000,
                19 to 100_000,
                20 to 200_000,
                21 to 300_000,
                22 to 400_000,
                23 to 500_000,
                24 to 600_000,
                25 to 700_000,
                26 to 800_000,
                27 to 900_000,
                28 to 1_000_000,
                29 to 1_100_000,
                30 to 1_200_000,
                31 to 1_300_000,
                32 to 1_400_000,
                33 to 1_500_000,
                34 to 1_600_000,
                35 to 1_700_000,
                36 to 1_800_000,
                37 to 1_900_000,
                38 to 2_000_000,
                39 to 2_100_000,
                40 to 2_200_000,
                41 to 2_300_000,
                42 to 2_400_000,
                43 to 2_500_000,
                44 to 2_600_000,
                45 to 2_750_000,
                46 to 2_900_000,
                47 to 3_100_000,
                48 to 3_400_000,
                49 to 3_700_000,
                50 to 4_000_000,
                51 to 4_300_000,
                52 to 4_600_000,
                53 to 4_900_000,
                54 to 5_200_000,
                55 to 5_500_000,
                56 to 5_800_000,
                57 to 6_100_000,
                58 to 6_400_000,
                59 to 6_700_000,
                60 to 7_000_000,
            )
    }

    /**
     * Map containing coin rewards for each skill level.
     * Key is the level, value is the number of coins awarded for reaching that level.
     * Levels 45-60 all award 1,000,000 coins.
     */
    val coinReward: Map<Int, Int> =
        mapOf(
            1 to 100,
            2 to 250,
            3 to 500,
            4 to 750,
            5 to 1_000,
            6 to 2_000,
            7 to 3_000,
            8 to 4_000,
            9 to 5_000,
            10 to 7_500,
            11 to 10_000,
            12 to 15_000,
            13 to 20_000,
            14 to 25_000,
            15 to 30_000,
            16 to 40_000,
            17 to 50_000,
            18 to 65_000,
            19 to 80_000,
            20 to 100_000,
            21 to 125_000,
            22 to 150_000,
            23 to 175_000,
            24 to 200_000,
            25 to 225_000,
            26 to 250_000,
            27 to 275_000,
            28 to 300_000,
            29 to 325_000,
            30 to 350_000,
            31 to 375_000,
            32 to 400_000,
            33 to 425_000,
            34 to 450_000,
            35 to 475_000,
            36 to 500_000,
            37 to 550_000,
            38 to 600_000,
            39 to 650_000,
            40 to 700_000,
            41 to 750_000,
            42 to 800_000,
            43 to 850_000,
            44 to 900_000,
            45 to 1_000_000,
            46 to 1_000_000,
            47 to 1_000_000,
            48 to 1_000_000,
            49 to 1_000_000,
            50 to 1_000_000,
            51 to 1_000_000,
            52 to 1_000_000,
            53 to 1_000_000,
            54 to 1_000_000,
            55 to 1_000_000,
            56 to 1_000_000,
            57 to 1_000_000,
            58 to 1_000_000,
            59 to 1_000_000,
            60 to 1_000_000,
        )
}
