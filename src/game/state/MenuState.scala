package game.state

import java.awt.event.KeyEvent
import java.awt.{Font, Color, Graphics2D}

import game.tilemap.Background

class MenuState(manager: GameStateManager) extends GameState(manager) {

  private var currentChoice = 0;
  private val options = List("Start", "Help", "Quit")
  private val titleColor = new Color(128, 0, 0)
  private val titleFont = new Font("Century Gothic", Font.PLAIN, 28)
  private val font = new Font("Arial", Font.PLAIN, 12)
  private val bg: Background = new Background("/Backgrounds/menubg.gif", 1)
  bg.setVector(-0.1, 0)

  override def init: Unit = {}

  override def update: Unit = {
    bg.update()
  }

  override def keyPressed(k: Int): Unit = {
    if (k == KeyEvent.VK_ENTER) {
      select();
    } else if (k == KeyEvent.VK_UP) {
      currentChoice = currentChoice - 1
      if (currentChoice == -1) {
        currentChoice = options.length - 1
      }
    } else if (k == KeyEvent.VK_DOWN) {
      currentChoice = currentChoice + 1
      if (currentChoice == options.length) {
        currentChoice = 0
      }
    }
  }

  private def select(): Unit = {
    if (currentChoice == 0) {
      manager.setState(GameStateManager.Level1State)
    } else if (currentChoice == 1) {

    } else if (currentChoice == 2) {
      System.exit(0)
    }
  }

  override def draw(g: Graphics2D): Unit = {
    bg.draw(g)

    // daw title
    g.setColor(titleColor)
    g.setFont(titleFont)
    g.drawString("Dragon Tale", 80, 70)

    // draw menu options
    g.setFont(font)
    var i = 0
    options.foreach(option => {
      if (i == currentChoice) {
        g.setColor(Color.BLACK)
      } else {
        g.setColor(Color.RED)
      }
      g.drawString(options(i), 145, 140 + i * 15)
      i = i + 1
    })
  }

  override def keyReleased(k: Int): Unit = {}
}
