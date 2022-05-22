class Creature(val position: Position) {

    fun newPosition(position: Position): Creature {
        return Creature(position)
    }
}