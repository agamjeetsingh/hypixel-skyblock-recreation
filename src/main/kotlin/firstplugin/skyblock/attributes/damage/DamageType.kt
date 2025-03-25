package firstplugin.skyblock.attributes.damage

enum class DamageType {
    /**
     * A typical melee or ranged attack from a player or enemy.
     */
    NORMAL,

    /**
     * A special type of attack, typically from an Item Ability.
     */
    MAGIC,

    /**
     * A type of damage that ignores the victim's Defense.
     */
    TRUE,

    /**
     * A type of damage that results from falling from a high distance.
     */
    FALL,

    /**
     * A type of damage from either standing in lava or being on fire. Deals True Damage.
     */
    FIRE,

    /**
     * A type of damage that results from being poisoned with the poison effect.
     */
    POISON,

    /**
     * A type of damage that results from being withered with the wither effect.
     */
    WITHER,
}
