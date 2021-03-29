
import cats.Functor
import cats.syntax.functor._

import scala.concurrent.{ExecutionContext, Future}

final case class Box[A](value: A)
val box = Box[Int](123)

implicit val boxFunctor: Functor[Box] = new Functor[Box] {
  override def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
}

box.map(value => value + 1)

implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
  new Functor[Future] {
    def map[A, B](value: Future[A])(func: A => B): Future[B] = value.map(func)
  }


import cats.Functor
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map
val func1: Int => Double = (x: Int) => x.toDouble
val func2: Double => String = (y: Double) => (y * 2).toString
val func3 = func1.map(func2)

type F[A] = Int => A
val functor = Functor.apply[F]

// Type Constructor is declared as F[_]
//def myMethod[F[_]]
