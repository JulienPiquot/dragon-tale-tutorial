package game.entity

import java.awt.image.BufferedImage

class Animation {

  private var _frames = new Array[BufferedImage](0)
  var currentFrame: Int = 0

  private var startTime: Long = 0
  var delay: Long = 0

  var playedOnce: Boolean = false

  def frames_=(someFrames: Array[BufferedImage]): Unit = {
    _frames = someFrames
    currentFrame = 0
    startTime = System.nanoTime()
    playedOnce = false
  }

  def update() = {
    if (delay != -1) {
      val elapsed = (System.nanoTime() - startTime) / 1000000
      if (elapsed > delay) {
        currentFrame = currentFrame + 1
        startTime = System.nanoTime()
      }
      if (currentFrame == _frames.length) {
        currentFrame = 0
        playedOnce = true
      }
    }
  }

  def getImage(): BufferedImage = {
    _frames(currentFrame)
  }

}
