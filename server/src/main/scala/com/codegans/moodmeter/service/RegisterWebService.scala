package com.codegans.moodmeter.service

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter
import com.codegans.moodmeter.model.{Key, Presentation, User}
import com.codegans.moodmeter.service.RegisterWebService.{PresentationMessage, UserMessage}
import com.codegans.moodmeter.util.Library._
import org.squeryl.PrimitiveTypeMode._
import spray.http.MediaTypes._
import spray.http.{HttpEntity, HttpResponse}
import spray.routing.RequestContext

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 14.03.2015 23:31
 */
trait RegisterWebService {
  def log: LoggingAdapter

  def registerPresentation(message: PresentationMessage) = {
    val name = message.presentation.name

    log.debug("Register new presentation: {}", name)

    transaction {
      try {
        val maybe = presentations.where(p => p.name === name).headOption

        if (maybe.isDefined) {
          throw new IllegalArgumentException("The presentation [" + name + "] was already registered")
        }

        val key = Key[Presentation]()

        val p = message.presentation.withId(key)

        presentations.insert(p)

        message.requestContext.complete(HttpResponse(entity = HttpEntity(`text/plain`, key.value.toString)))
      } catch {
        case e: Exception => message.requestContext.failWith(e)
      }
    }
  }

  def registerUser(message: UserMessage) = {
    log.debug("Register new user: {}", message.name)

    transaction {
      val key = Key[User]()

      val u = User(key, message.name)

      users.insert(u)

      message.requestContext.complete(HttpResponse(entity = HttpEntity(`text/plain`, key.value.toString)))
    }
  }
}

class RegisterActor extends Actor with ActorLogging with RegisterWebService {
  def receive = {
    case message: PresentationMessage => registerPresentation(message)
  }
}

object RegisterWebService {

  case class PresentationMessage(requestContext: RequestContext, presentation: Presentation)

  case class JeeConfPresentationMessage(requestContext: RequestContext, url: String)

  case class UserMessage(requestContext: RequestContext, name: String)

}

