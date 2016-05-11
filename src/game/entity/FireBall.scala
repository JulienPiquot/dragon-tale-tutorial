package game.entity

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import game.tilemap.TileMap

class FireBall(tm: TileMap, right: Boolean) extends MapObject(tm: TileMap) {

  private var hit: Boolean = false
  private var remove: Boolean = false
  private val sprites: Array[BufferedImage] = new Array[BufferedImage](4)
  private val hitSprites: Array[BufferedImage] = new Array[BufferedImage](3)

  var width = 30
  val height = 30
  val cwidth = 14
  val cheight = 14
  val moveSpeed = 3.8
  if (right) dx = moveSpeed
  else dx = -moveSpeed

  // load sprites
  cutFireballTiles(readSprites())

  private def cutFireballTiles(image: BufferedImage) = {
    for (i <- 0 until 4) {
      sprites(i) = image.getSubimage(i * width, 0, width, height)
    }
    for (i <- 0 until 3) {
      sprites(i) = image.getSubimage(i * width, height, width, height)
    }
  }

  private def readSprites(): BufferedImage = {
    val stream = getClass.getResourceAsStream("/Sprites/Player/fireball.gif")
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      stream.close()
    }
  }

}
