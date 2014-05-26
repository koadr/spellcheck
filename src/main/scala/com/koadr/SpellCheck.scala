package com.koadr

import scala.io.Source
import java.io.File


object SpellCheck {
  val alphabet = 'a' to 'z'

  val dictionary =
    Source.fromFile(
      new File("src/main/resources/us/US.dic"),
      "iso-8859-1"
    ).getLines().toSet

  def actual(words: Set[String]) = Set.empty ++ (for(w <- words if dictionary contains w) yield w)

  // Edit distance algorithm as outlined by Peter Norvig http://norvig.com/spell-correct.html
  def edit1(word: String) = {
    Set.empty ++
      (for (i <- 0 until word.length) yield (word take i) + (word drop (i + 1))) ++ // Deletes
      (for (i <- 0 until word.length - 1) yield (word take i) + word(i + 1) + word(i) + (word drop (i + 2))) ++ // Transpositions
      (for (i <- 0 until word.length; j <- alphabet) yield (word take i) + j + (word drop (i+1))) ++ // Alterations
      (for (i <- 0 until word.length; j <- alphabet) yield (word take i) + j + (word drop i)) // Inserts
  }

  def edit2(edit1Corrections: Set[String]) = {
    for {
      e1 <- edit1Corrections
      e2 <- edit1(e1)
    } yield e2
  }

  // TODO Add Implementation for Edit Distances > 2
  def correct(word: String): String = {
    // First Check if word is permissable
    val nonPermissable = "[\\d|\\W]".r findFirstMatchIn word map (_ => "NO SUGGESTION")
    nonPermissable getOrElse {
      val clean = sanitize(word)

      lazy val e1 = edit1(clean)
      lazy val e2 = edit2(e1)

      val corrections: Option[Set[String]] = (dictionary.find(_ == clean) map{Set(_)})  orElse
        Option(actual(e1)).filter(!_.isEmpty) orElse
        Option(actual(e2)).filter(!_.isEmpty)

      corrections match {
        case Some(suggestions) => suggestions.mkString(",")
        case None => "NO SUGGESTION"
      }
    }

  }

  def sanitize(word: String): String = {
    /*
     * Make the following assumptions for sanitization
     * 1) An English word can only begin with repeating 'l', 's', 'u', or a vowel. Keep only one of such letters
     * 2) An English word cannot have a repeating letter 3 or more times. Only two should be kept
     *
     * */

     // Lowercase
     val lowerCase = word.toLowerCase
     // Keep only two repeating letters if more than 3 exist
     val no3 = lowerCase.replaceAll("([a-z])\\1{2,}", "$1$1")
     // Keep only beginning letter where repeated isn't permissable
     no3.replaceAll("^([^aeiouls])\\1{1,}", "$1")

   }

}
