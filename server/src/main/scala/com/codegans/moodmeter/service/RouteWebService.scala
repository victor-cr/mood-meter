package com.codegans.moodmeter.service

import akka.actor._
import akka.event.LoggingAdapter
import com.codegans.moodmeter.model.{Key, Presentation, Vote}
import com.codegans.moodmeter.service.IndexWebService.IndexMessage
import com.codegans.moodmeter.service.RegisterWebService.{PresentationMessage, UserMessage}
import com.codegans.moodmeter.util.LocalJsonProtocol._
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 14.03.2015 15:55
 */
trait RouteWebService extends HttpService {
  def log: LoggingAdapter

  val serviceIndex = (path("") & get) {
    requestContext => {
      log.info("Index Message: {}", requestContext)

      actorRefFactory.actorOf(Props[IndexActor]) ! new IndexMessage(requestContext)
    }
  }

  val serviceRegisterUser = (path("register/user") & post & clientIP) {
    ip => requestContext => {
      log.info("Register User Message: {}", requestContext)

      actorRefFactory.actorOf(Props[RegisterActor]) ! new UserMessage(requestContext, ip.toString())
    }
  }

  val serviceRegisterPresentation = (path("register/presentation") & post) {
    entity(as[Presentation]) {
      presentation => requestContext => {
        log.info("Register Presentation Message: {}", requestContext)

        val message = new PresentationMessage(requestContext, presentation)

        actorRefFactory.actorOf(Props[RegisterActor]) ! message
      }
    }
  }

  val serviceVote = (path("vote") & post) {
    entity(as[Vote]) {
      vote => requestContext => {
        log.info("Vote Message: {}", requestContext)

        actorRefFactory.actorOf(Props[VoteActor]) ! new VoteWebService.Message(requestContext, vote)
      }
    }
  }

  val serviceStatistics = (path("stat" / JavaUUID) & get) {
    uuid => requestContext => {
      log.info("Statistics Message: {}", requestContext)

      actorRefFactory.actorOf(Props[StatisticActor]) ! new StatisticWebService.Message(requestContext, Key[Presentation](uuid))
    }
  }

  val servicePresentations = (path("presentations") & get) {
    requestContext => {
      log.info("Presentations Message: {}", requestContext)

      actorRefFactory.actorOf(Props[PresentationsActor]) ! new PresentationsWebService.Message(requestContext)
    }
  }

  val route = serviceIndex ~
    serviceRegisterUser ~
    serviceRegisterPresentation ~
    serviceVote ~
    serviceStatistics ~
    servicePresentations
}

class RouteActor extends Actor with ActorLogging with RouteWebService {
  def actorRefFactory = context

  def receive = runRoute(route)
}
