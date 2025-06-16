package firstplugin.skyblock.economy

class BankAccount(
    val owner: MoneyHolder,
) {
    var maxBalance: Long = BankAccountTier.STARTER.maxBalance
        private set

    val balance = Coins(maxBalance)

    fun deposit(amount: Long): Long = balance.add(amount)

    fun withdraw(amount: Long): Boolean = balance.remove(amount)

    var tier: BankAccountTier = BankAccountTier.STARTER
        private set

    fun upgradeAccount(): Boolean {
        // Check if already at max tier
        if (tier.ordinal >= BankAccountTier.entries.size - 1) {
            return false
        }

        val nextTier = BankAccountTier.entries[tier.ordinal + 1]

        tier = nextTier
        maxBalance = tier.maxBalance

        // Update the coins maxBalance
        balance.set(balance.amount)

        return true
    }

    fun applyInterest(): Long {
        val currentBalance = balance.amount
        val interestAmount = tier.interestFunction(currentBalance)
        return balance.add(interestAmount)
    }
}
