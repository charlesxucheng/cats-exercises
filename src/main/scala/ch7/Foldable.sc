import cats.Foldable
import cats.instances.list._ // for Foldable

val ints = List(1, 2, 3)
Foldable[List].foldLeft(ints, 0)(_ + _)

import cats.instances.option._ // for Foldable

val maybeInt = Option(123)
Foldable[Option].foldLeft(maybeInt, 10)(_ * _)

import cats.Eval
def bigData = (1 to 1000000).to(LazyList)
bigData.foldRight(0L)(_ + _)

import cats.instances.lazyList._ // for Foldable
val eval: Eval[Long] =
  Foldable[LazyList].
    foldRight(bigData, Eval.now(0L)) { (num, eval) =>
      eval.map(_ + num)
    }
eval.value

import cats.instances.string._ // for Monoid
Foldable[List].foldMap(List(1, 2, 3))(_.toString)

import cats.instances.int._ // for Monoid
Foldable[List].combineAll(List(1, 2, 3))

import cats.instances.vector._ // for Monoid
val ints = List(Vector(1, 2, 3), Vector(4, 5, 6))
(Foldable[List] compose Foldable[Vector]).combineAll(ints)
