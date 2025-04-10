package firstplugin.skyblock.economy

interface MoneyHolder {
    val purse: Coins

    val bankAccount: BankAccount

    fun add(value: Long): Long = purse.add(value)

    fun remove(value: Long): Boolean = purse.remove(value)

    fun canAfford(value: Long): Boolean = purse.canAfford(value)

    fun reset() {
        purse.reset()
    }

    fun set(value: Long) {
        purse.set(value)
    }

    fun getAmount(): Long = purse.amount

    fun transferTo(
        target: MoneyHolder,
        amount: Long,
    ): Boolean {
        if (!canAfford(amount)) return false

        remove(amount)
        target.add(amount)
        return true
    }

    fun formatBalance(): String = Coins.format(getAmount())
}
