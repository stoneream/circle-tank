package circletank

// 1p とりあえず洗車を動かすことだけを考える
object GameLogic {
  def init: World = {
    val tanks = Map("tank1" -> Tank(position = Position(4, 4)))
    World(tanks = tanks)
  }

  def tick(world: World)(elapsedMillis: Int): World = {
    // 戦車を動かす
    val newTanks = world.tanks.map {
      case (key, tank) =>
        val pos = tank.position
        val vel = tank.velocity

        val newX = pos.x + vel.x
        val newY = pos.y + vel.y

        val (fixedNewX, fixedVelX) = newX match {
          case x if x - tank.radius <= 0 - tank.radius => (tank.radius, 0)
          case x if x + tank.radius > world.size._1 => (world.size._1 - tank.radius, 0)
          case x => (x, vel.x)
        }

        val (fixedNewY, fixedVelY) = newY match {
          case y if y - tank.radius <= 0 - tank.radius => (tank.radius, 0)
          case y if y + tank.radius > world.size._2 => (world.size._2 - tank.radius, 0)
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
        val newTank = inputType match {
          case Up => TankLogic.addVelocity(tank, Vector2(0, 1))
          case Down => TankLogic.addVelocity(tank, Vector2(0, -1))
          case Right => TankLogic.addVelocity(tank, Vector2(1, 0))
          case Left => TankLogic.addVelocity(tank, Vector2(-1, 0))
          case Stop => tank.copy(velocity = Vector2.zero)
        }
        (key, newTank)
    }

    world.copy(tanks = newTanks)
  }

  def event(world: World)(event: Event): World = {
    event match {
      case Event.Input(inputType) => input(world)(inputType)
      case Event.Tick(elapsedMillis) => tick(world)(elapsedMillis)
    }
  }
}
