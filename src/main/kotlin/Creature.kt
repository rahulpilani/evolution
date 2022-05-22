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

    

}