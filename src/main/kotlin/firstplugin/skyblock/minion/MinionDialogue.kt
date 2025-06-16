package firstplugin.skyblock.minion

import org.bukkit.entity.Player

enum class MinionDialogue(
    /**
     * Dialogue is `null` when the dialogue option doesn't apply to the particular minion.
     */
    val dialogue: (Minion, Player?) -> String?,
) {
    /**
     * If the minion's mobs are out of range
     */
    CANNOT_REACH_ANY_MOBS({ minion, _ ->
        "I can't reach any ${(minion as? firstplugin.skyblock.minion.minions.combat.CombatMinion)?.pluralFormOfMob ?: "mobs"}"
    }),

    /**
     * If the minion's storage was full and now has space
     */
    BACK_TO_WORK({ _, _ -> "Back to work! Break is over" }),

    /**
     * If the minion setup is not consistent with optimal setup
     */
    LOCATION_NOT_PERFECT({ _, _ -> "This location isn't perfect! :(" }),

    /**
     * If the minion is on fire
     */
    ON_FIRE({ _, _ -> "This is fine." }),

    /**
     * If the minion has no places to spawn a mob
     */
    NO_PLACE_TO_SPAWN_MOB({ minion, _ ->
        "I have no space to spawn ${(minion as? firstplugin.skyblock.minion.minions.combat.CombatMinion)?.pluralFormOfMob ?: "mobs" }"
    }),

    /**
     * There is a Zombie nearby
     */
    ZOMBIE_NEARBY({ _, _ -> "Yikes a Zombie! I was wondering where the smell was coming from..." }),

    /**
     * There is a Rabbit nearby
     */
    RABBIT_NEARBY({ _, _ -> "That's a cute Rabbit! Come here and see me little Rabbit!" }),

    /**
     * If there is a Skeleton nearby and the minion is a SkyBlock Wheat, Carrot, or Potato Minion
     */
    GET_SKELETON_AWAY_FROM_MY_CROPS({ _, _ -> "Someone get that Skeleton away from my crops!" }),

    /**
     * If there is a Spider nearby
     */
    SPIDER_NEARBY({ _, _ -> "EEEKKKKK! NOT A SPIDER!" }),
}
