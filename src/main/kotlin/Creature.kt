class Creature(val position: Position, val genome: Genome) {
    val brain: Brain = Brain(genome, 6, 6, 6)

    companion object {
        fun randomCreature(position: Position, numberOfGenes: Int): Creature {
            return Creature(position, Genome(Genome.randomGenes(numberOfGenes)))
        }
    }

    fun newPosition(position: Position): Creature {
        return Creature(position, genome)
    }

    override fun toString(): String {
        return "" + position + "\n" + genome + "\n" + brain
    }
}

fun main() {
    for (i in 0 until 100) {
        val creature = Creature.randomCreature(Position.random(128, 128), 8)
        println(creature)
    }
}