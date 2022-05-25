import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import java.util.stream.Stream
import kotlin.test.assertEquals

class GenomeTest {
    @Test
    fun testHash() {
        val gene1 = 0b10001010010011010010000010110111u
        val gene2 = 0b01111111010000011010100110000101u
        val expected = 0b11001001u


        // 0
        // 1
        // 11
        // 11111111
        // 1 + 2 + 4 + 8 + 16 + 32 + 64 = 127
        // VV        VV
        // 1000 1010 0100 1101 0010 0000 1011 0111u
        // 0111 1111 0100 0001 1010 1001 1000 0101u
        //

        assertEquals(expected.toUByte(), Genome.computeHash(Gene(gene1), Gene(gene2)))
    }
}