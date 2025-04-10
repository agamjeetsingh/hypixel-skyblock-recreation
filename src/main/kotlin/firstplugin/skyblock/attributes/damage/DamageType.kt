package firstplugin.skyblock.attributes.damage

sealed class DamageType(val displayName: String) {
    /**
     * A special type of attack, typically from an Item Ability.
     */
    object MAGIC : DamageType("Magic")

    /**
     * A type of damage that ignores the victim's Defense.
     */
    object TRUE : DamageType("True")

    /**
     * A type of damage that results from falling from a high distance.
     */
    object FALL : DamageType("Fall")

    /**
     * A type of damage from either standing in lava or being on fire. Deals True Damage.
     */
    object FIRE : DamageType("Fire")

    /**
     * A type of damage that results from being poisoned with the poison effect.
     */
    object POISON : DamageType("Poison")

    /**
     * A type of damage that results from being withered with the wither effect.
     */
    object WITHER : DamageType("Wither")

    /**
     * A typical melee or ranged attack from a player or enemy.
     */
    sealed class NORMAL(subtype: String) : DamageType("Normal ($subtype)") {
        /**
         * A typical melee attack from a player or enemy.
         */
        object MELEE : NORMAL("Melee")

        /**
         * A ranged attack from a bow or similar weapon.
         */
        object BOW : NORMAL("Bow")

        /**
         * Generic normal damage for backward compatibility.
         */
        object GENERIC : NORMAL("Generic")
    }

    companion object {
        val values = listOf(
            MAGIC, TRUE, FALL, FIRE, POISON, WITHER,
            NORMAL.MELEE, NORMAL.BOW, NORMAL.GENERIC
        )
    }
}
