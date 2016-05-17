package game.entity

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import game.tilemap.TileMap

import scala.collection.mutable.ArrayBuffer

class Player(tm: TileMap) extends MapObject(tm) {

  var width = 30
  val height = 30
  val cwidth = 20
  val cheight = 20
  val moveSpeed = 0.3
  val maxSpeed = 1.6
  val stopSpeed = 0.4
  val fallSpeed = 0.15
  val maxFallSpeed = 4.0
  val jumpStart = -4.8
  val stopJumpSpeed = 0.3
  val maxHealth: Int = 5
  val maxFire: Int = 2500

  private val fireCost: Int = 200
  private val fireBallDamage: Int = 5
  private val scratchDamage: Int = 8
  private val scratchRange: Int = 40

  // animations
  private val sprites: ArrayBuffer[Array[BufferedImage]] = new ArrayBuffer[Array[BufferedImage]]()
  private val numFrames: Array[Int] = Array(2, 8, 1, 2, 4, 2, 5)

  // player stuff
  var health: Int = maxHealth
  var fire: Int = maxFire
  private val fireballs: ArrayBuffer[FireBall] = new ArrayBuffer[FireBall]()

  // fireball
  var firing: Boolean = false

  // scratch
  var scratching: Boolean = false

  // gliding
  var gliding: Boolean = false

  private var dead: Boolean = false
  private var flinching: Boolean = false
  private var flinchTimer: Long = 0

  cutAnimationTiles(readSprites())
  currentAction = Player.idle
  animation.frames_=(sprites(Player.idle))
  animation.delay = 400

  def update(): Unit = {
    // update position
    getNextPosition()
    checkTileMapCollision()
    setPosition(xtemp.round.toInt, ytemp.round.toInt)

    if (currentAction == Player.scratching && animation.playedOnce) scratching = false
    if (currentAction == Player.fireball && animation.playedOnce) firing = false

    // fireball attack
    fire += 1
    if (fire > maxFire) fire = maxFire
    if (firing && currentAction != Player.fireball) {
      if (fire > fireCost) {
        fire -= fireCost
        val fb = new FireBall(tm, facingRight)
        fb.setPosition(x, y)
        fireballs :+ fb
      }
    }

    // set animation
    if (scratching) {
      if (currentAction != Player.scratching) {
        currentAction = Player.scratching
        animation.frames_=(sprites(Player.scratching))
        animation.delay = 50
        width = 60
      }
    } else if (firing) {
      if (currentAction != Player.fireball) {
        currentAction = Player.fireball
        animation.frames_=(sprites(Player.fireball))
        animation.delay = 100
        width = 30
      }
    } else if (dy > 0) {
      if (gliding) {
        if (currentAction != Player.gliding) {
          currentAction = Player.gliding
          animation.frames_=(sprites(Player.gliding))
          animation.delay = 100
          width = 30
        }
      } else if (currentAction != Player.falling) {
        currentAction = Player.falling
        animation.frames_=(sprites(Player.falling))
        animation.delay = 100
        width = 30
      }
    } else if (dy < 0) {
      if (currentAction != Player.jumping) {
        currentAction = Player.jumping
        animation.frames_=(sprites(Player.jumping))
        animation.delay = -1
        width = 30
      }
    } else if (left || right) {
      if (currentAction != Player.walking) {
        currentAction = Player.walking
        animation.frames_=(sprites(Player.walking))
        animation.delay = 40
        width = 30
      }
    } else {
      if (currentAction != Player.idle) {
        currentAction = Player.idle
        animation.frames_=(sprites(Player.idle))
        animation.delay = 400
        width = 30
      }
    }
    animation.update()

    // set direction
    if (currentAction != Player.scratching && currentAction != Player.fireball) {
      if (right) facingRight = true
      if (left) facingRight = false
    }
  }

  def draw(g: Graphics2D) = {
    setMapPosition()

    // draw fireballs
    fireballs.foreach(fb => fb.draw(g))

    var draw = true

    // draw player
    if (flinching) {
      val elapsed = (System.nanoTime() - flinchTimer) / 1000000
      if (elapsed / 100 % 2 == 0) {
        draw = false
      }
    }

    if (draw) {
      if (facingRight) {
      g.drawImage(animation.getImage(),
        x + xmap - width / 2,
        y + ymap - height / 2,
        null)
      } else {
        g.drawImage(animation.getImage(),
          x + xmap - width / 2 + width,
          y + ymap - height / 2,
          -width,
          height,
          null)
        }
    }
  }

  private def getNextPosition() = {
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
    } else {
      if (dx > 0) {
        dx -= stopSpeed
        if (dx < 0) {
          dx = 0
        }
      } else if (dx < 0) {
        dx += stopSpeed
        if (dx > 0) {
          dx = 0
        }
      }
    }

    // cannot move while attacking except in the air
    if ((currentAction == Player.scratching || currentAction == Player.fireball) && !(jumping || falling)) {
      dx = 0
    }

    // jumping
    if (jumping && !falling) {
      dy = jumpStart
      falling = true
    }

    // falling
    if (falling) {
      if (dy > 0 && gliding) {
        dy += fallSpeed * 0.1
      } else {
        dy += fallSpeed
      }
      if (dy > 0) {
        jumping = false
      }
      if (dy < 0 && !jumping) {
        dy += stopJumpSpeed
      }
      if (dy > maxFallSpeed) {
        dy = maxFallSpeed
      }
    }
  }

  private def readSprites(): BufferedImage = {
    val stream = getClass.getResourceAsStream("/Sprites/Player/playersprites.gif")
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      stream.close()
    }
  }

  private def cutAnimationTiles(image: BufferedImage): Unit = {
    for (i <- 0 until 7) {
      val imageArray = new Array[BufferedImage](numFrames(i))
      for (j <- 0 until numFrames(i)) {
        // when scratching, the size is 60 instead of 30
        if (i != 6) {
          imageArray(j) = image.getSubimage(j * width, i * height, width, height)
        } else {
          imageArray(j) = image.getSubimage(j * width * 2, i * height, width * 2, height)
        }
      }
      sprites += imageArray
    }
  }
}

// TODO use enum
object Player {
  val idle = 0
  val walking = 1
  val jumping = 2
  val falling = 3
  val gliding = 4
  val fireball = 5
  val scratching = 6
}
