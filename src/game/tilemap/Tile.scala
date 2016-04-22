package game.tilemap

import java.awt.image.BufferedImage

class Tile(val image: BufferedImage, val kind: Int) {

}

object Tile {
  val Normal = 0
  val Blocked = 0
}
