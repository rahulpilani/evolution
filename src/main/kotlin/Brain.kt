import kotlin.math.tanh

class Brain(val creature: Creature, val genome: Genome, maxNeurons: Int, maxSenses: Int, maxActions: Int) {
    val nodes = mutableMapOf<Int, Neuron>()

    data class NeuronConfig(val max: Int)
    data class SenseConfig(val max: Int)
    data class ActionConfig(val max: Int)

    val neuronConfig = NeuronConfig(maxNeurons)
    val senseConfig = SenseConfig(maxSenses)
    val actionConfig = ActionConfig(maxActions)
    val connections: MutableList<Triple<Neuron, Float, Neuron>>

    init {
        for (gene in genome.genes) {

            val sourceNeuronNumber = sourceNeuron(gene, neuronConfig, senseConfig)
            val sinkNeuronNumber = sinkNeuron(gene, neuronConfig, actionConfig)

            val sourceNeuron: Neuron =
                nodes.getOrPut(sourceNeuronNumber) { Neuron(sourceNeuronNumber, gene.sourceTypeAsEnum()) }

            val sinkNeuron: Neuron =
                nodes.getOrPut(sinkNeuronNumber) { Neuron(sinkNeuronNumber, gene.sinkTypeAsEnum()) }

            sourceNeuron.addOutgoing(sinkNeuron, gene.weight())
        }

        removeTerminalNeurons()
        remap()
        connections = makeConnections()
    }

    private fun makeConnections(): MutableList<Triple<Neuron, Float, Neuron>> {
        val connections = mutableListOf<Triple<Neuron, Float, Neuron>>()
        for (node in nodes) {
            connections.addAll(node.value.outgoingEdges.map { p -> Triple(node.value, p.first, p.second) })
        }
        connections.sortByDescending { it.third.category }
        return connections
    }

    private fun removeTerminalNeurons() {
        var neuronsToRemove = findTerminalInternalNeurons(nodes)
        while (neuronsToRemove.isNotEmpty()) {
            for (n in neuronsToRemove) {
                    nodes.remove(n.id)
                    nodes.values.forEach { m -> m.removeIncoming(n) }
                    nodes.values.forEach { m -> m.removeOutgoing(n) }
            }
            neuronsToRemove = findTerminalInternalNeurons(nodes)
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

    private fun findTerminalInternalNeurons(nodes: MutableMap<Int, Neuron>): List<Neuron> {
        return nodes.values
            .filter { e -> e.category == Neuron.Category.INTERNAL }
            .filter { e -> e.outgoingEdges.size == 1 }
            .filter { e -> e.hasOutgoingConnectionTo(e) }
    }

    private fun remap() {
        var i = 0
        for (node in nodes) {
            node.value.remap(i++)
        }
    }

    override fun toString(): String {
        return "Brain(nodes=$nodes)"
    }

    fun step(): Map<Neuron, Float> {

        val internalMap = mutableMapOf<Neuron, Float>()

        //First calculate input weights
        for (connection in connections) {
            val input: Float = if (connection.first.category == Neuron.Category.SENSOR) {
                creature.getSensorInput(connection.first.id)
            } else {
                connection.first.output
            }
            val signal = input * connection.second
            if (connection.third.category == Neuron.Category.INTERNAL) {
                internalMap[connection.third] = internalMap.getOrDefault(connection.third, 0.0F) + signal
            }

        }

        //now calculate output of internal neurons
        for (node in internalMap.keys) {
            if (node.driven()) {
                node.output = tanh(internalMap[node]?.toDouble()?:0.0).toFloat()
            }
        }

        //now calculate output of action neurons
        val actionMap = mutableMapOf<Neuron, Float>()
        for (connection in connections) {
            if (connection.third.category == Neuron.Category.ACTION) {
                val input: Float = if (connection.first.category == Neuron.Category.SENSOR) {
                    creature.getSensorInput(connection.first.id)
                } else {
                    connection.first.output
                }
                val signal = input * connection.second
                actionMap[connection.third] = actionMap.getOrDefault(connection.third, 0.0F) + signal
            }
        }
        return actionMap

    }
}

fun main() {
    val genome = Genome(Genome.randomGenes(8))
    val brain = Brain(Creature(Position(0, 0), genome, World(128, 128, 1000)), genome, 5, 5, 5)
    println(brain)
}