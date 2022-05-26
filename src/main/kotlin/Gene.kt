import kotlin.random.Random

class Gene(val gene: UInt) {
    /*
    |Source: 1 bit| Source ID: 7 bits | Sink: 1 bit | Sink ID: 7 bits | Weight: 16 bits|
     */

    companion object {
        fun randomGene(): Gene {
            val source: UInt = Random.nextInt(0,2).toUInt() and 1.toUInt() shl 31
            val sourceID: UInt = Random.nextInt(0,128).toUInt() and 0x7FFF.toUInt() shl 24
            val sink: UInt = Random.nextInt(0, 2).toUInt() and 1.toUInt() shl 23
            val sinkID: UInt = Random.nextInt(0, 128).toUInt() and 0x7FFF.toUInt() shl 16
            val weight: UInt = (Random.nextInt(0,0xFFFF)).toUInt()
            return Gene(source or sourceID or sink or sinkID or weight)
        }

        fun toBitString(gene: UInt): String {
            var mask = 1.toUInt()
            val builder: StringBuilder = java.lang.StringBuilder()
            for (i in 0 until 32) {
                builder.append((gene and mask) shr i)
                mask = mask shl 1
                if ((i + 1) % 8 == 0) {
                    builder.append(" ")
                }
            }
            return builder.reverse().toString()
        }

        fun toHexString(gene: UInt): String {
            return Integer.toHexString(gene.toInt());
        }

    }

    fun sourceType(): UInt {
        return gene and 0x80000000.toUInt()
    }

    fun sinkType(): UInt {
        return gene and 0x00800000.toUInt()
    }

    fun sourceTypeAsEnum(): Neuron.Type {
        return Neuron.Type.getByValue(this.sourceType().toInt())!!
    }

    fun sinkTypeAsEnum(): Neuron.Type {
        return Neuron.Type.getByValue(this.sinkType().toInt())!!
    }

    fun sourceId(): Int {
        return (gene and 0x7F000000.toUInt() shr 24).toInt()
    }

    fun sinkId(): Int {
        return (gene and 0x007F0000.toUInt() shr 16).toInt()
    }

    fun weight(): Double {
        return (gene and 0x0000FFFF.toUInt()).toShort().toDouble() / 8192.0
    }

    override fun toString(): String {
        return "" + sourceType() + ", " + sourceId() + ", " + sinkType() + ", " + sinkId() + ", " + weight()
    }


}
fun main() {
    for (i in 0 until 100) {
        val randomGene = Gene.randomGene()
        println(Gene.toBitString(randomGene.gene) + " " + randomGene);
    }
}