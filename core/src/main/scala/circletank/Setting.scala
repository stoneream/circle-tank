package circletank

case class Setting(
  worldSize: (Int, Int),
  maxFps: Int) {
  val cycleDuration = 1000 / maxFps // ms
}
