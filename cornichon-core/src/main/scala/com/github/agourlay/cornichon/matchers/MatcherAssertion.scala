package com.github.agourlay.cornichon.matchers

import cats.syntax.validated._
import cats.data.ValidatedNel
import com.github.agourlay.cornichon.steps.regular.assertStep.Assertion
import cats.syntax.either._
import com.github.agourlay.cornichon.core.{ CornichonError, Done }
import com.github.agourlay.cornichon.core.Done._
import com.github.agourlay.cornichon.json.JsonPath
import io.circe.Json

case class MatcherAssertion(negate: Boolean, m: Matcher, input: Json, jsonPath: JsonPath) extends Assertion {

  lazy val validated: ValidatedNel[CornichonError, Done] =
    jsonPath.runStrict(input) match {
      case Left(e) =>
        MatcherAssertionEvaluationError(m, input, e).invalidNel
      case Right(focus) =>
        Either.catchNonFatal(m.predicate(focus))
          .leftMap(e => MatcherAssertionEvaluationError(m, focus, CornichonError.fromThrowable(e)))
          .fold[ValidatedNel[CornichonError, Done]](
            errors => errors.invalidNel,
            matcherResult =>
              // XNOR condition for not negate
              if (matcherResult == !negate) validDone else MatcherAssertionError(m, focus, negate).invalidNel
          )
    }
}

case class MatcherAssertionEvaluationError(m: Matcher, input: Json, error: CornichonError) extends CornichonError {
  val baseErrorMessage = s"evaluation of matcher '${m.key}' (${m.description}) failed for input '${input.spaces2}'"
  override val causedBy = error :: Nil
}

case class MatcherAssertionError(m: Matcher, input: Json, negate: Boolean) extends CornichonError {
  val baseErrorMessage = s"matcher '${m.key}' (${m.description}) ${if (negate) "was expected to fail" else "failed"} for input '${input.spaces2}'"
}
