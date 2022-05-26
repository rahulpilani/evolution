class Connection(val gene: Gene) {

    val source: Int
    val sourceType: Neuron.Type
    val sink: Int
    val sinkType: Neuron.Type

    init {

        sourceType = gene.sourceTypeAsEnum()
        sinkType = gene.sinkTypeAsEnum()
        if (sourceType == Neuron.Type.NEURON) {
            source = gene.sourceId() % Neuron.MAX_NEURONS
        } else {
            source = gene.sourceId() % Sense.MAX_SENSES
        }

        if (sinkType == Neuron.Type.NEURON) {
            sink = gene.sinkId() % Neuron.MAX_NEURONS
        } else {
            sink = gene.sinkId() % Action.MAX_ACTIONS
        }
    }
}