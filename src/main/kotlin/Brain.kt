class Brain(val genome: Genome, maxNeurons: Int, maxSenses: Int, maxActions: Int) {
    val nodes = mutableMapOf<Int, Neuron>()

    data class NeuronConfig(val max: Int)
    data class SenseConfig(val max: Int)
    data class ActionConfig(val max: Int)

    val neuronConfig = NeuronConfig(maxNeurons)
    val senseConfig = SenseConfig(maxSenses)
    val actionConfig = ActionConfig(maxActions)

    enum class NeuronCategory(val id: Int) {
        INTERNAL(0), ACTION(1);

        companion object {
            fun getByValue(value: Int) = values().firstOrNull { it.id == value }
        }
    }

    init {
        for (gene in genome.genes) {

            val sourceNeuronNumber = sourceNeuron(gene, neuronConfig, senseConfig)
            val sinkNeuronNumber = sinkNeuron(gene, neuronConfig, actionConfig)

            val sourceNeuron: Neuron =
                nodes.getOrPut(sourceNeuronNumber) { Neuron(sourceNeuronNumber, gene.sourceTypeAsEnum()) }

            val sinkNeuron: Neuron =
                nodes.getOrPut(sinkNeuronNumber) { Neuron(sinkNeuronNumber, gene.sourceTypeAsEnum()) }

            sourceNeuron.addOutgoing(sinkNeuron)
        }

        removeTerminalNeurons()
        remap()
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
        if (gene.sourceTypeAsEnum() == NeuronCategory.INTERNAL) {
            return gene.sinkId() % neuronConfig.max
        } else {
            return gene.sinkId() % actionConfig.max
        }
    }

    private fun sourceNeuron(gene: Gene, neuronConfig: NeuronConfig, senseConfig: SenseConfig): Int {
        if (gene.sinkTypeAsEnum() == NeuronCategory.INTERNAL) {
            return gene.sourceId() % neuronConfig.max
        } else {
            return gene.sourceId() % senseConfig.max
        }
    }

    private fun findTerminalInternalNeurons(nodes: MutableMap<Int, Neuron>): List<Neuron> {
        return nodes.values
            .filter { e -> e.category == NeuronCategory.INTERNAL }
            .filter { e -> e.outgoingEdges.size == 1 }
            .filter { e -> e.outgoingEdges.contains(e) }

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
}

fun main() {
    val genome = Genome(Genome.randomGenes(8))
    val brain = Brain(genome, 5, 5, 5)
    println(brain)
}