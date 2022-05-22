class World(val maxX: Int, val maxY: Int, val population: Int) {
    val creatures = mutableMapOf<Position, Creature>()

    init {
        for (i in 0 until population) {
            var position = Position.random(maxX, maxY)
            while (creatures.containsKey(position)) {
                position = Position.random(maxX, maxY)
            }
            creatures[position] = Creature(position)
        }
    }

    fun canMoveEast(creature: Creature): Boolean =
        creature.position.x < maxX && !creatures.containsKey(Position(creature.position.x + 1, creature.position.y))

    fun canMoveWest(creature: Creature): Boolean =
        creature.position.x > 0 && !creatures.containsKey(Position(creature.position.x - 1, creature.position.y))

    fun canMoveSouth(creature: Creature): Boolean =
        creature.position.y < maxY && !creatures.containsKey(Position(creature.position.x, creature.position.y + 1))

    fun canMoveNorth(creature: Creature): Boolean =
        creature.position.y > 0 && !creatures.containsKey(Position(creature.position.x, creature.position.y - 1))

    fun moveEast(creature: Creature) {
        val newPosition = if (canMoveEast(creature)) Position(creature.position.x + 1, creature.position.y) else creature.position
        if (creature.position != newPosition) {
            creatures.remove(creature.position)
            creatures[newPosition] = creature.newPosition(newPosition)
        }
    }


    fun moveWest(creature: Creature): Position =
        if (canMoveWest(creature)) Position(creature.position.x - 1, creature.position.y) else creature.position

    fun moveNorth(creature: Creature): Position =
        if (canMoveNorth(creature)) Position(creature.position.x, creature.position.y - 1) else creature.position

    fun moveSouth(creature: Creature): Position =
        if (canMoveSouth(creature)) Position(creature.position.x, creature.position.y + 1) else creature.position
}