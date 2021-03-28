package circletank.viwer

import circletank.{ Game, InputType, Setting }

import java.awt.event.ActionEvent
import javax.swing.{ AbstractAction, Timer }
import scala.swing.event.{ Key, KeyPressed }
import scala.swing.{ Color, Dimension, Font, Frame, Graphics2D, MainFrame, Panel, SimpleSwingApplication }

object Main extends SimpleSwingApplication {
  val worldSize = (64, 64)
  val scale = 6
  val gridSize = scale * 4 // 戦車半径 4

  val margin = 10
  val windowWidth = 1368
  val windowHeight = 1368

  val yGridMargin = (gridSize to windowHeight by gridSize)
  val xGridMargin = (gridSize to windowWidth by gridSize)

  val gameSetting = Setting(worldSize = worldSize, maxFps = 60)
  val game = Game(gameSetting)

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
        g setColor Colors.textColor
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

        g setColor Colors.tankColor
        g fillOval (x * scale, y * scale, width * scale, height * scale)
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

    private def drawBackground(g: Graphics2D) = {
      g setColor Colors.backgroundColor
      g fillRect (0, 0, size.width, size.height)
    }

    private def drawGrid(g: Graphics2D) = {
      g setColor Colors.gridColor
      yGridMargin.foreach { y =>
        g drawLine (0, y, windowWidth, y)
      }
      xGridMargin.foreach { x =>
        g drawLine (x, 0, x, windowHeight)
      }
    }

    override def paint(g: Graphics2D) = {
      drawBackground(g)
      drawGrid(g)
      onPaint(g)
    }

    val timer = new Timer(gameSetting.cycleDuration, new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        repaint
      }
    })

    timer.start()
  }
}
