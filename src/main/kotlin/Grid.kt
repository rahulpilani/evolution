class Grid(val maxX: Int, val maxY: Int, val population: Int) {
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
}