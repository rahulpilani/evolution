class Creature(val position: Position, val genome: Genome) {
    companion object {
        fun randomCreature(position: Position, numberOfGenes: Int): Creature {
            val genes = mutableListOf<Gene>()
            for (i in 0 until numberOfGenes) {
                genes.add(Gene.randomGene())
            }
            return Creature(position, Genome(genes))
        }
    }
    fun newPosition(position: Position): Creature {
        return Creature(position, genome)
    }

    override fun toString(): String {
        return "" + position + "\n" + genome.toString()
    }
}

fun main() {
    for (i in 0 until 100) {
        val creature = Creature.randomCreature(Position.random(128, 128), 8)
        println(creature)
    }
}