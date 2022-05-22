import java.awt.Color
import java.awt.EventQueue
import java.awt.Graphics
import javax.swing.JFrame

class Display(title: String, private val maxX: Int, private val maxY: Int, population: Int) : JFrame() {
    private val grid = Grid(maxY, maxX, population)

    init {
        createUI(title)
    }

    private fun createUI(title: String) {

        setTitle(title)



        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(maxX * MULT + X_OFFSET + 30, maxY * MULT + Y_OFFSET + 60)
        setLocationRelativeTo(null)
    }

    override fun paint(g: Graphics?) {
        g?.color = Color.WHITE
        g?.fillRect(X_OFFSET, Y_OFFSET, maxX * MULT, maxY * MULT)
        g?.color = Color.BLACK
        g?.drawRect(X_OFFSET, Y_OFFSET, maxX * MULT, maxY * MULT)
        grid.creatures.forEach { entry ->
            drawCircle(entry.key, g)
        }
    }

    private fun drawCircle(position: Position, g: Graphics?) {
        val xPosition = (position.x * MULT) + X_OFFSET
        val yPosition = (position.y * MULT) + Y_OFFSET
        g?.color = Color.GREEN
        g?.fillOval(xPosition, yPosition, MULT, MULT)
    }

    companion object {
        const val X_OFFSET = 7
        const val MULT = 6
        const val Y_OFFSET = 35
    }
}

private fun createAndShowGUI() {

    val frame = Display("Evolution", 128, 128, 32 * 32)
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}