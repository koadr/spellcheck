package com.koadr


object Main {
  def main(args: Array[String]) {
    var ok = true

    println("---------------------------------------------")
    println("-------SPELL CHECKER-------------------------")
    println("---------------------------------------------")

    while(ok) {
      val ln = readLine()
      ok = ln != "STOP"
      if (ok) println(SpellCheck.correct(ln))
    }
  }
}
