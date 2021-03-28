package circletank

case class Tank(
  velocity: Vector2 = Vector2.zero,
  position: Position = Position.zero) extends Material {
  val name = "tank"
  val acceleration = Vector2(0.01, 0.01)
  val radius: Double = 4
}