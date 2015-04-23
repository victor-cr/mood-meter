package com.codegans.moodmeter.model

import java.util.UUID

/**
 * JavaDoc here
 *
 * @author Victor Polischuk
 * @since 15.03.2015 9:55
 */
case class Key[T <: Product](value: UUID = UUID.randomUUID()) extends AnyVal
