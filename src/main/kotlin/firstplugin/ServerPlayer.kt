package firstplugin

import org.bukkit.entity.Player

class ServerPlayer(
    val bukkitPlayer: Player,
    var rank: Rank = Rank.DEFAULT,
) : Player by bukkitPlayer
