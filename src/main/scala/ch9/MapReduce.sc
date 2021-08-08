//Regardless of the batching strategy, mapping and reducing with Monoids is a
//powerful and general framework that isn’t limited to simple tasks like addition
//and string concatenation. Most of the tasks data scientists perform in their
//day‐to‐day analyses can be cast as monoids.

import cats.Monoid
import cats.syntax.semigroup._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future} // for |+|
def foldMap[A, B : Monoid](as: Vector[A])(func: A => B): B =
  as.map(func).foldLeft(Monoid[B].empty)(_ |+| _)

def foldMap2[A, B : Monoid](as: Vector[A])(func: A => B): B =
  as.foldLeft(Monoid[B].empty)(_ |+| func(_))

import cats.instances.int._ // for Monoid
foldMap(Vector(1, 2, 3))(identity)
// res1: Int = 6
import cats.instances.string._ // for Monoid
// Mapping to a String uses the concatenation monoid:
foldMap(Vector(1, 2, 3))(_.toString + "! ")
// res2: String = "1! 2! 3! "

// Mapping over a String to produce a String:
foldMap("Hello world!".toVector)(_.toString.toUpperCase)
// res3: String = "HELLO WORLD!"

def parallelFoldMap[A, B: Monoid]
(values: Vector[A])
(func: A => B): Future[B] = {
  // Calculate the number of items to pass to each CPU:
  val numCores = Runtime.getRuntime.availableProcessors
  val groupSize = (1.0 * values.size / numCores).ceil.toInt
  // Create one group for each CPU:
  val groups: Iterator[Vector[A]] =
    values.grouped(groupSize)
  // Create a future to foldMap each group:
  val futures: Iterator[Future[B]] =
    groups map { group =>
      Future {
        group.foldLeft(Monoid[B].empty)(_ |+| func(_))
      }
    }
  // foldMap over the groups to calculate a final result:
  Future.sequence(futures) map { iterable =>
    iterable.foldLeft(Monoid[B].empty)(_ |+| _)
  }
}
val result: Future[Int] =
  parallelFoldMap((1 to 10000000).toVector)(identity)
Await.result(result, 1.second)
// res14: Int = 1784293664

import cats.Monoid
import cats.instances.int._ // for Monoid
import cats.instances.future._ // for Applicative and Monad
import cats.instances.vector._ // for Foldable and Traverse
import cats.syntax.foldable._ // for combineAll and foldMap
import cats.syntax.traverse._ // for traverse
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


def parallelFoldMap[A, B: Monoid]
(values: Vector[A])
(func: A => B): Future[B] = {
  val numCores = Runtime.getRuntime.availableProcessors
  val groupSize = (1.0 * values.size / numCores).ceil.toInt
  values
    .grouped(groupSize)
    .toVector
    .traverse(group => Future(group.foldMap(func)))
    .map(_.combineAll)
}
val future: Future[Int] =
  parallelFoldMap((1 to 1000).toVector)(_ * 1000)
Await.result(future, 1.second)
