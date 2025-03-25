package firstplugin.skyblock.items

enum class Rarity {
    /**
     * COMMON is assigned to items that are very easy to get. It is the default rarity assigned to items and there is no tier lower.
     */
    COMMON,

    /**
     * UNCOMMON is for items that are still likely to be early game but are not quite common.
     * This is the lowest tier you can get with a recombobulated item.
     */
    UNCOMMON,

    /**
     * RARE items are mostly early game utility; things you strive for as an early game player.
     * This includes lots of accessories, minions, and upgrades.
     */
    RARE,

    /**
     * EPIC items start to become more expensive.
     * These items are meant to stand out and take some hard effort to obtain.
     */
    EPIC,

    /**
     * LEGENDARY items mark the end of the line for most non-cosmetic item's base rarity.
     * They are meant to be the hardest to obtain and are often low drop chance items.
     */
    LEGENDARY,

    /**
     * There are only a few non-cosmetic items with a base rarity of MYTHIC, including the Divan's Drill, Radioactive Vial, and Plasma Bucket.
     * They can also be obtained by upgrading LEGENDARY items as described below.
     */
    MYTHIC,

    /**
     * Divine rarity is for any items that have been upgraded from MYTHIC.
     * Items such as Cake Souls and Divan's Drill can be upgraded to DIVINE using a Recombobulator 3000.
     */
    DIVINE,

    /**
     * The SPECIAL tag is reserved for Admin Items, trophies, and collectibles.
     * The SPECIAL rarity is not meant to be obtained by upgrading items, but is instead reserved for cosmetic items such as Spooky Pies and Creative Minds.
     */
    SPECIAL,

    /**
     * VERY SPECIAL is a rarity that exists for upgraded SPECIAL items.
     */
    VERY_SPECIAL,

    /**
     * ULTIMATE is a rarity that exists for skins that can have their appearance swapped from different selections.
     */
    ULTIMATE,

    /**
     * ADMIN is a rarity that exists for items exclusive to players with the ADMIN rank.
     * These items are unobtainable for regular players, and if a player without the rank manages to obtain one, it automatically gets blacklisted.
     */
    ADMIN,
}
