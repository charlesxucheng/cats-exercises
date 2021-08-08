import cats.Semigroup
import cats.data.Validated
import cats.syntax.either._
import cats.syntax.semigroup._ // for |+|

final case class CheckF[E, A](func: A => Either[E, A]) {
  def apply(a: A): Either[E, A] =
    func(a)

  def and(that: CheckF[E, A])
         (implicit s: Semigroup[E]): CheckF[E, A] =
    CheckF { a =>
      (this (a), that(a)) match {
        case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
        case (Left(e), Right(_)) => e.asLeft
        case (Right(_), Left(e)) => e.asLeft
        case (Right(_), Right(_)) => a.asRight
      }
    }
}

import cats.instances.list._ // for Semigroup

val a: CheckF[List[String], Int] =
  CheckF { v =>
    if (v > 2) v.asRight
    else List("Must be > 2").asLeft
  }
val b: CheckF[List[String], Int] =
  CheckF { v =>
    if (v < -2) v.asRight
    else List("Must be < -2").asLeft
  }
val check: CheckF[List[String], Int] =
  a and b

check(5)

check(0)

check(-8)

import cats.Semigroup
import cats.data.Validated
import cats.syntax.semigroup._ // for |+|
import cats.syntax.apply._ // for mapN
import cats.data.Validated._ // for Valid and Invalid

sealed trait Check[E, A] {

  import Check._

  def and(that: Check[E, A]): Check[E, A] = And(this, that)

  def or(that: Check[E, A]): Check[E, A] = Or(this, that)

  def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
    this match {
      case Pure(func) =>
        func(a)
      case And(left, right) =>
        (left(a), right(a)).mapN((_, _) => a)
      case Or(left, right) =>
        left(a) match {
          case Valid(a) => Valid(a)
          case Invalid(e1) =>
            right(a) match {
              case Valid(a) => Valid(a)
              case Invalid(e2) => Invalid(e1 |+| e2)
            }
        }
    }
}

// ADT
object Check {
  final case class And[E, A](left: Check[E, A], right: Check[E, A])
    extends Check[E, A]

  final case class Or[E, A](left: Check[E, A], right: Check[E, A])
    extends Check[E, A]

  final case class Pure[E, A](func: A => Validated[E, A]) extends Check[E, A]
}

