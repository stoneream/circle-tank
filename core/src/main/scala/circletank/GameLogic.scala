package circletank

object GameLogic {
  // とりえあず動かすのは1台
  def init(size: (Int, Int)): World = {
    val tanks = Map("tank1" -> Tank(position = Position(4, 4)))
    World(size = size, tanks = tanks)
  }

  def tick(world: World): World = {
    val width = world.size._1
    val height = world.size._2

    // 戦車を動かす
    val newTanks = world.tanks.map {
      case (key, tank) =>
        val pos = tank.position
        val vel = tank.velocity
        val r = tank.radius

        val (fixedNewX, fixedVelX) = pos.x + vel.x match {
          case x if x - r <= 0 => (r, 0)
          case x if x + r > width => (width - r, 0)
          case x => (x, vel.x)
        }

        val (fixedNewY, fixedVelY) = pos.y + vel.y match {
          case y if y - r <= 0 => (r, 0)
          case y if y + r > height => (height - r, 0)
          case y => (y, vel.y)
        }

        val newPos = Position(fixedNewX, fixedNewY)
        val newVel = Vector2(fixedVelX, fixedVelY)

        (key, tank.copy(position = newPos, velocity = newVel))
    }

    world.copy(tanks = newTanks)
  }

  def input(world: World)(inputType: InputType): World = {
    import circletank.InputType._

    // とりえあず1台だけなので
    val newTanks = world.tanks.map {
      case (key, tank) =>
        val newVel = inputType match {
          case Up => Vector2(0, 1)
          case Down => Vector2(0, -1)
          case Right => Vector2(1, 0)
          case Left => Vector2(-1, 0)
          case Stop => Vector2.zero
        }

        val newTank = tank.copy(velocity = newVel)

        (key, newTank)
    }

    world.copy(tanks = newTanks)
  }

  def event(world: World)(event: Event): World = {
    event match {
      case Event.Input(inputType) => input(world)(inputType)
      case Event.Tick => tick(world)
    }
  }
}
