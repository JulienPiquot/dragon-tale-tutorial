package game.state

import java.awt.Graphics2D

import game.state.GameStateManager._

import scala.collection.mutable.ArrayBuffer

class GameStateManager {

  private val gameStates = new ArrayBuffer[GameState]
  private var currentState = MenuState
  gameStates.append(new MenuState(this))
  gameStates.append(new Level1State(this))

  def setState(state: Int): Unit = {
    currentState = state
    gameStates(currentState).init
  }

  def update() = {
    gameStates(currentState).update
  }

  def draw(g: Graphics2D) = {
    gameStates(currentState).draw(g)
  }

  def keyPressed(k: Int) = {
    gameStates(currentState).keyPressed(k)
  }

  def keyReleased(k: Int) = {
    gameStates(currentState).keyReleased(k)
  }

}

object GameStateManager {
  val MenuState = 0
  val Level1State = 1
}
