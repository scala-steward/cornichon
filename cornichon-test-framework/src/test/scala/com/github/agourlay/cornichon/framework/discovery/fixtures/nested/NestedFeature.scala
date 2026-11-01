package com.github.agourlay.cornichon.framework.discovery.fixtures.nested

import com.github.agourlay.cornichon.CornichonFeature

class NestedFeature extends CornichonFeature {

  def feature = Feature("discovery nested package") {
    Scenario("noop") {
      When I save("a" -> "b")
    }
  }

}
