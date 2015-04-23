package com.codegans.moodmeter

import com.codegans.moodmeter.service.RouteWebService
import org.specs2.mutable.Specification
import spray.http.StatusCodes.MethodNotAllowed
import spray.testkit.Specs2RouteTest

class RouteWebServiceSpec extends Specification with Specs2RouteTest with RouteWebService {
  def actorRefFactory = system

  def log = system.log

  "RouteWebService" should {

    "return a registry of all available methods for GET requests to the root path" in {
      Get("/") ~> route ~> check {
        responseAs[String] must contain("Registry of available methods")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> route ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/") ~> sealRoute(route) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}