package com.github.agourlay.cornichon.dsl

import scala.language.experimental.{ macros ⇒ `scalac, please just let me do it!` }

case class BodyElementCollector[Body, Result](fn: List[Body] ⇒ Result) {
  def apply(body: ⇒ Body): Result = macro BodyElementCollectorMacro.collectImpl
  def apply(body: ⇒ Seq[Body]): Result = macro BodyElementCollectorMacro.collectImpl

  def get(body: List[Body]): Result = fn(body)
}