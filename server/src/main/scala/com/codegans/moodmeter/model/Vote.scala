package com.codegans.moodmeter.model

import java.util.Date

import com.codegans.moodmeter.model.Score._

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 31.03.2015 9:14
 */
case class Vote(userKey: Key[User], presentationKey: Key[Presentation], score: Score, timestamp: Date = new Date()) {
  override def toString: String = "[" + timestamp + "] " + presentationKey + " - " + score
}
