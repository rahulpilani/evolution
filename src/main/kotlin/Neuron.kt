class Neuron(
    var id: Int,
    val category: Brain.NeuronCategory,
    val incomingEdges: MutableList<Neuron> = mutableListOf(),
    val outgoingEdges: MutableList<Pair<Float, Neuron>> = mutableListOf()
) {
    val initialOutput: Float = 0.5F

    fun driven(): Boolean {
        return incomingEdges.size > 0 &&
                if (incomingEdges.contains(this))
                    incomingEdges.size > 1
                else true
    }

    fun addOutgoing(neuron: Neuron, weight: Float) {
        if (!outgoingEdges.map { e -> e.second }.contains(neuron)) {
            outgoingEdges.add(Pair(weight, neuron))
            neuron.addIncoming(this)
        }
    }

    private fun addIncoming(neuron: Neuron) {
        if (!incomingEdges.contains(neuron)) {
            incomingEdges.add(neuron)
        }
    }

    fun removeOutgoing(neuron: Neuron) {
        val found = outgoingEdges.find { e -> e.second == neuron }
        found.let { f -> outgoingEdges.remove(f) }
    }

    fun removeIncoming(neuron: Neuron) {
        incomingEdges.remove(neuron)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + category.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Neuron

        if (id != other.id) return false
        if (category != other.category) return false

        return true
    }

    fun remap(id: Int) {
        this.id = id
    }

    override fun toString(): String {
        return "Neuron($id, $category, incomingEdges=${incomingEdges.map { v -> v.id }}, outgoingEdges=${outgoingEdges.map { v -> v.second.id }})"
    }

    enum class NeuronType(val category: Brain.NeuronCategory) {
        INTERNAL(Brain.NeuronCategory.INTERNAL),
        SENSE_NORTH(Brain.NeuronCategory.ACTION),
        SENSE_SOUTH(Brain.NeuronCategory.ACTION),
        SENSE_EAST(Brain.NeuronCategory.ACTION),
        SENSE_WEST(Brain.NeuronCategory.ACTION),

    }
}