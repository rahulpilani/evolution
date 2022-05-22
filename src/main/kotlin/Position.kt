import kotlin.math.floor

class Position(val x: Int, val y:Int, val maxX: Int, val maxY: Int) {
    fun canMoveEast(): Boolean = x < maxX
    fun canMoveNorth(): Boolean = y > 0
    fun canMoveSouth(): Boolean = y < maxY
    fun canMoveWest(): Boolean = x > 0

    fun moveEast(): Position = if (canMoveEast()) Position(x+1, y, maxX, maxY) else this
    fun moveWest(): Position = if (canMoveWest()) Position(x - 1, y, maxX, maxY) else this
    fun moveNorth(): Position = if (canMoveNorth()) Position(x, y - 1, maxX, maxY) else this
    fun moveSouth(): Position = if (canMoveSouth()) Position(x, y + 1, maxX, maxY) else this

    companion object {
        fun random(maxX: Int, maxY: Int): Position {
            return Position(floor(Math.random() * maxX).toInt(), floor(Math.random() * maxY).toInt(), maxX, maxY)
        }
    }
}