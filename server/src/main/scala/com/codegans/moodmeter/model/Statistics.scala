package com.codegans.moodmeter.model

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 02.04.2015 9:25
 */
case class Statistics(good: Long, ok: Long, bad: Long) {
  override def toString: String = "+" + good + "," + ok + ",-" + bad
}