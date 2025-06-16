package firstplugin.skyblock.collection

import firstplugin.skyblock.entity.SkyblockPlayer
import firstplugin.skyblock.rewards.SkyblockReward

abstract class Collection(
    val holder: CollectionHolder,
) {
    abstract val collectionItem: CollectionItem

    var tier: Int = 0
        private set

    abstract val skyblockRewards: Map<Int, List<SkyblockReward>>

    val maxTier: Int
        get() = skyblockRewards.keys.max()

    var collectionProgress: Int = 0
        private set

    fun addToCollection(progress: Int): Boolean {
        if (progress <= 0) {
            return false
        }
        collectionProgress += progress
        updateTier()
        return true
    }

    private fun updateTier() {
        while (tier < maxTier && tierRequirementsCumulativeMap[tier + 1]!! <= collectionProgress) {
            tier++
            collectionRewardMessage()
            addRewards()
        }
    }

    private fun addRewards() {
        val skyblockPlayer = holder as? SkyblockPlayer ?: return
        skyblockRewards[tier]!!.forEach { it.applyRewardTo(skyblockPlayer) }
    }

    /**
     * Should only be called **after** the [tier] has been updated.
     */
    private fun collectionRewardMessage() {
        TODO()
    }

    /**
     * Represents the (non-cumulative) collection progress needed to progress to that tier.
     */
    abstract val tierRequirementsMap: Map<Int, Int>

    val tierRequirementsCumulativeMap: Map<Int, Int>
        get() {
            val map: MutableMap<Int, Int> = mutableMapOf()
            var totalRequirement = 0
            for (tier in tierRequirementsMap.keys) {
                totalRequirement += tierRequirementsMap[tier]!!
                map[tier] = totalRequirement
            }
            return map
        }

    companion object {
        /**
         * The Skyblock XP gained for every unlocked collection
         */
        private val SKYBLOCK_XP_REWARD_AMOUNT = 4

        fun setupCollections(collectionHolder: CollectionHolder): List<Collection> {
            return listOf()
            TODO()
        }
    }
}
