import Brain.Connection
import Neuron.Category
import kotlin.math.roundToInt

class GenomeGenerator {

    companion object {
        fun generateGenome(connections: List<Connection>): Genome {
            return Genome(connections.map(this::connectionToGene).toList())
        }

        fun connectionToGene(connection: Connection): Gene {
            val sourceType = if (connection.source.category == Category.SENSOR) 1 else 0
            val sourceId = connection.source.id
            val sinkType = if (connection.sink.category == Category.ACTION) 1 else 0
            val sinkId = connection.sink.id
            val weight = (connection.weight * 8192.0).roundToInt()

            val gene = Bitwise.orTogether(
                sourceType.toUInt() shl 31,
                sourceId.toUInt() and 0x7FFFu shl 24,
                sinkType.toUInt() shl 23,
                sinkId.toUInt() and 0x7FFFu shl 16,
                weight.toUInt() and 0xFFFFu
            )

            return Gene(gene)
        }
    }
}

fun main() {
    val connections = listOf(
        Connection(Neuron(0, Category.INTERNAL), -1.0F, Neuron(2, Category.ACTION)),
    )
    val genome = GenomeGenerator.generateGenome(connections)
    println(genome)
}