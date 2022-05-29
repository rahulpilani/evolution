import Brain.Connection
import Neuron.Category
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BrainTest {
    val mockSensorInputProvider = SensorInputProvider {
        when(it) {
            0 -> 0.5F
            1 -> 0.1F
            2 -> 0.2F
            else -> 0.0F
        }
    }

    val mockConnectionProvider = ConnectionProvider {
        listOf(
            Connection(Neuron(0, Category.SENSOR), 2.0F, Neuron(0, Category.INTERNAL)),
            Connection(Neuron(0, Category.SENSOR), 1.0F, Neuron(1, Category.INTERNAL)),
            Connection(Neuron(1, Category.SENSOR), -3.0F, Neuron(0, Category.INTERNAL)),
            Connection(Neuron(1, Category.SENSOR), 3.99F, Neuron(1, Category.INTERNAL)),
            Connection(Neuron(1, Category.SENSOR), 3.99F, Neuron(2, Category.ACTION)),
            Connection(Neuron(2, Category.SENSOR), -1.0F, Neuron(0, Category.INTERNAL)),
            Connection(Neuron(2, Category.SENSOR), 0.0F, Neuron(1, Category.INTERNAL)),
            Connection(Neuron(0, Category.INTERNAL), 3.99F, Neuron(0, Category.ACTION)),
            Connection(Neuron(0, Category.INTERNAL), -1.0F, Neuron(1, Category.ACTION)),
            Connection(Neuron(0, Category.INTERNAL), 0.0F, Neuron(2, Category.ACTION)),
            Connection(Neuron(1, Category.INTERNAL), 2.0F, Neuron(0, Category.ACTION)),
            Connection(Neuron(1, Category.INTERNAL), 3.0F, Neuron(1, Category.ACTION)),
            Connection(Neuron(1, Category.INTERNAL), 0.5F, Neuron(2, Category.ACTION)),
        )
    }

    @Test
    fun testFeedForward() {
        val brain = Brain(mockSensorInputProvider, mockConnectionProvider)
        //{Neuron(0, ACTION, true)=2.995, Neuron(1, ACTION, true)=1.0, Neuron(2, ACTION, true)=0.64900005}
        val expected = mapOf(
            Pair(Neuron(0, Category.ACTION, true), 2.995F),
            Pair(Neuron(1, Category.ACTION, true), 1.0F),
            Pair(Neuron(2, Category.ACTION, true), 0.64900005F),
        )
        assertEquals(expected, brain.step())
    }
}