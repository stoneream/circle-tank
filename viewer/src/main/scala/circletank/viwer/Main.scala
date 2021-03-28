package circletank.viwer

import circletank.{ AbstractUI, InputType }

import java.awt.event.ActionEvent
import javax.swing.{ AbstractAction, Timer }
import scala.swing.event.{ Key, KeyPressed }
import scala.swing.{ Color, Dimension, Frame, Graphics2D, MainFrame, Panel, Rectangle, SimpleSwingApplication }

object Main extends SimpleSwingApplication {
  // window size
  val windowHeight = 400
  val windowWidth = 700
  val yGridMargin = (0 to windowHeight + 8 by 8)
  val xGridMargin = (0 to windowWidth + 8 by 8)

  // colors
  val backgroundColor = new Color(48, 99, 99)
  val tankColor = new Color(79, 130, 130)
  val gridColor = new Color(19, 15, 64)

  val game = new AbstractUI

  override def top: Frame = {
    new MainFrame {
      title = "circle-tank-viewer"
      contents = mainPanel
    }
  }

  def onPaint(g: Graphics2D): Unit = {
    val world = game.view

    // draw tank
    g setColor tankColor
    world.tanks.foreach {
      case (_, tank) =>
        val x = tank.position.x
        val y = tank.position.y
        val width = tank.radius * 2
        val height = tank.radius * 2
        g fill new Rectangle(x.toInt, y.toInt, width.toInt, height.toInt)
    }
  }

  val mainPanel = new Panel {
    preferredSize = new Dimension(windowWidth, windowHeight)
    focusable = true
    listenTo(keys)
    reactions += {
      case KeyPressed(_, key, _, _) => key match {
        case Key.W => game.input(InputType.Up)
        case Key.A => game.input(InputType.Left)
        case Key.S => game.input(InputType.Down)
        case Key.D => game.input(InputType.Right)
        case Key.Escape =>
          game.terminate
          timer.stop
          sys.exit(0)
        case _ => // do nothing
      }
    }

    override def paint(g: Graphics2D) = {
      g setColor backgroundColor
      g fillRect (0, 0, size.width, size.height)

      g setColor gridColor
      yGridMargin.foreach { y =>
        g drawLine (0, y, windowWidth + 8, y)
      }
      xGridMargin.foreach { x =>
        g drawLine (x, 0, x, windowHeight + 8)
      }

      onPaint(g)
    }

    val timer = new Timer(game.cycleDuration, new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        repaint
      }
    })

    timer.start()
  }

  override def shutdown(): Unit = {
    mainPanel.timer.stop
    game.terminate
    println("stopped")
    super.shutdown()
  }
}
