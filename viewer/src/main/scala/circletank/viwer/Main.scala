package circletank.viwer

import circletank.{ AbstractUI, InputType }

import java.awt.event.ActionEvent
import javax.swing.{ AbstractAction, Timer }
import scala.swing.event.{ Key, KeyPressed }
import scala.swing.{ Color, Dimension, Font, Frame, Graphics2D, MainFrame, Panel, Rectangle, SimpleSwingApplication }

object Main extends SimpleSwingApplication {
  // window size
  val margin = 10
  val windowWidth = 1368
  val windowHeight = 1368
  val yGridMargin = (24 to windowHeight by 24)
  val xGridMargin = (24 to windowWidth by 24)

  // colors
  val textColor = new Color(0, 184, 148)
  val backgroundColor = new Color(223, 230, 233)
  val tankColor = new Color(214, 48, 49)
  val gridColor = new Color(178, 190, 195)

  val game = new AbstractUI

  override def top: Frame = {
    new MainFrame {
      title = "circle-tank-viewer"
      contents = mainPanel
    }
  }

  def onPaint(g: Graphics2D): Unit = {
    val world = game.view

    world.tanks.foreach {
      case (_, tank) =>
        // draw debug text
        val text = s"${tank.position}, ${tank.velocity}"
        g setColor textColor
        g setFont Font("Arial", Font.Bold, 18)
        g drawString (text, 0, 18)

        // draw tank
        val x = tank.position.x match {
          case x if x - tank.radius <= 0 => 0
          case x if x + tank.radius >= windowWidth => windowWidth - tank.radius
          case x => (x - tank.radius) * tank.radius
        }
        val y = tank.position.y match {
          case y if y - tank.radius <= 0 => 0
          case y if y + tank.radius >= windowHeight => windowHeight - tank.radius
          case y => (y - tank.radius) * tank.radius
        }
        val width = tank.radius
        val height = tank.radius

        g setColor tankColor
        //        g drawOval (x * 6, y * 6, width * 6, height * 6)
        g fillOval (x * 6, y * 6, width * 6, height * 6)
    }
  }

  val mainPanel = new Panel {
    preferredSize = new Dimension(windowWidth + margin, windowHeight + margin)
    focusable = true
    listenTo(keys)
    reactions += {
      case KeyPressed(_, key, _, _) => key match {
        case Key.W => game.input(InputType.Down)
        case Key.A => game.input(InputType.Left)
        case Key.S => game.input(InputType.Up)
        case Key.D => game.input(InputType.Right)
        case Key.Space => game.input(InputType.Stop)
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
        g drawLine (0, y, windowWidth, y)
      }
      xGridMargin.foreach { x =>
        g drawLine (x, 0, x, windowHeight)
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
