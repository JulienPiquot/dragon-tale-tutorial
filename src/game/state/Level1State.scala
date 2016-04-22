package game.state

import java.awt.{Color, Graphics2D}

import game.GamePanel
import game.tilemap.{Background, TileMap}

class Level1State(manager: GameStateManager) extends GameState(manager) {

  private val tileMap = new TileMap(30)
  private val background = new Background("/Backgrounds/grassbg1.gif", 0.1)

  override def init: Unit = {
    tileMap.loadTile("/Tilesets/grasstileset.gif")
    tileMap.loadMap("/Maps/level1-1.map")
    tileMap.setPosition(0, 0)
  }

  override def update: Unit = {}

  override def keyPressed(k: Int): Unit = {}

  override def draw(g: Graphics2D): Unit = {
    // draw background
    background.draw(g)

    // draw tilemap
    tileMap.draw(g)
  }

  override def keyReleased(k: Int): Unit = {}
}
