package game.entity

import game.tilemap.TileMap

abstract class Enemy(tm: TileMap) extends MapObject(tm) {

  protected var health: Int = 0
  protected val maxHealth: Int
  var dead: Boolean = false
  val damage: Int

  protected var flinching: Boolean = false
  protected var flinchTimer: Long = 0

  def hit(damage: Int) {
    if (!dead && !flinching) {
      health -= damage
      if (health < 0) health = 0
      if (health == 0) dead = true
      flinching = true
      flinchTimer = System.nanoTime()
    }
  }
}
