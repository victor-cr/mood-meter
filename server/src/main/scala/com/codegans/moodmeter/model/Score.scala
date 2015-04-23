package com.codegans.moodmeter.model

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 31.03.2015 9:15
 */
object Score extends Enumeration {
  type Score = Value

  val Bad = Value(-1, "Bad")
  val Ok = Value(0, "Ok")
  val Good = Value(1, "Good")
}
