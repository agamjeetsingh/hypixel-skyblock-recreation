package firstplugin

import kotlinx.serialization.Serializable
import org.bukkit.entity.Player

@Serializable
class ServerPlayer(
    val bukkitPlayer: Player,
    var rank: Rank = Rank.DEFAULT,
) : Player by bukkitPlayer
