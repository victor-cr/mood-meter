package com.codegans.moodmeter.service

import java.util.Date

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter
import com.codegans.moodmeter.service.PresentationsWebService.Message
import com.codegans.moodmeter.util.Library.presentations
import com.codegans.moodmeter.util.LocalJsonProtocol._
import org.squeryl.PrimitiveTypeMode._
import spray.http.MediaTypes._
import spray.http.{HttpEntity, HttpResponse}
import spray.json.pimpAny
import spray.routing.RequestContext

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 14.03.2015 23:31
 */
trait PresentationsWebService {

  def log: LoggingAdapter

  def get(message: Message) = {
    log.debug("Get presentations")

    val now = new Date

    transaction {
      val result = presentations.where(p => p.since <= now and p.till >= now).toList

      message.requestContext.complete(HttpResponse(entity = HttpEntity(`application/json`, result.toJson.compactPrint)))
    }
  }
}

class PresentationsActor extends Actor with ActorLogging with PresentationsWebService {
  def receive = {
    case message: Message => get(message)
  }
}

object PresentationsWebService {

  case class Message(requestContext: RequestContext)

}

