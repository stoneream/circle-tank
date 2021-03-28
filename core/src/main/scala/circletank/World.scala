package circletank

case class World(
  size: (Int, Int) = (700, 400),
  tanks: Map[String, Tank])

