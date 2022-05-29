import kotlin.math.min

fun interface SensorInputProvider {
    fun getSensorInput(id: Int): Float
}

class Creature(val position: Position, val genome: Genome, val world: World): SensorInputProvider {
    var lastPosition: Position = position
    val sensorMap: Map<Int, () -> Float> = mapOf(
        Pair(0, this::xLocation),
        Pair(1, this::yLocation),
        Pair(2, this::xDistanceToBoundary),
        Pair(3, this::distanceToBoundary),
        Pair(4, this::lastMoveX),
        Pair(5, this::lastMoveY),
    )

    val brain: Brain = Brain(this, GenomeToConnectionProvider(genome, 6, sensorMap.size, 6))

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

    fun step() {
        val actionValues = brain.step()

    }

    override fun getSensorInput(id: Int): Float {
        val sensor = sensorMap[id]
        return sensor?.invoke() ?: 0.0F
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