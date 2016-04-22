package game

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.image.BufferedImage
import java.awt.{Dimension, Graphics2D}
import javax.swing.JPanel

import game.state.GameStateManager

class GamePanel extends JPanel with Runnable with KeyListener {

  // game thread
  var running: Boolean = false
  var thread: Option[Thread] = None
  val fps = 60
  val targetTime = 1000 / fps

  // image
  val image: BufferedImage = new BufferedImage(GamePanel.Width, GamePanel.Height, BufferedImage.TYPE_INT_RGB)
  val g: Graphics2D = image.getGraphics().asInstanceOf[Graphics2D]

  // game state manager
  val gsm: GameStateManager = new GameStateManager

  val widthAtScale = GamePanel.Width * GamePanel.Scale
  val heightAtscale = GamePanel.Height * GamePanel.Scale
  setPreferredSize(new Dimension(widthAtScale, heightAtscale))
  setFocusable(true)

  private def update() = {
    gsm.update()
  }

  private def draw() = {
    gsm.draw(g)
  }

  private def drawToScreen() = {
    def g = getGraphics
    g.drawImage(image, 0, 0, GamePanel.Width * GamePanel.Scale, GamePanel.Height * GamePanel.Scale, null)
    g.dispose()
  }

  override def run(): Unit = {
    running = true

    var start = 0l
    var elapsed = 0l
    var wait = 0l

    // game loop
    while (running) {
      start = System.nanoTime()

      update()
      draw()
      drawToScreen()

      elapsed = System.nanoTime() - start
      wait = targetTime - elapsed / 1000000
      try {
        if (wait > 0) {
          Thread.sleep(wait)
        }
      } catch {
        case e: Exception => e.printStackTrace()
      }

    }
  }

  override def keyTyped(e: KeyEvent): Unit = {}

  override def keyPressed(e: KeyEvent): Unit = {
    gsm.keyPressed(e.getKeyCode)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    gsm.keyReleased(e.getKeyCode)
  }

  override def addNotify(): Unit = {
    super.addNotify()
    if (!thread.isDefined) {
      val t = new Thread(this)
      thread = new Some(t)
      addKeyListener(this)
      t.start()
    }
  }
}

object GamePanel {
  // dimensions
  val Width = 320
  val Height = 240
  val Scale = 2
}
