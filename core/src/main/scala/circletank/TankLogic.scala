package circletank

object TankLogic {
  def addVelocity(tank: Tank, velocity: Vector2) = {
    val vx = (tank.velocity.x + velocity.x) match {
      case n if n == 0 => 0
      case n if n < -1.0 => -1.0
      case n if n > 1.0 => 1.0
      case n => n
    }

    val vy = (tank.velocity.y + velocity.y) match {
      case n if n == 0 => 0
      case n if n < -1.0 => -1.0
      case n if n > 1.0 => 1.0
      case n => n
    }

    tank.copy(velocity = Vector2(vx, vy))
  }

  //  def toZero(tank: Tank) = {
  //    val x = tank.velocity.x - 0.01 match {
  //      case n if n >= 0 => n
  //      case n if n < 0 => 0
  //    }
  //
  //    val y = tank.velocity.y - 0.01 match {
  //      case n if n >= 0 => n
  //      case n if n < 0 => 0
  //    }
  //
  //    tank.copy(velocity = Vector2(x, y))
  //  }
}
