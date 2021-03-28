package circletank

import akka.actor.typed.{ ActorRef, Behavior }
import akka.actor.typed.scaladsl.Behaviors

sealed trait StateMessage

object StateMessage {

  final case class UpdateState(event: Event) extends StateMessage

  final case object Terminate extends StateMessage

  final case class GetState(replyTo: ActorRef[World]) extends StateMessage

}

object StateActor {
  def apply(init: World): Behavior[StateMessage] = updateState(init)

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