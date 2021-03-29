trait Codec[A] { self =>
  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = {
    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))
      override def decode(value: String): B = dec(self.decode(value))
    }
  }
}

implicit val stringCodec: Codec[String] = new Codec[String] {
  override def encode(value: String) = value
  override def decode(value: String) = value
}

val a: Double = 2.0
implicit val doubleCodec: Codec[Double] =
  stringCodec.imap[Double](_.toDouble, _.toString)

doubleCodec.encode(a)
doubleCodec.decode("3.0")

final case class Box[A](value: A)
implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = c.imap(Box(_), _.value)

def encode[A](value: A)(implicit codec: Codec[A]) = codec.encode(value)
def decode[A](value: String)(implicit codec: Codec[A]) = codec.decode(value)

encode(a)
decode[Double]("123.4")

encode(Box(a))
decode[Box[Double]]("123.4")

import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.invariant._ // for imap
import cats.syntax.semigroup._ // for |+|
implicit val symbolMonoid: Monoid[Symbol] =
  Monoid[String].imap(Symbol.apply)(_.name)

Monoid[Symbol].empty

Symbol("a") |+| Symbol("few") |+| Symbol("words")




