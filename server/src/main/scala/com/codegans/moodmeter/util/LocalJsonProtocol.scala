package com.codegans.moodmeter.util

import java.util.{Date, UUID}

import com.codegans.moodmeter.model.Score.Score
import com.codegans.moodmeter.model._
import spray.http.Uri
import spray.json._

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 15.03.2015 10:26
 */
object LocalJsonProtocol extends DefaultJsonProtocol {

  implicit val keyPresentationFormat = new KeyFormat[Presentation]
  implicit val keyUserFormat = new KeyFormat[User]
  implicit val dateFormat = JsDateFormat
  implicit val uriFormat = JsUriFormat
  implicit val scoreFormat = JsScoreFormat
  implicit val userFormat = jsonFormat2(User)
  implicit val presentationFormat = jsonFormat7(Presentation)
  implicit val statisticsFormat = jsonFormat3(Statistics)
  implicit val voteFormat = jsonFormat4(Vote)
//  implicit val listPresentationsFormat = listFormat[Presentation]

  class KeyFormat[T <: Product] extends JsonFormat[Key[T]] {
    def write(x: Key[T]) = JsString(x.value.toString)

    def read(json: JsValue) = json match {
      case JsString(x) => Key[T](UUID.fromString(x))
      case x => deserializationError("Expected UUID as JsStrin, but got " + x)
    }
  }

  object JsDateFormat extends JsonFormat[Date] {
    def write(x: Date) = JsNumber(x.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(x) => new Date(x.longValue())
      case x => deserializationError("Expected Long as JsNumber, but got " + x)
    }
  }

  object JsUriFormat extends JsonFormat[Uri] {
    def write(x: Uri) = JsString(x.toString())

    def read(json: JsValue) = json match {
      case JsString(x) => Uri.apply(x)
      case x => deserializationError("Expected Uri as JsString, but got " + x)
    }
  }

  object JsScoreFormat extends JsonFormat[Score] {
    def write(x: Score) = JsString(x.toString)

    def read(json: JsValue) = json match {
      case JsString(x) => Score.withName(x)
      case x => deserializationError("Expected Score as JsString, but got " + x)
    }
  }

}
