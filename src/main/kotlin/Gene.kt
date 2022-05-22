import kotlin.random.Random

class Gene(private val gene: UInt) {
    /*
    |Source: 1 bit| Source ID: 7 bits | Sink: 1 bit | Sink ID: 7 bits | Weight: 16 bits|
     */
    companion object {
        fun randomGene(): Gene {
            return Gene(Random.nextInt().toUInt())
        }
    }

    fun sourceInternal(): Boolean {
        return gene and 0x80000000.toUInt() == 0x80000000.toUInt()
    }

    fun sinkInternal(): Boolean {
        return gene and 0x08000000.toUInt() == 0x08000000.toUInt()
    }

    fun sourceId(): Int {
        return (gene and 0x7F000000.toUInt() shr 24).toInt()
    }

    fun sinkId(): Int {
        return (gene and 0x007F0000.toUInt() shr 16).toInt()
    }

    fun weight(): Short {
        return (gene and 0x0000FFFF.toUInt()).toShort()
    }

}