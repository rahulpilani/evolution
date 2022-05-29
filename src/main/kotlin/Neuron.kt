class Neuron(
    var id: Int,
    val category: Category,
    val incomingEdges: MutableList<Neuron> = mutableListOf(),
    val outgoingEdges: MutableList<Pair<Float, Neuron>> = mutableListOf()
) {
    val initialOutput: Float = 0.5F
    var output = initialOutput

    fun driven(): Boolean {
        return incomingEdges.size > 0 &&
                if (incomingEdges.contains(this))
                    incomingEdges.size > 1
                else true
    }

    fun hasOutgoingConnectionTo(e: Neuron) = e.outgoingEdges.map { m -> m.second }.contains(e)

    fun addOutgoing(neuron: Neuron, weight: Float) {
        if (!hasOutgoingConnectionTo(neuron)) {
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
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Neuron

        if (id != other.id) return false

        return true
    }

    fun remap(id: Int) {
        this.id = id
    }

    override fun toString(): String {
        return "Neuron($id, $category, incomingEdges=${incomingEdges.map { v -> v.id }}, outgoingEdges=${outgoingEdges.map { v -> v.second.id }})"
    }

    enum class Category {
        SENSOR, ACTION, INTERNAL;

        companion object {
            fun getByValue(value: Int, source: Boolean): Category {
                if (value == 1) {
                    if (source) {
                        return SENSOR
                    } else {
                        return ACTION
                    }
                } else {
                    return INTERNAL
                }
            }
        }
    }


}