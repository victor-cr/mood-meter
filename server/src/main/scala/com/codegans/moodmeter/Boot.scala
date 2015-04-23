package com.codegans.moodmeter

import javax.naming.InitialContext
import javax.sql.DataSource

import akka.actor.{ActorSystem, Props}
import com.codegans.moodmeter.service.RouteActor
import com.codegans.moodmeter.util.Library
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter
import org.squeryl.{Session, SessionFactory}
import spray.servlet.WebBoot

// this class is instantiated by the servlet initializer
// it needs to have a default constructor and implement
// the spray.servlet.WebBoot trait
class Boot extends WebBoot {

  // we need an ActorSystem to host our application in
  val system = ActorSystem("mood-meter")

  // the service actor replies to incoming HttpRequests
  val serviceActor = system.actorOf(Props[RouteActor])

  val ds = new InitialContext().lookup("java:comp/env/jdbc/voteDS").asInstanceOf[DataSource]

  SessionFactory.concreteFactory = Some(() => Session.create(ds.getConnection, new H2Adapter))

  transaction {
    Library.create
    system.log.info("Database schema created")
  }

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }

  system.log.info("Application started up")
}
