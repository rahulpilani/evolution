import java.lang.Math.exp
import java.lang.Math.pow
import java.util.stream.Collectors
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextUInt

class World(val maxX: Int, val maxY: Int, val population: Int) {
    val creatures = mutableMapOf<Position, Creature>()
    var generation = 0
    var step = 0
    val genes = 12
    val mutationProbability = 0.2
    init {
        for (i in 0 until population) {
            var position = Position.random(maxX, maxY)
            while (creatures.containsKey(position)) {
                position = Position.random(maxX, maxY)
            }
            creatures[position] = Creature.randomCreature(position, genes, this)
        }
    }

    fun step() {
        step++
        val moves = creatures.values.parallelStream().map { c -> Pair(c, c.step()) }.collect(Collectors.toList())
        for (move in moves) {
            val (creature, position) = move
            if (position.x >= 0 && position.y >= 0 && position.x < maxX && position.y < maxY) {
                if (!creatures.containsKey(position)) {
                    creatures[position] = creature
                    creatures.remove(creature.position)
                    creature.updatePosition(position)
                }
            }
        }
    }

    fun endOfThisGeneration() {
        val survivors = applySurvivalCriteria(this::surviveCenterCircle)
        val children = spawnNextGeneration(survivors)
        creatures.clear()
        for (child in children) {
            var position = Position.random(maxX, maxY)
            while (creatures.containsKey(position)) {
                position = Position.random(maxX, maxY)
            }
            child.position = position
            creatures[position] = child
        }
    }

    fun surviveRightHalf(p: Position, c: Creature): Triple<Creature, Boolean, Float> {
        return if (p.x > maxX / 2) Triple(c, true, 1.0F) else Triple(c, false, 0.0F)
    }

    fun surviveUpperRightQuadrant(p: Position, c: Creature): Triple<Creature, Boolean, Float> {
        return if (p.x > maxX / 2 && p.y < maxY / 2) Triple(c, true, 1.0F) else Triple(c, false, 0.0F)
    }

    fun surviveCenterCircle(p: Position, c: Creature): Triple<Creature, Boolean, Float> {
        val radius = 10
        val center = Position(maxX / 2, maxY / 2)
        val distance = sqrt(pow(center.x.toDouble() - p.x.toDouble(), 2.0) + pow(center.y.toDouble() - p.y.toDouble(), 2.0))
        return if (distance <= radius) Triple(c, true, 1.0F) else Triple(c, false, 0.0F)
    }

    fun applySurvivalCriteria(criteria: (Position, Creature) -> Triple<Creature, Boolean, Float>): MutableList<Triple<Creature, Boolean, Float>> {
        return creatures.map { Pair(it.key, it.value) }.parallelStream().map { criteria.invoke(it.first, it.second) }
                .filter { it.second }
                .collect(Collectors.toList())
    }

    private fun spawnNextGeneration(survivors: List<Triple<Creature, Boolean, Float>>): List<Creature> {
        var count = 0
        val children = mutableListOf<Creature>()
        while (count < population) {
            val parent1 = survivors[Random.nextInt(survivors.size)]
            val parent2 = survivors[Random.nextInt(survivors.size)]
            val parent1Genes = parent1.first.genome.genes.toMutableList()
            val parent2Genes = parent2.first.genome.genes.toMutableList()
            val childGenes = mutableListOf<Gene>()

            var geneCount = 0
            while (geneCount < genes) {
                val r = Random.nextInt(2)
                val gene = if (r == 1) parent1Genes.removeAt(Random.nextInt(parent1Genes.size)) else parent2Genes.removeAt(Random.nextInt(parent2Genes.size))
                childGenes.add(gene)

                geneCount++
            }

            geneCount = 0
            while (geneCount < genes) {
                randomBitFlip(childGenes)
                geneCount++
            }
            children.add(Creature(Position(0,0), Genome(childGenes), this))
            count ++
        }
        return children
    }

    fun randomBitFlip(genes: MutableList<Gene>): Genome {
        val nextInt = Random.nextInt(genes.size)
        if ((Random.nextUInt().toDouble() / UInt.MAX_VALUE.toDouble()) < mutationProbability) {
            val gene = Gene(genes[nextInt].gene xor (1u shl (Random.nextInt(32))))
            genes[nextInt] = gene
        }
        return Genome(genes)
    }
}