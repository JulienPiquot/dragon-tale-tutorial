package game

import javax.swing.JFrame

object Game {
  def main(args: Array[String]): Unit = {
    val window = new JFrame("Dragon Tale")
    window.setContentPane(new GamePanel())
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    window.setResizable(false)
    window.pack()
    window.setVisible(true)
  }
}
