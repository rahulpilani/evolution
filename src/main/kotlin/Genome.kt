class Genome(val genes: List<Gene>) {
    val hash: Triple<UByte, UByte, UByte>

    init {
        val gene1 = genes.first()
        val gene2 = genes.last()
        val hash1 = computeHash(Bitwise::significantBit, 6, gene1, gene2)
        val hash2 = computeHash(Bitwise::significantBit, 5, gene1, gene2)
        val hash3 = computeHash(Bitwise::significantBit, 4, gene1, gene2)
        hash = Triple(hash1, hash2, hash3)
    }

    private fun computeHash(f: (UInt, Int) -> UInt, bit: Int, gene1: Gene, gene2: Gene): UByte {
        val message0 = Bitwise.booleanToBit(gene1.sourceInternal())
        val message1 = Bitwise.booleanToBit(gene2.sourceInternal()) shl 1
        val message2 = f(gene1.sourceId().toUInt(), bit) shl 2
        val message3 = f(gene2.sourceId().toUInt(), bit) shl 3
        val message4 = Bitwise.booleanToBit(gene1.sinkInternal()) shl 4
        val message5 = Bitwise.booleanToBit(gene2.sinkInternal()) shl 5
        val message6 = f(gene1.sinkId().toUInt(), bit) shl 6
        val message7 = f(gene2.sinkId().toUInt(), bit) shl 7

        return (message0 or
                message1 or
                message2 or
                message3 or
                message4 or
                message5 or
                message6 or
                message7).toUByte()
    }

    override fun toString(): String {
        val builder = java.lang.StringBuilder()
        for (gene in genes) {
            builder.append(Gene.toBitString(gene.gene) + " " + gene )
            builder.append("\n")
        }
        builder.append(Gene.toBitString(hash.third.toUInt() or (hash.second.toUInt() shl 8) or (hash.first.toUInt() shl 16 )))
        return builder.toString()
    }


}