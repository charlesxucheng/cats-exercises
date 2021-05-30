import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future} // for tell

type Logged[A] = Writer[Vector[String], A]

val writer1 = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b", "c").tell
  b <- 32.writer(Vector("x", "y", "z"))
} yield a + b

writer1.run

val writer2 = writer1.mapWritten(_.map(_.toUpperCase))

writer2.run

val writer3 = writer1.bimap(
  log => log.map(_.toUpperCase),
  res => res * 100
)

writer3.run

val writer4 = writer1.mapBoth { (log, res) =>
  val log2 = log.map(_ + "!")
  val res2 = res * 1000
  (log2, res2)
}

writer4.run

val writer5 = writer1.reset
writer5.run

val writer6 = writer1.swap
writer6.run

def slowly[A](body: => A) =
  try body finally Thread.sleep(100)

def factorial(n: Int): Logged[Int] = {
  for {
    ans <- if(n == 0) 1.pure[Logged] else slowly(factorial(n - 1).map(_*n))
    _ <- Vector(s"fact $n $ans").tell
  } yield ans
}

val (log, res) = factorial(5).run

Await.result(Future.sequence(Vector(
  Future(factorial(5)),
  Future(factorial(5))
)).map(_.map(_.written)), 5.seconds)