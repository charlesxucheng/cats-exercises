
import cats.Functor
import cats.syntax.functor._

final case class Box[A](value: A)
val box = Box[Int](123)

implicit val boxFunctor: Functor[Box] = new Functor[Box] {
  override def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
}

box.map(value => value + 1)