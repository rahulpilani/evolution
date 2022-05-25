import io.kotest.core.spec.style.FunSpec
import io.kotest.core.tuple
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalUnsignedTypes::class)
internal class BitwiseTest : FunSpec({
    context("Bitwise significant bit works") {
        withData(
            tuple(0b1111011010000100u, 15, 1u),
            tuple(0b1111011010000100u, 14, 1u),
            tuple(0b1111011010000100u, 13, 1u),
            tuple(0b1111011010000100u, 12, 1u),
            tuple(0b1111011010000100u, 11, 0u),
            tuple(0b1111011010000100u, 10, 1u),
            tuple(0b1111011010000100u, 9, 1u),
            tuple(0b1111011010000100u, 8, 0u),
            tuple(0b1111011010000100u, 7, 1u),
            tuple(0b1111011010000100u, 6, 0u),
            tuple(0b1111011010000100u, 5, 0u),
            tuple(0b1111011010000100u, 4, 0u),
            tuple(0b1111011010000100u, 3, 0u),
            tuple(0b1111011010000100u, 2, 1u),
            tuple(0b1111011010000100u, 1, 0u),
            tuple(0b1111011010000100u, 0, 0u),
        ) { (value, bit, expected) ->
            Bitwise.significantBit(value, bit) shouldBe expected
        }
    }

    context("orTogether") {
        val a = 0b1111011010000100u
        val b = (15 downTo 0)
            .map { i -> Bitwise.significantBit(a, i) shl i }
            .toCollection(mutableListOf())
            .toUIntArray()
        val c = Bitwise.orTogether(*b)
        c shouldBe a
    }
})