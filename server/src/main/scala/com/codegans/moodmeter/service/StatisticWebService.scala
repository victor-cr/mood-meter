package com.codegans.moodmeter.service

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter
import com.codegans.moodmeter.model.Score.Score
import com.codegans.moodmeter.model.{Key, Presentation, Score, Statistics}
import com.codegans.moodmeter.service.StatisticWebService.Message
import com.codegans.moodmeter.util.Library._
import com.codegans.moodmeter.util.LocalJsonProtocol._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Query
import org.squeryl.dsl.GroupWithMeasures
import spray.http.MediaTypes._
import spray.http.{HttpEntity, HttpResponse}
import spray.json.pimpAny
import spray.routing.RequestContext

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 15.03.2015 10:04
 */
trait StatisticWebService {
  def log: LoggingAdapter

  def process(message: Message) = {
    log.debug("Load statistics: ", message.key)

    val key = message.key
    val timestamp = new DateType(System.currentTimeMillis() - 5 * 60 * 1000)

    transaction {
      val query: Query[GroupWithMeasures[Score, Long]] = from(votes)(v =>
        where(v.presentationKey.value === key.value and v.timestamp >= timestamp)
          groupBy (v.score)
          compute (count))

      val result = query.map(m => (m.key, m.measures)).toMap

      val statistics = Statistics(
        result.getOrElse(Score.Good, 0),
        result.getOrElse(Score.Ok, 0),
        result.getOrElse(Score.Bad, 0)
      )

      log.info("Query result is: {}", statistics)

      message.requestContext.complete(
        HttpResponse(entity = HttpEntity(`application/json`, statistics.toJson.toString()))
      )
    }
  }
}

class StatisticActor extends Actor with ActorLogging with StatisticWebService {
  def receive = {
    case message: Message => process(message)
  }
}

object StatisticWebService {

  case class Message(requestContext: RequestContext, key: Key[Presentation])

}
