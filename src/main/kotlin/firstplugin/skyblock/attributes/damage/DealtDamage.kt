package firstplugin.skyblock.attributes.damage

import firstplugin.skyblock.entity.CombatEntity

class DealtDamage(
    val damageType: DamageType,
    val critHit: Boolean,
    val damageAmt: Double,
    val damageDealer: CombatEntity,
    val damageVictim: CombatEntity,
)
