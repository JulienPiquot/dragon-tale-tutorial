package game.tilemap

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.{InputStreamReader, BufferedReader}
import javax.imageio.ImageIO

import game.GamePanel

class TileMap(val tileSize: Int) {

  var x: Double = 0.0
  var y: Double = 0.0

  private val xmin = 0
  private val ymin = 0
  private val xmax = Int.MaxValue
  private val ymax = Int.MaxValue

  // map
  private val tween: Double = 0.07
  var numRows = 0
  var numCols = 0
  private var map = Array.ofDim[Int](numRows, numCols)
  var width = 0
  var heigth = 0

  // tileset
  private var tileset: BufferedImage = null
  private var numTilesAcross = 0
  private var tiles: Array[Array[Tile]] = null

  // drawing
  private var rowOffset = 0
  private var colOffset = 0
  private val numRowsToDraw = GamePanel.Height / tileSize + 2
  private val numColsToDraw = GamePanel.Width / tileSize + 2


  private def readImage(imageName: String): BufferedImage = {
    val stream = getClass.getResourceAsStream(imageName)
    try {
      ImageIO.read(stream)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      stream.close()
    }
  }

  private def readMap(resourceName: String): BufferedReader = {
    val stream = getClass.getResourceAsStream(resourceName)
    try {
      new BufferedReader(new InputStreamReader(stream))
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    }
  }

  def loadTile(s: String) = {
    tileset = readImage(s)
    numTilesAcross = tileset.getWidth / tileSize
    tiles = Array.ofDim[Tile](2, numTilesAcross)
    var col = 0
    while (col < numTilesAcross) {
      val firstSubimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize)
      tiles(0)(col) = new Tile(firstSubimage, Tile.Normal)
      val secondSubImage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize)
      tiles(1)(col) = new Tile(secondSubImage, Tile.Blocked)
      col = col + 1
    }
  }

  def loadMap(s: String) = {
    val reader = readMap(s)
    try {
      parseMap(reader)
    } catch {
      case e: Exception => e.printStackTrace()
        throw e
    } finally {
      reader.close()
    }
  }

  private def parseMap(reader: BufferedReader) = {
    numCols = Integer.parseInt(reader.readLine())
    numRows = Integer.parseInt(reader.readLine())
    map = Array.ofDim[Int](numRows, numCols)
    width = numCols * tileSize
    heigth = numRows * tileSize
    val delim = "\\s+"
    var row = 0
    while (row < numRows) {
      val line = reader.readLine()
      val tokens = line.split(delim)
      var col = 0
      while (col < numCols) {
        map(row)(col) = Integer.parseInt(tokens(col))
        col = col + 1
      }
      row = row + 1
    }
  }

  def getType(row: Int, col: Int) = {
    val rc = map(row)(col)
    val r = rc / numTilesAcross
    val c = rc % numTilesAcross
    tiles(r)(c).kind
  }

  def setPosition(x: Double, y: Double) {
    this.x += (x - this.x) * tween
    this.y += (y - this.y) * tween
    fixBounds()
    colOffset = (-this.x / tileSize).asInstanceOf[Int]
    rowOffset = (-this.y / tileSize).asInstanceOf[Int]
  }

  private def fixBounds() = {
    if (x < xmin) x = xmin
    if (y < ymin) y = ymin
    if (x > xmax) x = xmax
    if (y > ymax) y = ymax
  }

  def draw(g: Graphics2D) = {
    var row = rowOffset
    while (row < rowOffset + numRowsToDraw && row <  numRows) {
      var col = colOffset
      while (col < colOffset + numColsToDraw && col < numCols) {
        def rc = map(row)(col)
        if (rc != 0) {
          def r = rc / numTilesAcross
          def c = rc % numTilesAcross
          g.drawImage(tiles(r)(c).image, (x + col * tileSize).asInstanceOf[Int], (y + row * tileSize).asInstanceOf[Int], null)
        }
        col = col + 1
      }
      row = row + 1
    }
  }
}
