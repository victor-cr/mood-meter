package com.codegans.moodmeter.model

import org.squeryl.KeyedEntity

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 04.04.2015 10:08
 */
case class User(id: Key[User], name: String) extends KeyedEntity[Key[User]] {
  override def toString: String = id + " [" + name + "]"
}