class Bitwise {
    companion object {

        fun significantBit(number: UInt, bit: Int): UInt {
            if (bit > 31) {
                throw java.lang.IllegalArgumentException("UInt has just 32 bits and bit is zero index")
            }
            val bitMask = 1u shl bit
            val maskedNumber = number and bitMask
            return maskedNumber shr bit
        }

        fun booleanToBit(flag: Boolean): UInt {
            return if (flag) 1u else 0u
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        fun orTogether(vararg values: UInt): UInt {
            return values.asList().foldRight(0x0u) { a, b -> b or a }
        }
    }
}

fun main() {
    val number = 108.toUInt()
    val number2 = 128.toUInt()
    val gene1 = Gene.randomGene()
    val gene2 = Gene.randomGene()
    println("gene1    " + Gene.toBitString(gene1.gene))
    println("gene2    " + Gene.toBitString(gene2.gene))
    val message0 = gene1.sourceType()
    println("message0 " + Gene.toBitString(message0.toUInt()))
    val message1 = gene2.sourceType() shl 1
    println("message1 " + Gene.toBitString(message1.toUInt()))
    val message2 = Bitwise.significantBit(gene1.sourceId().toUInt(), 6) shl 2
    println("message2 " + Gene.toBitString(message2.toUInt()))
    val message3 = Bitwise.significantBit(gene2.sourceId().toUInt(), 6) shl 3
    println("message3 " + Gene.toBitString(message3.toUInt()))
    val message4 = gene1.sinkType() shl 4
    println("message4 " + Gene.toBitString(message4.toUInt()))
    val message5 = gene2.sinkType() shl 5
    println("message5 " + Gene.toBitString(message5.toUInt()))
    val message6 = Bitwise.significantBit(gene1.sinkId().toUInt(), 6) shl 6
    println("message6 " + Gene.toBitString(message6.toUInt()))
    val message7 = Bitwise.significantBit(gene2.sinkId().toUInt(), 7) shl 7
    println("message7 " + Gene.toBitString(message7.toUInt()))

    val hash = (message0.toUInt() or
            message1.toUInt() or
            message2.toUInt() or
            message3.toUInt() or
            message4.toUInt() or
            message5.toUInt() or
            message6.toUInt() or
            message7.toUInt()).toUByte()
    println("hash     " + Gene.toBitString(hash.toUInt()))
}