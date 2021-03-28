package circletank

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

sealed trait StateMessage

object StateMessage {

  final case class UpdateState(event: Event) extends StateMessage

  final case object Terminate extends StateMessage

  final case class GetState(replyTo: ActorRef[World]) extends StateMessage

}

object StateActor {
  def apply(): Behavior[StateMessage] = updateState(GameLogic.init)

  def updateState(s1: World): Behavior[StateMessage] = receive(s1)

  def receive(s0: World): Behaviors.Receive[StateMessage] = Behaviors.receiveMessage {
    case StateMessage.UpdateState(event) => updateState(GameLogic.event(s0)(event))
    case StateMessage.GetState(replyTo) => {
      replyTo ! s0
      Behaviors.same
    }
    case StateMessage.Terminate => Behaviors.stopped
  }
}

class AbstractUI {
  private val system = ActorSystem(StateActor(), "circle-tank-system")
  private val scheduler = system.scheduler
  private val timeout = akka.util.Timeout(5.seconds)
  val maxFps = 30
  val cycleDuration = 1000 / maxFps

  scheduler.scheduleAtFixedRate(0.milli, cycleDuration.millis) {
    new Runnable {
      override def run(): Unit = {
        system ! StateMessage.UpdateState(Event.Tick(cycleDuration))
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

  def view = Await.result(system.ask(StateMessage.GetState)(timeout, scheduler).mapTo[World], cycleDuration.millis)
}
