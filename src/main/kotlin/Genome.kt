class Genome(val genes: List<Gene>) {
    val hash: UInt

    init {
        var geneHash: UInt = 0xFFFFFFFF.toUInt()
        for (gene in genes) {
            geneHash = geneHash and gene.gene
        }
        hash = geneHash;

    }

    override fun toString(): String {
        val builder = java.lang.StringBuilder()
        for (gene in genes) {
            builder.append(Gene.toBitString(gene.gene) + " " + gene )
            builder.append("\n")
        }
        builder.append(Gene.toBitString(hash))
        return builder.toString()
    }


}