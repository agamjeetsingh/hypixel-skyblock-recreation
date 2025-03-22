package firstplugin.legacy.hotPotato

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

class HotPotatoListener(
    private val manager: HotPotatoManager,
) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player

        // If player is in game, handle their death
        if (manager.isPlayerInGame(player)) {
            manager.handlePlayerDeath(player)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        // If player is in game, handle as if they died
        if (manager.isPlayerInGame(player)) {
            manager.handlePlayerDeath(player)
        }
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        // Check if this is player-to-player combat
        if (event.damager !is Player || event.entity !is Player) return

        val attacker = event.damager as Player
        val victim = event.entity as Player

        // Check if victim has the hot potato
        if (manager.isPlayerInGame(attacker) &&
            manager.isPlayerInGame(victim) &&
            manager.isHotPotatoHolder(victim)
        ) {
            // Transfer the hot potato
            manager.transferHotPotato(victim, attacker)

            // Cancel the damage (optional)
            event.isCancelled = true
        }
    }
}
