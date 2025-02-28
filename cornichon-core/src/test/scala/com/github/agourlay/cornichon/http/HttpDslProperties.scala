package com.github.agourlay.cornichon.http

import com.github.agourlay.cornichon.core.{ Scenario, ScenarioRunner, Session, SessionKey }
import com.github.agourlay.cornichon.dsl.SessionSteps.SessionStepBuilder
import com.github.agourlay.cornichon.testHelpers.IOSpec
import org.scalacheck.{ Gen, Properties }
import org.scalacheck.Prop._

class HttpDslProperties extends Properties("HttpDsl") with IOSpec {

  private val ops = new HttpDslOps {}

  property("removeFromWithHeaders handle no 'with-headers'") =
    forAll(Gen.alphaStr) { header =>
      ops.removeFromWithHeaders(header)(Session.newEmpty) == Right(Session.newEmpty)
    }

  property("save_body accepts to save any String as a body") =
    forAll { (input: String) =>
      val session = Session.newEmpty.addValuesUnsafe(HttpDsl.lastBodySessionKey.name -> input)
      val saveStep = HttpDsl.save_body("new-key")
      val assertStep = SessionStepBuilder(SessionKey("new-key")).is(input)
      val s = Scenario("scenario with any save_body", saveStep :: assertStep :: Nil)
      val t = awaitIO(ScenarioRunner.runScenario(session)(s))
      t.isSuccess
    }
}
