package com.codegans.moodmeter.service

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter
import com.codegans.moodmeter.service.IndexWebService.IndexMessage
import spray.http.MediaTypes._
import spray.http.{HttpEntity, HttpResponse}
import spray.routing.RequestContext

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 14.03.2015 21:03
 */
trait IndexWebService {
  lazy val index = HttpResponse(
    entity = HttpEntity(`text/html`,
      <html>
        <body>
          <h1>Mood meter application</h1>
          <p>Registry of available methods:</p>
          <ul>
            <li>
              <i>/stat/(key)</i>
              [GET] - Get statistics</li>
            <li>
              <i>/vote</i>
              [POST] - Vote action</li>
            <li>
              <i>/register/user</i>
              [POST] - Get unique client ID</li>
            <li>
              <i>/register/presentation</i>
              [POST] - Get unique presentation ID</li>
          </ul>
        </body>
      </html>.toString()
    )
  )

  def log: LoggingAdapter

  def process(message: IndexMessage) = {
    log.debug("Show the registry of all available URI points")

    message.requestContext.complete(index)
  }
}

class IndexActor extends Actor with ActorLogging with IndexWebService {
  def receive = {
    case message: IndexMessage => process(message)
  }
}

object IndexWebService {

  case class IndexMessage(requestContext: RequestContext)

}
