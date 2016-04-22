package game.state

import java.awt.Graphics2D

abstract class GameState(gsm: GameStateManager) {

  def init

  def update

  def draw(g: Graphics2D)

  def keyPressed(k: Int)

  def keyReleased(k: Int)

}
