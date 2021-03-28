package circletank

// 1p とりあえず洗車を動かすことだけを考える
object GameLogic {
  def init: World = {
    val tanks = Map("tank1" -> Tank())
    World(tanks = tanks)
  }

  def tick(world: World)(elapsedMillis: Int): World = {
    // 戦車を動かす
    val newTanks = world.tanks.map {
      case (key, tank) =>
        val pos = tank.position
        val vel = tank.velocity
        val acc = tank.acceleration

        val newX = pos.x + (vel.x * acc.x * elapsedMillis)
        val newY = pos.y + (vel.y * acc.y * elapsedMillis)

        val fixedNewX = newX match {
          case x if x < 0 => 0
          case x if x > world.size._1 => world.size._1
          case x => x
        }

        val fixedNewY = newY match {
          case y if y < 0 => 0
          case y if y > world.size._2 => world.size._2
          case y => y
        }

        val newPos = Position(fixedNewX, fixedNewY)

        println(s"$pos , $vel")

        // 動かした後に止める
        //        val newTank = TankLogic.toZero(tank).copy(position = newPos)

        (key, tank.copy(position = newPos))
    }

    world.copy(tanks = newTanks)
  }

  def input(world: World)(inputType: InputType): World = {
    import circletank.InputType._

    // とりえあず1台だけなので
    val newTanks = world.tanks.map {
      case (key, tank) =>
        val newTank = inputType match {
          case Up => TankLogic.addVelocity(tank, Vector2(0, 0.02))
          case Down => TankLogic.addVelocity(tank, Vector2(0, -0.02))
          case Right => TankLogic.addVelocity(tank, Vector2(0.02, 0))
          case Left => TankLogic.addVelocity(tank, Vector2(-0.02, 0))
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
