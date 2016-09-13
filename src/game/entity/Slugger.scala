package game.entity

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import game.tilemap.TileMap

class Slugger(tm: TileMap) extends Enemy(tm) {
  protected val maxSpeed: Double = 0.3
  protected val moveSpeed: Double = 0.3
  protected val maxFallSpeed: Double = 10.0
  protected val fallSpeed: Double = 0.2
  protected val stopSpeed: Double = 0.1
  protected val stopJumpSpeed: Double = 0.1
  protected val jumpStart: Double = 0.1
  override var width: Int = 30
  override val cwidth: Int = 20
  override val cheight: Int = 20
  override val height: Int = 30

  health = 2
  right = true
  override val damage: Int = 1
  override protected val maxHealth: Int = 2

  private val sprites: Array[BufferedImage] = cutAnimationTiles(readSprites())
  animation.frames_=(sprites)
  animation.delay_=(300)
  right = true

  private def cutAnimationTiles(image: BufferedImage): Array[BufferedImage] = {
    for (i <- Array(0, 1, 2)) yield image.getSubimage(i * width, 0, width, height)
  }

  private def readSprites(): BufferedImage = {
    val stream = getClass.getResourceAsStream("/Sprites/Enemies/slugger.gif")
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      stream.close()
    }
  }

  private def getNextPosition() {
    // movement
    if (left) {
      dx -= moveSpeed
      if (dx < -maxSpeed) {
        dx = -maxSpeed
      }
    } else if (right) {
      dx += moveSpeed
      if (dx > maxSpeed) {
        dx = maxSpeed
      }
    }

    if (falling) {
      dy += fallSpeed
    }
  }

  override def update() {
    // update position
    getNextPosition()
    checkTileMapCollision()
    setPosition(xtemp.ceil.toInt, ytemp.ceil.toInt)

    // check flinching
    if (flinching) {
      val elapsed = (System.nanoTime() - flinchTimer) / 1000000
      if (elapsed > 400) {
        flinching = false
      }
    }

    // if it hits a wall, go other direction
    if (right && dx == 0) {
      right = false
      left = true
    } else if (left && dx == 0) {
      right = true
      left = false
    }
  }

  override def draw(g: Graphics2D): Unit = {
    if (!notOnScreen()) {
      setMapPosition()
      super.draw(g)
    }
  }
}
