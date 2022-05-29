class World(val maxX: Int, val maxY: Int, val population: Int) {
    val creatures = mutableMapOf<Position, Creature>()
    var generation = 0
    var step = 0

    init {
        for (i in 0 until population) {
            var position = Position.random(maxX, maxY)
            while (creatures.containsKey(position)) {
                position = Position.random(maxX, maxY)
            }
            creatures[position] = Creature.randomCreature(position, 2, this)
        }
    }

    fun step() {
        step++
        for (creature in creatures) {
            creature.value.step()
        }
    }
}