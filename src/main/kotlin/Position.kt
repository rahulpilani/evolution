import kotlin.math.floor

class Position(val x: Int, val y:Int) {

    companion object {
        fun random(maxX: Int, maxY: Int): Position {
            return Position(floor(Math.random() * maxX).toInt(), floor(Math.random() * maxY).toInt())
        }
    }
}