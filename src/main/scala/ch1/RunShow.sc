import cats.Show
import cats.implicits._

final case class Cat(name: String, age: Int, color: String)

implicit val catShow: Show[Cat] = Show.show[Cat] { cat =>
  val name = cat.name.show
  val age = cat.age.show
  val color = cat.color.show
  s"$name is a $age year-old $color cat."
}

val cat = Cat("Mantou", 1, "ginger tabby")


cat.show