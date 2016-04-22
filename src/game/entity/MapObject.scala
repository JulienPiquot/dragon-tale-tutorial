package game.entity

import java.awt.Rectangle

import game.GamePanel
import game.tilemap.{Tile, TileMap}

abstract class MapObject(tm: TileMap) {

  // tile stuff
  protected val tileSize: Int = tm.tileSize
  protected var xmap: Int = 0
  protected var ymap: Int = 0

  // position and vector
  var x: Int = 0
  var y: Int = 0
  protected var dx: Double = 0
  protected var dy: Double = 0

  // dimensions
  var width: Int
  val height: Int

  // collision box
  val cwidth: Int
  val cheight: Int

  // collision
  protected var currentRow: Int = 0
  protected var currentCol: Int = 0
  protected var xdest: Double = 0
  protected var ydest: Double = 0
  protected var xtemp: Double = 0
  protected var ytemp: Double = 0
  protected var topLeft: Boolean = false
  protected var topRight: Boolean = false
  protected var bottomLeft: Boolean = false
  protected var bottomRight: Boolean = false

  // animation
  protected val animation: Animation = new Animation
  protected var currentAction: Int = 0
  protected var previousAction: Int = 0
  protected var facingRight: Boolean = true

  // movement
  var left: Boolean = false
  var right: Boolean = false
  var up: Boolean = false
  var down: Boolean = false
  var jumping: Boolean = false
  var falling: Boolean = false

  // movement attributes
  protected val moveSpeed: Double
  protected val maxSpeed: Double
  protected val stopSpeed: Double
  protected val fallSpeed: Double
  protected val maxFallSpeed: Double
  protected val jumpStart: Double
  protected val stopJumpSpeed: Double

  def intersects(o: MapObject): Boolean = {
    val r1 = getRectangle()
    val r2 = o.getRectangle()
    r1.intersects(r2)
  }

  def checkTileMapCollision(): Unit = {
    currentCol = x / tileSize
    currentRow = y / tileSize

    xdest = x + dx
    ydest = y + dy

    xtemp = x
    ytemp = y

    calculateCorners(x, ydest)
    // going upwards
    if (dy < 0) {
      if (topLeft || topRight) {
        dy = 0
        ytemp = currentRow * tileSize + cheight / 2
      } else {
        ytemp += dy
      }
    }
    // going downwards
    if (dy > 0) {
      if (bottomLeft || bottomRight) {
        dy = 0
        falling = false
        ytemp = (currentRow + 1) * tileSize - cheight / 2
      } else {
        ytemp += dy
      }
    }

    calculateCorners(xdest, y)
    // going left
    if (dx < 0) {
      if (topLeft || bottomLeft) {
        dx = 0
        xtemp = currentCol * tileSize + cwidth / 2
      } else {
        xtemp += dx
      }
    }
    // going right
    if (dx > 0) {
      if (topLeft || bottomRight) {
        dx = 0
        xtemp = (currentCol + 1) * tileSize - cwidth / 2
      } else {
        xtemp += dx
      }
    }

    if (!falling) {
      calculateCorners(x, ydest + 1)
      if (!bottomLeft && !bottomRight) {
        falling = true
      }
    }
  }

  private def calculateCorners(x: Double, y: Double): Unit = {
    val leftTile = (x - cwidth / 2) / tileSize
    val rightTile = (x + cwidth / 2 - 1) / tileSize
    val topTile = (y - cheight / 2) / tileSize
    val bottomTile = (y + cheight / 2 - 1) / tileSize
    if (topTile < 0 || bottomTile >= tm.numRows ||
      leftTile < 0 || rightTile >= tm.numCols) {
      topLeft = false
      topRight = false
      bottomLeft = false
      bottomRight = false
    } else {
      val tl = tm.getType(topTile.toInt, leftTile.toInt)
      val tr = tm.getType(topTile.toInt, rightTile.toInt)
      val bl = tm.getType(bottomTile.toInt, leftTile.toInt)
      val br = tm.getType(bottomTile.toInt, rightTile.toInt)
      topLeft = tl == Tile.Blocked
      topRight = tr == Tile.Blocked
      bottomLeft = bl == Tile.Blocked
      bottomRight = br == Tile.Blocked
    }
  }

  private def getRectangle(): Rectangle = {
    new Rectangle(x - cwidth, y - cheight, cwidth, cheight)
  }

  def setPosition(x: Int, y: Int) = {
    this.x = x
    this.y = y
  }

  def setVector(dx: Int, dy: Int) = {
    this.dx = dx
    this.dy = dy
  }

  def setMapPosition() = {
    xmap = tm.x.toInt
    ymap = tm.y.toInt
  }

  def notOnScreen() = {
    x + xmap + width < 0 || x + xmap - width > GamePanel.Width || y + ymap + height < 0 || y + ymap - height > GamePanel.Height
  }
}
