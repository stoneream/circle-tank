package circletank

case class Vector2(x: Int, y: Int) {
  def add(x: Int, y: Int) = this.copy(this.x + x, this.y + y)
  def add(that: Vector2) = this.copy(this.x + that.x, this.y + that.y)
}
object Vector2 {
  def zero = Vector2(0, 0)
}

case class Position(x: Int, y: Int) {}
object Position {
  def zero = Position(0, 0)
}

trait Material {
  val name: String

  // 速度
  val velocity: Vector2

  // 位置
  val position: Position

  // 半径（当たり判定用）
  val radius: Int
}
