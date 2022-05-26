import Display.Companion.MULT
import Display.Companion.X_OFFSET
import Display.Companion.Y_OFFSET
import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

class CreaturePanel(private val creatures: MutableCollection<Creature>, private val maxX: Int, private val maxY: Int) :
    JPanel() {

    override fun paint(g: Graphics?) {
        g?.color = Color.WHITE
        g?.fillRect(X_OFFSET, Y_OFFSET, maxX * MULT, maxY * MULT)
        g?.color = Color.BLACK
        g?.drawRect(X_OFFSET, Y_OFFSET, maxX * MULT, maxY * MULT)
        creatures.forEach { creature ->
            drawCircle(creature.position, creature.genome.hash, g)
        }
    }

    private fun drawCircle(position: Position, hash: UByte, g: Graphics?) {
        val xPosition = (position.x * MULT) + X_OFFSET
        val yPosition = (position.y * MULT) + Y_OFFSET
        val (red, green, blue) = colorFromHash(hash)
        g?.color = Color(red, green, blue)
        g?.fillOval(xPosition, yPosition, MULT, MULT)
    }

    private fun colorFromHash(hash: UByte): Triple<Int, Int, Int> {
        var red = hash.toInt()
        var green = (hash.toInt() % 31) * 8
        var blue = ((hash.toInt() % 7) * 32)

        val maxLuma = 190
        val maxColor = 190

        val luma = ((red * 3) + blue + (green * 4)) / 8
        if (luma > maxLuma) {
            if (red > maxColor) red %= maxColor
            if (green > maxColor) green %= maxColor
            if (blue > maxColor) blue %= maxColor
        }
        return Triple(red, green, blue)
    }
}