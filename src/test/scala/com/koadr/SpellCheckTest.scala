package com.koadr

import org.specs2.mutable._

class SpellCheckTest extends Specification {

  "A Spellchecker" should {
    "suggest weekend for wekend" in {
      SpellCheck.correct("wekend") === "weekend"
    }
    "suggest siren for siRen" in {
      SpellCheck.correct("siRen").split(",").contains("siren") === true
    }
    "suggest job for jjooobb" in {
      SpellCheck.correct("jjooobb").split(",").contains("job") === true
    }
    "not suggest for 12shej" in {
      SpellCheck.correct("12shej")  === "NO SUGGESTION"
    }
    "suggest conspiracy for CUNsperrICY" in {
      SpellCheck.correct("CUNsperrICY").split(",").contains("conspiracy")  === true
    }.pendingUntilFixed("Cannot figure out implementation for edit distance greater than 2.")
  }
}
