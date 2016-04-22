package game.tilemap

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import game.GamePanel
import GamePanel._

class Background(s: String, ms: Double) {

  private val image: BufferedImage = readImage()
  var x: Double = 0
  var y: Double = 0
  var dx: Double = 0
  var dy: Double = 0
  var moveScale: Double = ms

  private def readImage(): BufferedImage = {
    val stream = getClass.getResourceAsStream(s)
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace();
        throw e
    } finally {
      stream.close()
    }
  }

  def setPosition(x: Double, y: Double) = {
    this.x = (x * moveScale) % Width
    this.y = (y * moveScale) % Height
  }

  def setVector(dx: Double, dy: Double): Unit = {
    this.dx = dx
    this.dy = dy
  }

  def update() {
    x += dx
    y += dy
  }

  def draw(g: Graphics2D) = {
    g.drawImage(image, x.toInt, y.toInt, null)
    if (x < 0) {
      g.drawImage(image, x.toInt + Width, y.toInt, null)
    } else if (x > 0) {
      g.drawImage(image, x.toInt - Width, y.toInt, null)
    }
  }
}
