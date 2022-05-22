class Genome(val genes: List<Gene>) {
    val hash: UInt

    init {
        var geneHash: UInt = 0x00000000.toUInt()
        for (gene in genes) {
            geneHash = geneHash or gene.gene
        }
        hash = geneHash;

    }

    override fun toString(): String {
        val builder = java.lang.StringBuilder()
        for (gene in genes) {
            builder.append(Gene.toBitString(gene.gene) + " " + gene)
            builder.append("\n")
        }
        return builder.toString()
    }

    
}