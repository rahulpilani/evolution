fun interface ConnectionProvider {
    fun getConnections(): List<Brain.Connection>
}

class GenomeToConnectionProvider(genome: Genome, maxNeurons: Int, maxSenses: Int, maxActions: Int) :
    ConnectionProvider {
    data class NeuronConfig(val max: Int)
    data class SenseConfig(val max: Int)
    data class ActionConfig(val max: Int)

    private val neuronConfig = NeuronConfig(maxNeurons)
    private val senseConfig = SenseConfig(maxSenses)
    private val actionConfig = ActionConfig(maxActions)
    private val connectionList: MutableList<Brain.Connection>

    init {
        val nodes = mutableMapOf<Pair<Neuron.Category, Int>, Neuron>()
        val connections = mutableListOf<Brain.Connection>()
        for (gene in genome.genes) {

            val sourceNeuronNumber = sourceNeuron(gene, neuronConfig, senseConfig)
            val sinkNeuronNumber = sinkNeuron(gene, neuronConfig, actionConfig)

            val sourceNeuron: Neuron =
                nodes.getOrPut(Pair(gene.sourceTypeAsEnum(), sourceNeuronNumber)) {
                    Neuron(
                        sourceNeuronNumber,
                        gene.sourceTypeAsEnum()
                    )
                }

            val sinkNeuron: Neuron =
                nodes.getOrPut((Pair(gene.sinkTypeAsEnum(), sinkNeuronNumber))) {
                    Neuron(
                        sinkNeuronNumber,
                        gene.sinkTypeAsEnum()
                    )
                }

            connections.add(Brain.Connection(sourceNeuron, gene.weight(), sinkNeuron))
        }

        removeTerminalNeurons(connections)
        remapInternal(connections)
        markNotDriven(connections)
        connectionList = connections

    }

    private fun markNotDriven(connections: MutableList<Brain.Connection>) {
        val nodes = internalNeurons(connections)
        val incomingConnections = mutableMapOf<Neuron, List<Neuron>>()
        for (node in nodes) {
            val sources = connections.filter { it.sink == node }
                .map { it.source }
                .distinct()
            incomingConnections[node] = sources
        }

        incomingConnections
            .filter { it.value.isEmpty() || (it.value.size == 1 && it.value.contains(it.key)) }
            .forEach { it.key.driven = false }

    }

    private fun removeTerminalNeurons(connections: MutableList<Brain.Connection>) {
        var neuronsToRemove = findTerminalInternalNeurons(connections)
        while (neuronsToRemove.isNotEmpty()) {
            for (n in neuronsToRemove) {
                connections.removeIf { it.source == n || it.sink == n }
            }
            neuronsToRemove = findTerminalInternalNeurons(connections)
        }
    }

    private fun sinkNeuron(gene: Gene, neuronConfig: NeuronConfig, actionConfig: ActionConfig): Int {
        return if (gene.sinkTypeAsEnum() == Neuron.Category.INTERNAL) {
            gene.sinkId() % neuronConfig.max
        } else {
            gene.sinkId() % actionConfig.max
        }
    }

    private fun sourceNeuron(gene: Gene, neuronConfig: NeuronConfig, senseConfig: SenseConfig): Int {
        return if (gene.sourceTypeAsEnum() == Neuron.Category.INTERNAL) {
            gene.sourceId() % neuronConfig.max
        } else {
            gene.sourceId() % senseConfig.max
        }
    }

    private fun findTerminalInternalNeurons(connections: MutableList<Brain.Connection>): List<Neuron> {
        val internalNeurons = internalNeurons(connections)

        val terminalNeurons = mutableListOf<Neuron>()
        for (i in internalNeurons) {
            val outputSet = connections.filter { it.source == i }
                .map { it.sink }
                .distinct()
            if (outputSet.isEmpty() || (outputSet.size == 1 && outputSet.contains(i))) {
                terminalNeurons.add(i)
            }
        }

        return terminalNeurons
    }

    private fun internalNeurons(connections: MutableList<Brain.Connection>) = connections
        .map { listOf(it.source, it.sink) }
        .flatten()
        .filter { it.category == Neuron.Category.INTERNAL }
        .distinct()

    private fun remapInternal(connections: MutableList<Brain.Connection>) {
        val nodes = internalNeurons(connections).distinct()
        var i = 0
        for (node in nodes) {
            node.remap(i++)
        }
    }

    override fun getConnections(): List<Brain.Connection> {
        return connectionList
    }

    fun toGraphViz(): String {
        val nodes = connectionList
            .map { listOf(it.source, it.sink) }
            .flatten()
            .distinct()
        val colors = mapOf(
            Pair(Neuron.Category.SENSOR, "blue"),
            Pair(Neuron.Category.ACTION, "green"),
            Pair(Neuron.Category.INTERNAL, "yellow"),
        )
        val nodeDeclaration = nodes
            .map { "${it.category.toString().first()}${it.id}[label=\"${it.id}\", color=\"black\", fillcolor=\"${colors[it.category]}\"]"}
            .joinToString(separator = "\n")
        val edges = connectionList
            .map { "${it.source.category.toString().first()}${it.source.id}->${it.sink.category.toString().first()}${it.sink.id}" }
            .joinToString(separator = "\n")

        return """ 
        digraph neural_net {
            node[shape=circle, style=filled];
            $nodeDeclaration
            
            $edges
       }
        """.trimIndent()
    }
}

fun main() {
    val genome = Genome(Genome.randomGenes(100))
    val provider = GenomeToConnectionProvider(genome, 10, 7, 7)
    println(provider.toGraphViz())
}