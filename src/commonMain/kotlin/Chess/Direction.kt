package Chess

sealed class Direction(open val inc: Int) {
    data class NORTH(override val inc: Int = -8) : Direction(inc)
    data class SOUTH(override val inc: Int = 8) : Direction(inc)
    data class WEST(override val inc: Int = -1) : Direction(inc)
    data class EAST(override val inc: Int = 1) : Direction(inc)
    data class NORTH_WEST(override val inc: Int = -9) : Direction(inc)
    data class SOUTH_EAST(override val inc: Int = 9) : Direction(inc)
    data class NORTH_EAST(override val inc: Int = -7) : Direction(inc)
    data class SOUTH_WEST(override val inc: Int = 7) : Direction(inc)

    fun isEastward() = this is EAST || this is NORTH_EAST || this is SOUTH_EAST

    fun isWestward() = this is WEST || this is NORTH_WEST || this is SOUTH_WEST

    fun isNorthward() = this is NORTH || this is NORTH_EAST || this is NORTH_WEST

    fun isSouthward() = this is SOUTH || this is SOUTH_WEST || this is SOUTH_EAST
}