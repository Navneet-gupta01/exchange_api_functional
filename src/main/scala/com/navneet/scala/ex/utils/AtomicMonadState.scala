package com.navneet.scala.ex.utils

import monix.eval.Task
import monix.execution.atomic.{Atomic, AtomicAny}
import scalaz.{MonadState => _, _}
import mtl._
import shims._

class AtomicMonadState[S](atomic: Atomic[S])
  extends MonadState[Task, S] {
  val monad: Monad[Task] = Monad[Task]
  def get: Task[S]                   = Task.delay(atomic.get)
  def set(s: S): Task[Unit]          = Task.delay(atomic.set(s))
  def inspect[A](f: S => A): Task[A] = Task.delay(f(atomic.get))
  def modify(f: S => S): Task[Unit]  = Task.delay(atomic.transform(f))
}

object AtomicMonadState {
  def create[S <: AnyRef](s: S): AtomicMonadState[S] =
    new AtomicMonadState(AtomicAny[S](s))
}