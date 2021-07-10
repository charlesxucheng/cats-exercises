import cats.syntax.semigroup._ // for |+|

import cats.instances.int._ // for Monoid
import cats.instances.option._ // for Monoid
Option(1) |+| Option(2)

import cats.instances.map._ // for Monoid
val map1 = Map("a" -> 1, "b" -> 2)
val map2 = Map("b" -> 3, "d" -> 4)
map1 |+| map2
// res2: Map[String, Int] = Map("b" -> 5, "d" -> 4, "a" -> 1)

import cats.instances.string._ // for Monoid
import cats.instances.tuple._ // for Monoid
val tuple1 = ("hello", 123)
val tuple2 = ("world", 321)
tuple1 |+| tuple2
// res3: (String, Int) = ("helloworld", 444)