package circletank

object TankLogic {
  def addVelocity(tank: Tank, velocity: Vector2) = {
    val vx = (tank.velocity.x + velocity.x) match {
      case n if n == 0 => 0
      case n if n < -1 => -1
      case n if n > 1 => 1
      case n => n
    }

    val vy = (tank.velocity.y + velocity.y) match {
      case n if n == 0 => 0
      case n if n < -1 => -1
      case n if n > 1 => 1
      case n => n
    }

    tank.copy(velocity = Vector2(vx, vy))
  }
}
