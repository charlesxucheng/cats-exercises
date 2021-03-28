package ch1

trait Printable[A] {
  def format(a: A): String
}

object PrintableInstances {
  implicit val intPrintable: Printable[Int] = (a: Int) => a.toString
  implicit val stringPrintable: Printable[String] = (a: String) => a
  implicit val catPrintable: Printable[Cat] = (a: Cat) => {
    val name = Printable.format(a.name)
    val age = Printable.format(a.age)
    val color = Printable.format(a.color)
    s"$name is a $age year-old $color cat."
  }
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit a: Printable[A]): String = a.format(value)
    def print(implicit a: Printable[A]): Unit = println(format(a))
  }
}

object  Printable {
  def format[A](a: A)(implicit p: Printable[A]): String = p.format(a)
  def print[A](a: A)(implicit p: Printable[A]): Unit = println(format(a))
}

final case class Cat(name: String, age: Int, color: String)

