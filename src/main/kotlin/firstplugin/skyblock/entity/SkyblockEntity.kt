package firstplugin.skyblock.entity

import kotlinx.serialization.Serializable
import org.bukkit.entity.Entity

@Serializable
abstract class SkyblockEntity(
    open val _entity: Entity?,
)
