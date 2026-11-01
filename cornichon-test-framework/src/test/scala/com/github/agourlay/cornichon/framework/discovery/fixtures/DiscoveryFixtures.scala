package com.github.agourlay.cornichon.framework.discovery.fixtures

import com.github.agourlay.cornichon.CornichonFeature

// Fixtures for `MainRunnerDiscoverySpec`: every shape a feature class can take in a scanned package.

class DirectFeature extends CornichonFeature {

  def feature = Feature("discovery direct") {
    Scenario("noop") {
      When I save("a" -> "b")
    }
  }

}

trait FeatureTrait extends CornichonFeature

class ViaTraitFeature extends FeatureTrait {

  def feature = Feature("discovery via trait") {
    Scenario("noop") {
      When I save("a" -> "b")
    }
  }

}

abstract class AbstractFeature extends CornichonFeature

class ViaAbstractFeature extends AbstractFeature {

  def feature = Feature("discovery via abstract class") {
    Scenario("noop") {
      When I save("a" -> "b")
    }
  }

}

class NotAFeature
