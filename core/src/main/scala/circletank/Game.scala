package circletank

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern.Askable

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class Game(setting: Setting) {
  private val initWorld = GameLogic.init(setting.worldSize)
  private val system = ActorSystem(StateActor(initWorld), "circle-tank-system")
  private val scheduler = system.scheduler
  private val timeout = akka.util.Timeout(5.seconds)

  scheduler.scheduleAtFixedRate(0.milli, setting.cycleDuration.millis) {
    new Runnable {
      override def run(): Unit = {
        system ! StateMessage.UpdateState(Event.Tick)
      }
    }
  }(system.executionContext)

  def input(inputType: InputType): Unit = {
    system ! StateMessage.UpdateState(Event.Input(inputType))
  }

  def terminate = {
    system ! StateMessage.Terminate
    system.terminate
  }

  def view = Await.result(system.ask(StateMessage.GetState)(timeout, scheduler).mapTo[World], setting.cycleDuration.millis)
}

object Game {
  def apply(setting: Setting) = new Game(setting)
}
