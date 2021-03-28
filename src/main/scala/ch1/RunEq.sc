import cats.Eq
import cats.syntax.eq._

final case class Cat(name: String, age: Int, color: String)

import cats.instances.int._ // for Eq
import cats.instances.string._ // for Eq

implicit val catEqual: Eq[Cat] = Eq.instance[Cat] { (cat1, cat2) =>
  (cat1.name === cat2.name) && (cat1.age === cat2.age) && (cat1.color === cat2.color)
}

val cat1 = Cat("Garfield", 38, "orange and black")
val cat2 = Cat("Mantou", 1, "ginger")

cat1 === cat2
cat1 =!= cat2

import cats.instances.option._

val optionCat1 = Option(cat1)
val optionCat2 = Option.empty[Cat]

optionCat1 === optionCat2
optionCat1 =!= optionCat2