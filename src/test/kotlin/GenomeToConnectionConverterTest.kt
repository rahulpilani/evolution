import Brain.Connection
import Neuron.Category
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GenomeToConnectionConverterTest {

    @Test
    fun testConnectionGeneration() {
        val connections = listOf(
            Connection(Neuron(3, Category.INTERNAL), 4.0F, Neuron(3, Category.INTERNAL)),
            Connection(Neuron(3, Category.INTERNAL), 4.0F, Neuron(2, Category.INTERNAL)),
            Connection(Neuron(2, Category.INTERNAL), 4.0F, Neuron(4, Category.INTERNAL)),
            Connection(Neuron(4, Category.SENSOR), 3.0F, Neuron(1, Category.INTERNAL)),
            Connection(Neuron(4, Category.SENSOR), 3.0F, Neuron(3, Category.INTERNAL)),
            Connection(Neuron(1, Category.INTERNAL), 2.0F, Neuron(2, Category.ACTION)),
            Connection(Neuron(1, Category.INTERNAL), 2.0F, Neuron(3, Category.INTERNAL)),
            Connection(Neuron(1, Category.INTERNAL), 1.0F, Neuron(1, Category.INTERNAL)),
            Connection(Neuron(0, Category.INTERNAL), -1.0F, Neuron(2, Category.ACTION)),
        )
        val genome = GenomeGenerator.generateGenome(connections)
        val converter = GenomeToConnectionProvider(genome, 5, 5, 5)
        val expected = listOf(
            Connection(
                source = Neuron(4, Category.SENSOR, true), weight = 3.0F, sink = Neuron(
                    0,
                    Category.INTERNAL, true
                )
            ), Connection(
                source = Neuron(
                    0,
                    Category.INTERNAL, true
                ), weight = 2.0F, sink = Neuron(
                    2,
                    Category.ACTION, true
                )
            ), Connection(
                source = Neuron(
                    0,
                    Category.INTERNAL, true
                ), weight = 1.0F, sink = Neuron(
                    0,
                    Category.INTERNAL, true
                )
            ), Connection(
                source = Neuron(
                    1,
                    Category.INTERNAL, false
                ), weight = -1.0F, sink = Neuron(2, Category.ACTION, true)
            )
        )
        assertEquals(expected, converter.getConnections(), "Connections not as expected")

    }
}