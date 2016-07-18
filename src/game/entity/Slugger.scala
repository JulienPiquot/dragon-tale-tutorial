package game.entity

import game.tilemap.TileMap

class Slugger(tm: TileMap) extends Enemy(tm) {
  protected val maxSpeed: Double = 0.3
  protected val moveSpeed: Double = 0.3
  protected val maxFallSpeed: Double = 10.0
  protected val fallSpeed: Double = 0.2
  protected val stopSpeed: Double = 0.1
  protected val stopJumpSpeed: Double = 0.1
  protected val jumpStart: Double = 0.1
  override var width: Int = 20
  override val cwidth: Int = 15
  override val cheight: Int = 15
  override val height: Int = 20

  health = 2
  override val damage: Int = 1
  override protected val maxHealth: Int = 2
}
