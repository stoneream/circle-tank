package circletank

case class World(
  size: (Int, Int) = (64, 64),
  tanks: Map[String, Tank])

