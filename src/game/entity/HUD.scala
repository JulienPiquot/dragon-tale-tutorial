package game.entity

import java.awt.{Color, Graphics2D, Font}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class HUD(p: Player) {

  val image: BufferedImage = readHud
  val font: Font = new Font("Arial", Font.PLAIN, 14)

  private def readHud(): BufferedImage = {
    val stream = getClass.getResourceAsStream("/HUD/hud.gif")
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      stream.close()
    }
  }

  def draw(g: Graphics2D) {
    g.drawImage(image, 0, 10, null)
    g.setFont(font)
    g.setColor(Color.WHITE)
    g.drawString(p.health + "/" + p.maxHealth, 30, 25)
    g.drawString(p.fire / 100 + "/" + p.maxFire / 100, 30, 45)
  }

}
