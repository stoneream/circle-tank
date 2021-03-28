package circletank

case class Tank(
  velocity: Vector2 = Vector2.zero,
  position: Position = Position.zero) extends Material {
  val name = "tank"
  val radius: Int = 4
}