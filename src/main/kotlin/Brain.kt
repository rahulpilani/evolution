import kotlin.math.tanh

class Brain(private val sensorInputProvider: SensorInputProvider, private val connectionProvider: ConnectionProvider) {

    data class Connection(val source: Neuron, val weight: Float, val sink: Neuron)

    override fun toString(): String {
        return "Brain(connections=${connectionProvider.getConnections()})"
    }

    fun step(): Map<Int, Float> {
        val connections = connectionProvider.getConnections()
        val internalMap = mutableMapOf<Neuron, Float>()

        //First calculate input weights
        for (connection in connections) {
            val input: Float = if (connection.source.category == Neuron.Category.SENSOR) {
                sensorInputProvider.getSensorInput(connection.source.id)
            } else {
                connection.source.output
            }
            val signal = input * connection.weight
            if (connection.sink.category == Neuron.Category.INTERNAL) {
                internalMap[connection.sink] = internalMap.getOrDefault(connection.sink, 0.0F) + signal
            }

        }

        //now calculate output of internal neurons
        for (node in internalMap.keys) {
            if (node.driven) {
                node.output = tanh(internalMap[node]?.toDouble()?:0.0).toFloat()
            }
        }

        //now calculate output of action neurons
        val actionMap = mutableMapOf<Int, Float>()
        for (connection in connections) {
            if (connection.sink.category == Neuron.Category.ACTION) {
                val input: Float = if (connection.source.category == Neuron.Category.SENSOR) {
                    sensorInputProvider.getSensorInput(connection.source.id)
                } else {
                    connection.source.output
                }
                val signal = input * connection.weight
                actionMap[connection.sink.id] = actionMap.getOrDefault(connection.sink.id, 0.0F) + signal
            }
        }
        return actionMap

    }
}

fun main() {

}