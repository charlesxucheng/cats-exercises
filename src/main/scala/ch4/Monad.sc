trait Monad[F[_]] {
  def pure[A](value: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
  def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)(a => pure(func(a)))
}

import cats.Monad
import cats.instances.list._
import cats.instances.option._ // for Monad
import cats.instances.vector._ // for Monad

val opt1 = Monad[Option].pure(3)
val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
val opt3 = Monad[Option].map(opt2)(a => 100 * a)
val list1 = Monad[List].pure(3)
val list2 = Monad[List].flatMap(list1)(a => List(a, a*10))
val list3 = Monad[List].map(list2)(a => a + 123)

Monad[Option].flatMap(Option(1))(a => Option(a*2))
Monad[List].flatMap(List(1, 2, 3))(a => List(a, a*10))
Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a*10))

import cats.instances.future._ // for Monad
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
val fm = Monad[Future]

val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
Await.result(future, 1.second)

// Monad Syntax
import cats.instances.option._ // for Monad
import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure
1.pure[Option]
1.pure[List]

import cats.syntax.functor._
import cats.syntax.flatMap._
def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  for {
    x <- a
    y <- b
  } yield x*x + y*y
//  a.flatMap(x => b.map(y => x*x + y*y))

sumSquare(Option(1), Option(2))

sumSquare(List(1, 2, 3), List(4, 5))

import cats.Id
sumSquare(3 : Id[Int], 4 : Id[Int])

"Dave" : Id[String]
123 : Id[Int]
List(1, 2, 3) : Id[List[Int]]
val a = Monad[Id].pure(3)
val b = Monad[Id].flatMap(a)(_ + 1)
for {
  x <- a
  y <- b
} yield x + y
// res5:

def pure[A](value: A): Id[A] = value
def map[A, B](initial: Id[A])(func: A => B): Id[B] = func(initial)
def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] = func(initial)

import cats.syntax.either._ // for asRight
val a = 3.asRight[String]
// a: Either[String, Int] = Right(3)
val b = 4.asRight[String]
// b: Either[String, Int] = Right(4)
for {
  x <- a
  y <- b
} yield x*x + y*y
// res3: Either[String, Int] = Right(25)
