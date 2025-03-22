package firstplugin.legacy.minigames

import firstplugin.ServerPlayer
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Game(
    val plugin: Plugin,
    val world: World,
) {
    enum class GamePlayerState {
        PRE_GAME,
        IN_GAME,
        POST_GAME,
        ADMIN_SPECTATOR,
    }

    enum class GamePlayerOutcome {
        VICTORY,
        LOSS,
        NOT_DECIDED_YET,
    }

    inner class GamePlayer(
        private val player: ServerPlayer,
    ) : Player by player {
        var outcome: GamePlayerOutcome = GamePlayerOutcome.NOT_DECIDED_YET
            private set

        var state = GamePlayerState.PRE_GAME
            private set

        fun joinGame() {
            if (state == GamePlayerState.PRE_GAME) {
                state = GamePlayerState.IN_GAME
            }
        }

        fun lost() {
            if (state == GamePlayerState.IN_GAME) {
                outcome = GamePlayerOutcome.LOSS
                state = GamePlayerState.POST_GAME
            }
        }

        fun won() {
            if (state == GamePlayerState.IN_GAME) {
                outcome = GamePlayerOutcome.VICTORY
                state = GamePlayerState.POST_GAME
            }
        }
    }

    private val _players = mutableListOf<GamePlayer>()
    private val _winners = mutableListOf<GamePlayer>()

    val players: List<GamePlayer>
        get() = _players.toList()
    val winners: List<GamePlayer>
        get() = _winners.toList()

    enum class GameState {
        PRE_GAME,
        IN_GAME,
        POST_GAME,
    }

    var gameState = GameState.PRE_GAME
        private set

    /**
     * Advances the game to the next stage.
     *
     * @throws IllegalStateException if the game state is `POST_GAME`.
     */
    fun advance() {
        when (gameState) {
            GameState.PRE_GAME -> {
                gameState = GameState.IN_GAME
                _players.forEach { it.joinGame() }
            }
            GameState.IN_GAME -> {
                gameState = GameState.POST_GAME
                _players.forEach {
                    if (it.outcome == GamePlayerOutcome.NOT_DECIDED_YET) {
                        it.won()
                    }
                }
            }
            GameState.POST_GAME -> {
                throw IllegalStateException("Cannot advance game when it has already ended.")
            }
        }
    }

    /**
     * Adds the player to the game.
     *
     * @param player need to wrap a player with ServerPlayer
     *
     * @see ServerPlayer
     */
    fun addPlayer(player: ServerPlayer) {
        _players.add(GamePlayer(player))
    }

    /**
     * Removes the player from the game if they are in the game.
     *
     * @return true if the player was successfully removed or false if the player was not in the game.
     */
    fun removePlayer(player: ServerPlayer): Boolean = _players.removeIf { it.uniqueId == player.uniqueId }
}
