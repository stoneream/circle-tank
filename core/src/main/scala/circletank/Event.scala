package circletank

sealed trait Event {
  val name: String
}

object Event {
  case class Tick(elapsedMillis: Int) extends Event {
    val name = "tick"
  }
  case class Input(inputType: InputType) extends Event {
    val name = "input"
  }
}

sealed trait InputType {
  val name: String
}
object InputType {
  final case object Right extends InputType {
    val name = "right"
  }
  final case object Left extends InputType {
    val name = "left"
  }
  final case object Up extends InputType {
    val name = "up"
  }
  final case object Down extends InputType {
    val name = "down"
  }
  final case object Stop extends InputType {
    val name = "stop"
  }
}