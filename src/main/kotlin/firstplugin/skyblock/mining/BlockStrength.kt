package firstplugin.skyblock.mining

class BlockStrength(
    blockStrength: Int,
) : Comparable<BlockStrength> {
    val blockStrength = 0.coerceAtLeast(blockStrength)

    override fun compareTo(other: BlockStrength): Int = this.blockStrength - other.blockStrength

    override fun equals(other: Any?): Boolean = (this.blockStrength == (other as? BlockStrength)?.blockStrength)

    override fun hashCode(): Int = blockStrength * 37
}
