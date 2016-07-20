package game.entity

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import game.tilemap.TileMap

class FireBall(tm: TileMap, right: Boolean) extends MapObject(tm: TileMap) {

  facingRight = right
  val height = 30
  val cwidth = 14
  val cheight = 14
  val moveSpeed = 3.8
  private val sprites: Array[BufferedImage] = new Array[BufferedImage](4)
  private val hitSprites: Array[BufferedImage] = new Array[BufferedImage](3)
  var width = 30
  private var hit: Boolean = false
  private var remove: Boolean = false
  if (right) dx = moveSpeed
  else dx = -moveSpeed

  val maxSpeed: Double = 0.0
  val stopSpeed: Double = 0.0
  val fallSpeed: Double = 0.0
  val maxFallSpeed: Double = 0.0
  val jumpStart: Double = 0.0
  val stopJumpSpeed: Double = 0.0

  // load sprites
  cutFireballTiles(readSprites())
  animation.frames_=(sprites)
  animation.delay = 70

  def setHit = {
    if (!hit) {
      hit = true
      animation.frames_=(hitSprites)
      animation.delay = 70
      dx = 0
    }
  }

  def shoudRemove = remove

  def update = {
    checkTileMapCollision()
    setPosition(xtemp.round.toInt, ytemp.round.toInt)

    if (dx == 0 && !hit) {
      setHit
    }

    animation.update()
    if (hit && animation.playedOnce) {
      remove = true
    }
  }

  override def draw(g: Graphics2D): Unit = {
    setMapPosition()
    super.draw(g)
  }

  private def cutFireballTiles(image: BufferedImage) = {
    for (i <- 0 until 4) {
      sprites(i) = image.getSubimage(i * width, 0, width, height)
    }
    for (i <- 0 until 3) {
      hitSprites(i) = image.getSubimage(i * width, height, width, height)
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
