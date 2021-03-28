import ch1.{Cat, Printable}
import ch1.PrintableInstances._
import ch1.PrintableSyntax._

val cat = Cat("Mantou", 1, "ginger tabby")
Printable.print(cat)

cat.print