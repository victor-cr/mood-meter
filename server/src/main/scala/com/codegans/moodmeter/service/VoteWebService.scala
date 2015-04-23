package com.codegans.moodmeter.service

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter
import com.codegans.moodmeter.model.Score.Score
import com.codegans.moodmeter.model.{Vote, Key, Presentation, User}
import com.codegans.moodmeter.service.VoteWebService.Message
import com.codegans.moodmeter.util.Library._
import org.squeryl.PrimitiveTypeMode._
import spray.http.{HttpResponse, StatusCodes}
import spray.routing.{MalformedRequestContentRejection, RequestContext}

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 15.03.2015 10:04
 */
trait VoteWebService {
  def log: LoggingAdapter

  def process(message: Message) = {
    val vote = message.vote

    log.debug("Vote {}: {}", vote.score, vote.presentationKey)

    transaction {
      val maybeUser = users.where(u => u.id.value === vote.userKey.value).headOption
      val maybePresentation = users.where(u => u.id.value === vote.userKey.value).headOption

      if (maybeUser.isEmpty) {
        message.requestContext.reject(MalformedRequestContentRejection(s"User identification ${vote.userKey} does not exist"))
      } else if (maybeUser.isEmpty || maybePresentation.isEmpty) {
        message.requestContext.reject(MalformedRequestContentRejection(s"Presentation identification ${vote.presentationKey} does not exist"))
      } else {
        votes.insert(Vote(vote.userKey, vote.presentationKey, vote.score))

        log.info("Successfully voted")

        message.requestContext.complete(HttpResponse(StatusCodes.Created))
      }
    }
  }
}

class VoteActor extends Actor with ActorLogging with VoteWebService {
  def receive = {
    case message: Message => process(message)
  }
}

object VoteWebService {

  case class Message(requestContext: RequestContext, vote: Vote)

}
