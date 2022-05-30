class Genome(val genes: List<Gene>) {
    val hash: UByte

    init {
        val gene1 = genes.first()
        val gene2 = genes.last()
        hash = computeHash(gene1, gene2)
    }

    override fun toString(): String {
        val builder = java.lang.StringBuilder()
        for (gene in genes) {
            builder.append(Gene.toBitString(gene.gene) + " " + gene )
            builder.append("\n")
        }
        builder.append(Gene.toBitString(hash.toUInt()))
        return builder.toString()
    }


    companion object {
        fun randomGenes(geneCount: Int): List<Gene> {
            val genes = mutableListOf<Gene>()
            for (i in 0 until geneCount) {
                genes.add(Gene.randomGene())
            }
            return genes
        }
        fun computeHash(gene1: Gene, gene2: Gene): UByte {
            val message0 = gene1.sourceType()
            val message1 = gene2.sourceType() shl 1
            val message2 = Bitwise.significantBit(gene1.sourceId().toUInt(), 6) shl 2
            val message3 = Bitwise.significantBit(gene2.sourceId().toUInt(), 6) shl 3
            val message4 = gene1.sinkType() shl 4
            val message5 = gene2.sinkType() shl 5
            val message6 = Bitwise.significantBit(gene1.sinkId().toUInt(), 6) shl 6
            val message7 = Bitwise.significantBit(gene2.sinkId().toUInt(), 6) shl 7
            return Bitwise.orTogether(
                message0,
                message1,
                message2,
                message3,
                message4,
                message5,
                message6,
                message7,
            ).toUByte()
        }
    }
}