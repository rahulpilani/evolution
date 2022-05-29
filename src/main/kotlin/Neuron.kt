class Neuron(
    var id: Int,
    val category: Category,
    var driven: Boolean = true
) {
    private val initialOutput: Float = 0.5F
    var output = initialOutput


    fun remap(id: Int) {
        this.id = id
    }

    override fun toString(): String {
        return "Neuron($id, $category, $driven)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Neuron

        if (id != other.id) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + category.hashCode()
        return result
    }

    enum class Category {
        SENSOR, ACTION, INTERNAL;

        companion object {
            fun getByValue(value: Int, source: Boolean): Category {
                return if (value == 1) {
                    if (source) {
                        SENSOR
                    } else {
                        ACTION
                    }
                } else {
                    INTERNAL
                }
            }
        }
    }


}