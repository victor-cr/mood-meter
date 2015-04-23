package com.codegans.moodmeter.util

import com.codegans.moodmeter.model.{Vote, Presentation, User}
import org.squeryl.Schema

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 15.03.2015 9:52
 */
object Library extends Schema {
  val users = table[User]
  val presentations = table[Presentation]
  var votes = table[Vote]
}
