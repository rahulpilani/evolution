import java.awt.EventQueue
import javax.swing.JFrame

class Display(title: String, private val maxX: Int, private val maxY: Int, population: Int) : JFrame() {
    private val world = World(maxY, maxX, population)

    init {
        createUI(title)
    }

    private fun createUI(title: String) {

        setTitle(title)



        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(maxX * MULT + X_OFFSET + 30, maxY * MULT + Y_OFFSET + 60)
        setLocationRelativeTo(null)
        add(CreaturePanel(world.creatures.values, maxX, maxY))


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