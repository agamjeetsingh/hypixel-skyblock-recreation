package firstplugin.skyblock.economy

import kotlinx.serialization.Serializable
import kotlin.math.floor

@Serializable
class Coins(
    val maxBalance: Long = Long.MAX_VALUE,
) {
    var amount: Long = 0
        private set(value) {
            field = value.coerceIn(0, maxBalance)
        }

    fun add(value: Long): Long {
        if (value <= 0) return amount

        // Check if addition would exceed maxBalance
        val newAmount = (amount + value).coerceAtMost(maxBalance)
        amount = newAmount
        return amount
    }

    fun remove(value: Long): Boolean {
        if (value <= 0) return false
        if (amount < value) return false

        amount -= value
        return true
    }

    fun canAfford(value: Long): Boolean = amount >= value

    fun reset() {
        amount = 0
    }

    fun set(value: Long) {
        amount = value.coerceIn(0, maxBalance)
    }

    fun remainingCapacity(): Long = maxBalance - amount

    fun isFull(): Boolean = amount >= maxBalance

    override fun toString(): String = format(amount)

    companion object {
        fun format(amount: Long): String =
            when {
                amount >= 1_000_000_000_000L -> String.format("%.1fT", floor(amount / 1_000_000_000_000.0 * 10) / 10)
                amount >= 1_000_000_000L -> String.format("%.1fB", floor(amount / 1_000_000_000.0 * 10) / 10)
                amount >= 1_000_000L -> String.format("%.1fM", floor(amount / 1_000_000.0 * 10) / 10)
                amount >= 1_000L -> String.format("%.1fK", floor(amount / 1_000.0 * 10) / 10)
                else -> amount.toString()
            }
    }
}
