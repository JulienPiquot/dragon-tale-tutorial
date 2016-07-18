package game.state

import java.awt.event.KeyEvent
import java.awt.{Color, Graphics2D}

import game.GamePanel
import game.entity.{Enemy, Player}
import game.tilemap.{Tile, Background, TileMap}

import scala.collection.mutable.ArrayBuffer

class Level1State(manager: GameStateManager) extends GameState(manager) {

  private val enemyList: ArrayBuffer[Enemy] = new ArrayBuffer[Enemy]()
  private val tileMap = new TileMap(30)
  private val background = new Background("/Backgrounds/grassbg1.gif", 0.1)
  private val player = new Player(tileMap)
  player.setPosition(100, 100)

  override def init: Unit = {
    tileMap.loadTile("/Tilesets/grasstileset.gif")
    tileMap.loadMap("/Maps/level1-1.map")
    tileMap.setPosition(0, 0)

    assert(tileMap.numCols == 107)
    assert(tileMap.numRows == 8)
    assert(tileMap.getType(0, 0) == Tile.Blocked)
    assert(tileMap.getType(1, 0) == Tile.Blocked)
    assert(tileMap.getType(1, 1) == Tile.Normal)
    assert(tileMap.getType(1, 2) == Tile.Normal)
    assert(tileMap.getType(1, 3) == Tile.Normal)
    assert(tileMap.getType(1, 4) == Tile.Normal)
    assert(tileMap.getType(1, 5) == Tile.Normal)
    assert(tileMap.getType(1, 6) == Tile.Normal)
    assert(tileMap.getType(1, 7) == Tile.Normal)
  }

  override def update: Unit = {
    player.update()
    tileMap.setPosition(
      GamePanel.Width / 2 - player.x,
      GamePanel.Height / 2 - player.y
    )
    background.setPosition(tileMap.x, tileMap.y)
  }

  override def keyPressed(k: Int): Unit = {
    if (k == KeyEvent.VK_LEFT) player.left = true
    if (k == KeyEvent.VK_RIGHT) player.right = true
    if (k == KeyEvent.VK_UP) player.up = true
    if (k == KeyEvent.VK_DOWN) player.down = true
    if (k == KeyEvent.VK_Z) player.jumping = true
    if (k == KeyEvent.VK_E) player.gliding = true
    if (k == KeyEvent.VK_R) player.scratching = true
    if (k == KeyEvent.VK_F) player.firing = true
  }

  override def draw(g: Graphics2D): Unit = {
    background.draw(g)
    tileMap.draw(g)
    player.draw(g)
  }

  override def keyReleased(k: Int): Unit = {
    if (k == KeyEvent.VK_LEFT) player.left = false
    if (k == KeyEvent.VK_RIGHT) player.right = false
    if (k == KeyEvent.VK_UP) player.up = false
    if (k == KeyEvent.VK_DOWN) player.down = false
    if (k == KeyEvent.VK_Z) player.jumping = false
    if (k == KeyEvent.VK_E) player.gliding = false
    if (k == KeyEvent.VK_R) player.scratching = false
    if (k == KeyEvent.VK_F) player.firing = false
  }
}
