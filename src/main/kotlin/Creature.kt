import kotlin.math.abs
import kotlin.math.min
import kotlin.math.tanh
import kotlin.random.Random

fun interface SensorInputProvider {
    fun getSensorInput(id: Int): Float
}

class Creature(var position: Position, val genome: Genome, val world: World): SensorInputProvider {
    var lastPosition: Position = position
    val sensorMap: List<() -> Float> = listOf(
        this::xLocation,
        this::yLocation,
        this::xDistanceToBoundary,
        this::yDistanceToBoundary,
        this::xDistanceToCenter,
        this::yDistanceToCenter,
        this::distanceToBoundary,
        this::lastMoveX,
        this::lastMoveY,
    )
    enum class ActionCategory {
        MOVE
    }

    fun updatePosition(newPosition: Position) {
        lastPosition = position
        position = newPosition
    }

    enum class Actions {
        MOVE_X,
        MOVE_Y,
        MOVE_EAST,
        MOVE_WEST,
        MOVE_NORTH,
        MOVE_SOUTH,
        MOVE_RANDOM
    }


    val brain: Brain = Brain(this, GenomeToConnectionProvider(genome, 3, sensorMap.size, Actions.values().size))

    companion object {
        fun randomCreature(position: Position, numberOfGenes: Int, world: World): Creature {
            return Creature(position, Genome(Genome.randomGenes(numberOfGenes)), world)
        }
    }

    fun newPosition(position: Position): Creature {
        return Creature(position, genome, world)
    }

    override fun toString(): String {
        return "" + position + "\n" + genome + "\n" + brain
    }

    fun prob2Bool(absMove: Float): Int {
        return if (Random.nextInt().toUInt().toFloat() / UInt.MAX_VALUE.toFloat() < absMove) 1 else 0
    }
    fun step(): Position {
        val actionValues = brain.step()
        var moveX = actionValues[Actions.MOVE_X.ordinal]?: 0.0F
        var moveY = actionValues[Actions.MOVE_Y.ordinal]?: 0.0F
        moveX += actionValues[Actions.MOVE_EAST.ordinal]?: 0.0F
        moveX -= actionValues[Actions.MOVE_WEST.ordinal]?: 0.0F
        moveY -= actionValues[Actions.MOVE_NORTH.ordinal]?: 0.0F
        moveY += actionValues[Actions.MOVE_SOUTH.ordinal]?: 0.0F

        val level = actionValues[Actions.MOVE_RANDOM.ordinal]?: 0.0F
        val randomX = Random.nextInt(3) - 1
        val randomY = Random.nextInt(3) - 1
        moveX += randomX * level
        moveY += randomY * level

        moveX = tanh(moveX)
        moveY = tanh(moveY)

        val probX = prob2Bool(abs(moveX))
        val probY = prob2Bool(abs(moveY))

        val signX = if (moveX < 0) -1 else 1
        val signY = if (moveY < 0) -1 else 1

        val deltaX = probX * signX
        val deltaY = probY * signY

        return Position(position.x + deltaX, position.y + deltaY)

    }

    override fun getSensorInput(id: Int): Float {
        val sensor = sensorMap[id]
        return sensor.invoke()
    }

    private fun lastMoveX(): Float {
        val x = position.x - lastPosition.x
        return when {
            x > 0 -> 1.0F
            x < 0 -> 0.0F
            else -> 0.5F
        }
    }

    private fun lastMoveY(): Float {
        val y = position.y - lastPosition.y
        return when {
            y > 0 -> 1.0F
            y < 0 -> 0.0F
            else -> 0.5F
        }
    }

    private fun xDistanceToBoundary(): Float {
        val minDistanceX = min(position.x, world.maxX - position.x)
        return (minDistanceX * 1.0F) / (world.maxX / 2.0F)
    }

    private fun yDistanceToBoundary(): Float {
        val minDistanceY = min(position.y, world.maxY - position.y)
        return (minDistanceY * 1.0F) / (world.maxY / 2.0F)
    }

    private fun xDistanceToCenter(): Float {
        val distanceX = (world.maxX / 2) - position.x
        return distanceX * 1.0F / (world.maxX / 2.0F)
    }

    private fun yDistanceToCenter(): Float {
        val distanceY = (world.maxY / 2) - position.y
        return distanceY * 1.0F / (world.maxY / 2.0F)
    }

    private fun distanceToBoundary(): Float {
        return min(xDistanceToBoundary(), yDistanceToBoundary())
    }

    private fun xLocation(): Float {
        return position.x * 1.0F / world.maxX
    }

    private fun yLocation(): Float {
        return position.y * 1.0F / world.maxY
    }

}

fun main() {
    for (i in 0 until 100) {
        val creature = Creature.randomCreature(Position.random(128, 128), 8, World(128, 128, 5000))
        println(creature)
    }
}