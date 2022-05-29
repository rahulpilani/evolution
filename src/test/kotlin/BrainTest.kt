class BrainTest {
    val mockSensorInputProvider = SensorInputProvider {
        when(it) {
            0 -> 0.5F
            1 -> 0.1F
            2 -> 0.2F
            else -> 0.0F
        }
    }
}