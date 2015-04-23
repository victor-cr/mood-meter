package com.codegans.moodmeter.model

import java.util.Date

import org.squeryl.KeyedEntity
import spray.http.Uri

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 04.04.2015 10:11
 */
case class Presentation(id: Key[Presentation],
                        name: String,
                        presenter: String,
                        description: String,
                        since: Date,
                        till: Date,
                        image: Uri) extends KeyedEntity[Key[Presentation]] {

  def withId(id: Key[Presentation]) = Presentation(id, name, presenter, description, since, till, image)

  override def toString: String = id + " [" + name + "]"
}
