import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer


class Display(title: String, private val maxX: Int, private val maxY: Int, population: Int) : JFrame() {
    val world = World(maxY, maxX, population)
    private var creaturePanel: CreaturePanel = CreaturePanel(world.creatures.values, maxX, maxY)

    init {
        createUI(title)
    }

    private fun createUI(title: String) {

        setTitle(title)



        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(maxX * MULT + X_OFFSET + 30, maxY * MULT + Y_OFFSET + 60)
        setLocationRelativeTo(null)
        add(creaturePanel)

        EventQueue.invokeLater(this::displayStep)
    }

    fun displayStep() {
        val maxSteps = 200
        val timer = Timer(2, object : ActionListener {
            var currentFrame = 0
            override fun actionPerformed(e: ActionEvent) {
                world.step()
                remove(creaturePanel)
                creaturePanel = CreaturePanel(world.creatures.values, maxX, maxY)
                add(creaturePanel)
                revalidate()
                repaint()
                if (currentFrame < maxSteps) currentFrame++ else {
                    (e.source as Timer).stop()
                    world.endOfThisGeneration()
                    EventQueue.invokeLater(this@Display::displayStep)
                }
            }
        })
        timer.start()

        world.endOfThisGeneration()

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