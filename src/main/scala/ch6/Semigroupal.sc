import cats.Monoid
import cats.instances.int._ // for Monoid
import cats.instances.invariant._ // for Semigroupal
import cats.instances.list._ // for Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.apply._ // for imapN
final case class Cat(
                      name: String,
                      yearOfBirth: Int,
                      favoriteFoods: List[String]
                    )

val tupleToCat: (String, Int, List[String]) => Cat = Cat.apply

val catToTuple: Cat => (String, Int, List[String]) = cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

implicit val catMonoid: Monoid[Cat] = (
  Monoid[String],
  Monoid[Int],
  Monoid[List[String]]
  ).imapN(tupleToCat)(catToTuple)

import cats.syntax.semigroup._ // for |+|
val garfield = Cat("Garfield", 1978, List("Lasagne"))
val heathcliff = Cat("Heathcliff", 1988, List("Junk Food"))
garfield |+| heathcliff

// Future
import cats.Semigroupal
import cats.instances.future._ // for Semigroupal
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
val futurePair = Semigroupal[Future].
  product(Future("Hello"), Future(123))
Await.result(futurePair, 1.second)

val futureCat = (
  Future("Garfield"),
  Future(1978),
  Future(List("Lasagne"))
  ).mapN(Cat.apply)
Await.result(futureCat, 1.second)

// List - Cartesian product of elements
import cats.Semigroupal
import cats.instances.list._ // for Semigroupal
Semigroupal[List].product(List(1, 2), List(3, 4))

// Either - Fail fast
import cats.instances.either._ // for Semigroupal
type ErrorOr[A] = Either[Vector[String], A]
Semigroupal[ErrorOr].product(
  Left(Vector("Error 1")),
  Left(Vector("Error 2"))
)

import cats.Semigroupal
import cats.instances.either._ // for Semigroupal
type ErrorOr[A] = Either[Vector[String], A]
val error1: ErrorOr[Int] = Left(Vector("Error 1"))
val error2: ErrorOr[Int] = Left(Vector("Error 2"))
Semigroupal[ErrorOr].product(error1, error2)

import cats.syntax.apply._ // for tupled
import cats.instances.vector._ // for Semigroup on Vector
(error1, error2).tupled

import cats.syntax.parallel._ // for parTupled
(error1, error2).parTupled

import cats.instances.list._ // for Semigroup on List
type ErrorOrList[A] = Either[List[String], A]
val errStr1: ErrorOrList[Int] = Left(List("error 1"))
val errStr2: ErrorOrList[Int] = Left(List("error 2", "error 3"))
val errStr3: ErrorOrList[Int] = Right(3)
(errStr1, errStr2, errStr3).parTupled

val success1: ErrorOr[Int] = Right(1)
val success2: ErrorOr[Int] = Right(2)
val addTwo = (x: Int, y: Int) => x + y
(error1, error2).parMapN(addTwo)
// res4: ErrorOr[Int] = Left(Vector("Error 1", "Error 2"))
(success1, success2).parMapN(addTwo)
(error1, success2).parMapN(addTwo)