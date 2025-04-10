package firstplugin.skyblock.minion

import firstplugin.skyblock.minion.MinionHolder.Companion.BONUS_SLOTS_AT_LEVEL
import firstplugin.skyblock.minion.MinionHolder.Companion.DEFAULT_MINION_SLOTS

interface MinionHolder {
    val minionsUnlocked: List<Minion>

    val minionSlots: Int

    fun unlockMinion(minion: Minion): Boolean

    companion object {
        const val DEFAULT_MINION_SLOTS = 5

        val BONUS_SLOTS_AT_LEVEL: List<Int> =
            listOf(
                5,
                15,
                30,
                50,
                75,
                100,
                125,
                150,
                175,
                200,
                225,
                250,
                275,
                300,
                350,
                400,
                450,
                500,
                550,
                600,
                650,
            )
    }
}

class MinionHolderDelegate : MinionHolder {
    private val _minionsUnlocked: MutableList<Minion> = mutableListOf()

    override val minionsUnlocked: List<Minion>
        get() = _minionsUnlocked.toList()

    override var minionSlots: Int = DEFAULT_MINION_SLOTS
        private set

    override fun unlockMinion(minion: Minion): Boolean {
        if (minion in _minionsUnlocked) return false

        _minionsUnlocked.add(minion)
        if (_minionsUnlocked.size in BONUS_SLOTS_AT_LEVEL) {
            minionSlots++
        }
        return true
    }
}
