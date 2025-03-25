package firstplugin.skyblock.attributes.damage

import firstplugin.skyblock.SkyblockPlayer

class DealtDamage(
    val damageType: DamageType,
    val critHit: Boolean,
    val damageAmt: Double,
    val damageDealer: SkyblockPlayer, // TODO - Could be a NPC or any other entity too
    val damageVictim: SkyblockPlayer,
)
