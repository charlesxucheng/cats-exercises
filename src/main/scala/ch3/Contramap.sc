trait Printable[A] { self => // https://docs.scala-lang.org/tour/self-types.html
  def format(value: A): String
  def contramap[B](func: B => A): Printable[B] = (value: B) => self.format(func(value))
}
def format[A](value: A)(implicit p: Printable[A]): String =
  p.format(value)

implicit val stringPrintable: Printable[String] = (value: String) => s"'$value'"
implicit val booleanPrintable: Printable[Boolean] =
  (value: Boolean) => if (value) "yes" else "no"

format("Hello")

format(true)

final case class Box[A](value: A)

implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap(_.value)

format(Box("hello world"))
format(Box(true))

import cats.Contravariant
import cats.Show
import cats.instances.string._
val showString = Show[String]
val showSymbol = Contravariant[Show].
  contramap(showString)((sym: Symbol) => s"'${sym.name}")
showSymbol.show(Symbol("dave"))

import cats.syntax.contravariant._ // for contramap
showString
  .contramap[Symbol](sym => s"'${sym.name}")
  .show(Symbol("dave"))