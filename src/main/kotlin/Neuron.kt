class Neuron(val sourceNum: Int, val sinkNum: Int, val weight: Float) {
    enum class Type(val id: Int) {
        NEURON(0), ACTION(1);
        companion object {
            fun getByValue(value: Int) = values().firstOrNull { it.id == value }
        }
    }

    companion object {
        val MAX_NEURONS = 5
    }
}