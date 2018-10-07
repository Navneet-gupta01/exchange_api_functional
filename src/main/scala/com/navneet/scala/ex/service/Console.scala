package com.navneet.scala.ex.service

trait Console[F[_]] {
  def readLine: F[String]
  def printLn(line: String): F[Unit]

}
