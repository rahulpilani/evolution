import io.kotest.core.spec.style.FunSpec
import io.kotest.core.tuple
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

internal class GeneTest : FunSpec({

    context("Testing source flag works") {
        withData(
            tuple(0x80000000.toUInt(), true),
            tuple(0x90000000.toUInt(), true),
            tuple(0xA0000000.toUInt(), true),
            tuple(0xB0000000.toUInt(), true),
            tuple(0xC0000000.toUInt(), true),
            tuple(0x00000000.toUInt(), false),
            tuple(0x70000000.toUInt(), false),
            tuple(0x60000000.toUInt(), false),
        ) { (geneNumber, expected) ->
            Gene(geneNumber).sourceInternal() shouldBe expected
        }
    }

    context("Testing source ID works") {
        withData(
            tuple(0xAAAA0000.toUInt(), 42),
            tuple(0xADAA0000.toUInt(), 45),
        ) { (geneNumber, expected) ->
            Gene(geneNumber).sourceId() shouldBe expected
        }
    }

    context("Testing sink flag works") {
        withData(
            tuple(0xFFFF0000.toUInt(), true),
            tuple(0x00000000.toUInt(), false),
        ) { (geneNumber, expected) ->
            Gene(geneNumber).sinkInternal() shouldBe expected
        }
    }

    context("Testing sink ID works") {
        withData(
            tuple(0xFFFF0000.toUInt(), 127),
            tuple(0xFFFE0000.toUInt(), 126),
            tuple(0xFF000000.toUInt(), 0),
        ) { (geneNumber, expected) ->
            Gene(geneNumber).sinkId() shouldBe expected
        }
    }

    context("Testing weight works") {
        withData(
            tuple(0xFFFF7FFF.toUInt(), 3.9998779296875),
            tuple(0xFFFE8000.toUInt(), -4.0),
            tuple(0xFF000000.toUInt(), 0),
        ) { (geneNumber, expected) ->
            Gene(geneNumber).weight() shouldBe expected
        }
    }

})