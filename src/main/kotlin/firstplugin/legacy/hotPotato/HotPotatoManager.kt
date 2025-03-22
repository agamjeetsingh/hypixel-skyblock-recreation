package firstplugin.legacy.hotPotato

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class HotPotatoManager(
    private val plugin: JavaPlugin,
) {
    // Game states
    enum class GameState {
        LOBBY,
        COUNTDOWN,
        ACTIVE,
        ENDING,
    }

    private var gameState = GameState.LOBBY
    private val minPlayers = 1
    private val maxPlayers = 8
    private val countdownSeconds = 10
    private val hotPotatoTimeSeconds = 15

    private val gamePlayers = mutableSetOf<Player>()
    private var hotPotatoPlayer: Player? = null
    private var hotPotatoTask: BukkitTask? = null
    private var gameTask: BukkitTask? = null

    // Initialize manager
    fun initialize() {
        startLobbyCheck()
    }

    // Player functions
    fun addPlayer(player: Player): Boolean {
        if (gameState != GameState.LOBBY) {
            player.sendMessage(Component.text("Game is already in progress!").color(NamedTextColor.RED))
            return false
        }

        if (gamePlayers.size >= maxPlayers) {
            player.sendMessage(Component.text("Game is full!").color(NamedTextColor.RED))
            return false
        }

        gamePlayers.add(player)
        player.sendMessage(
            Component
                .text(
                    "You joined the game! Current players: ${gamePlayers.size}/$maxPlayers",
                ).color(NamedTextColor.GREEN),
        )

        // Broadcast to all players
        Bukkit.broadcast(
            Component
                .text(
                    "${player.name} joined the game! Current players: ${gamePlayers.size}/$maxPlayers",
                ).color(NamedTextColor.YELLOW),
        )

        return true
    }

    // For testing - normally you'd use a proper mocking framework for this
    fun simulateAddTestPlayer(name: String) {
        // This is just for demo to show how we'd test - a real implementation
        // would create a proper player mock
        Bukkit.broadcast(
            Component
                .text(
                    "TEST: $name joined the game! Current players: ${gamePlayers.size + 1}/$maxPlayers",
                ).color(NamedTextColor.YELLOW),
        )
    }

    fun isPlayerInGame(player: Player): Boolean = gamePlayers.contains(player)

    fun isHotPotatoHolder(player: Player): Boolean = hotPotatoPlayer == player

    fun transferHotPotato(
        from: Player,
        to: Player,
    ) {
        if (from != hotPotatoPlayer || !gamePlayers.contains(to)) return

        hotPotatoPlayer = to

        // Announce the transfer
        Bukkit.broadcast(
            Component
                .text("${to.name} now has the hot potato!")
                .color(NamedTextColor.RED),
        )

        // Reset the timer
        startHotPotatoTimer()
    }

    fun handlePlayerDeath(player: Player) {
        if (!gamePlayers.contains(player)) return

        // Remove from game
        gamePlayers.remove(player)

        // If this was the hot potato player
        if (hotPotatoPlayer == player) {
            hotPotatoPlayer = null

            // If game should continue, pick a new hot potato holder
            if (gamePlayers.size > 1) {
                hotPotatoPlayer = gamePlayers.random()
                Bukkit.broadcast(
                    Component
                        .text(
                            "${player.name} died! ${hotPotatoPlayer?.name} now has the hot potato!",
                        ).color(NamedTextColor.RED),
                )
            } else {
                // End the game if only 0-1 players left
                endGame()
            }
        }

        // Check if game should end
        if (gamePlayers.size <= 1) {
            endGame()
        }
    }

    // Periodically check if there are enough players in the lobby
    private fun startLobbyCheck() {
        // Cancel any existing tasks
        gameTask?.cancel()

        // Schedule a repeating task every 5 seconds to check if we can start the game
        gameTask =
            object : BukkitRunnable() {
                override fun run() {
                    if (gameState == GameState.LOBBY && gamePlayers.size >= minPlayers) {
                        // Start the countdown
                        startCountdown()
                    }
                }
            }.runTaskTimer(plugin, 20L, 5 * 20L) // Initial delay 1 second, then every 5 seconds
    }

    // Start the countdown before the game begins
    private fun startCountdown() {
        if (gameState != GameState.LOBBY) return

        gameState = GameState.COUNTDOWN
        var countdown = countdownSeconds

        // Cancel any existing tasks
        gameTask?.cancel()

        // Start countdown task
        gameTask =
            object : BukkitRunnable() {
                override fun run() {
                    if (gamePlayers.size < minPlayers) {
                        // Not enough players anymore, return to lobby
                        Bukkit.broadcast(
                            Component.text("Not enough players. Countdown cancelled!").color(NamedTextColor.RED),
                        )
                        gameState = GameState.LOBBY
                        startLobbyCheck()
                        this.cancel()
                        return
                    }

                    if (countdown <= 0) {
                        // Countdown complete, start the game
                        Bukkit.broadcast(Component.text("Game starting now!").color(NamedTextColor.GREEN))
                        startGame()
                        this.cancel()
                        return
                    }

                    // Broadcast countdown timer
                    Bukkit.broadcast(
                        Component.text("Game starting in $countdown seconds!").color(NamedTextColor.YELLOW),
                    )
                    countdown--
                }
            }.runTaskTimer(plugin, 0L, 20L) // Initial delay 0 seconds, then every second
    }

    // Start the actual game
    private fun startGame() {
        gameState = GameState.ACTIVE

        // Cancel any existing tasks
        gameTask?.cancel()

        // Choose a random player for the hot potato
        if (gamePlayers.isNotEmpty()) {
            hotPotatoPlayer = gamePlayers.random()
            Bukkit.broadcast(Component.text("${hotPotatoPlayer?.name} has the hot potato!").color(NamedTextColor.RED))

            // Start the hot potato timer
            startHotPotatoTimer()
        }
    }

    // Manage the hot potato timer
    private fun startHotPotatoTimer() {
        // Cancel any existing hot potato task
        hotPotatoTask?.cancel()

        var timeLeft = hotPotatoTimeSeconds

        // Start the timer
        hotPotatoTask =
            object : BukkitRunnable() {
                override fun run() {
                    val currentHolder = hotPotatoPlayer ?: return

                    if (timeLeft <= 0) {
                        // Time's up! The player with the hot potato loses
                        Bukkit.broadcast(
                            Component
                                .text(
                                    "${currentHolder.name} exploded with the hot potato!",
                                ).color(NamedTextColor.RED),
                        )

                        // "Kill" the player with the hot potato
                        currentHolder.health = 0.0

                        // Remove them from the game
                        gamePlayers.remove(currentHolder)
                        hotPotatoPlayer = null

                        // Check if game should end
                        if (gamePlayers.size <= 1) {
                            endGame()
                            this.cancel()
                            return
                        } else {
                            // Choose a new player for hot potato
                            hotPotatoPlayer = gamePlayers.random()
                            Bukkit.broadcast(
                                Component
                                    .text(
                                        "${hotPotatoPlayer?.name} has the hot potato!",
                                    ).color(NamedTextColor.RED),
                            )
                            timeLeft = hotPotatoTimeSeconds
                        }
                    } else {
                        // Countdown notification to the player with hot potato
                        if (timeLeft <= 5 || timeLeft % 5 == 0) {
                            currentHolder.sendMessage(
                                Component
                                    .text(
                                        "You have the hot potato! $timeLeft seconds left!",
                                    ).color(NamedTextColor.RED),
                            )
                        }
                        timeLeft--
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L) // Initial delay 0 seconds, then every second
    }

    // End the game
    private fun endGame() {
        // Cancel all game-related tasks
        gameTask?.cancel()
        hotPotatoTask?.cancel()

        gameState = GameState.ENDING

        // Announce winner if there is one
        if (gamePlayers.size == 1) {
            val winner = gamePlayers.first()
            Bukkit.broadcast(Component.text("${winner.name} wins the game!").color(NamedTextColor.GREEN))
        } else {
            Bukkit.broadcast(Component.text("Game over! No winner.").color(NamedTextColor.YELLOW))
        }

        // Reset game state
        gamePlayers.clear()
        hotPotatoPlayer = null

        // Return to lobby after 5 seconds
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                gameState = GameState.LOBBY
                Bukkit.broadcast(
                    Component.text("Game reset. Use /joingame to join the next game!").color(NamedTextColor.YELLOW),
                )
                startLobbyCheck()
            },
            5 * 20L,
        ) // 5 seconds delay
    }

    // Cleanup resources
    fun shutdown() {
        gameTask?.cancel()
        hotPotatoTask?.cancel()
    }
}

fun main() {
    println("Hi")
}
