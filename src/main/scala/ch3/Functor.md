Informally, a functor is anything with a map method. Formally, a functor is a type F[A] with an operation map with type (A => B) => F[B].

We should think of map not as an iteration pattern, but as a way of sequencing computations on values ignoring some complication dictated by the relevant
data type:
* Option—the value may or may not be present;
* Either—there may be a value or an error;
* List—there may be zero or more values.

Functors guarantee the same semantics whether we sequence many small operations one by one, or combine them into a larger function before mapping. 
To ensure this is the case the following laws must hold:

Identity: calling map with the identity function is the same as doing nothing:
```scala
fa.map(a => a) == fa
```

Composition: mapping with two functions f and g is the same as mapping with f and then mapping with g:
```scala
fa.map(g(f(_))) == fa.map(f).map(g)
```

Similar to Monoids, we obtain instances using the standard Functor.apply method on the companion object.
```scala
import cats.Functor
import cats.instances.list._ // for Functor
import cats.instances.option._ // for Functor
val list1 = List(1, 2, 3)
// list1: List[Int] = List(1, 2, 3)
val list2 = Functor[List].map(list1)(_ * 2)
// list2: List[Int] = List(2, 4, 6)
val option1 = Option(123)
// option1: Option[Int] = Some(123)
val option2 = Functor[Option].map(option1)(_.toString)
// option2: Option[String] = Some("123")
```

We can think of Functor's map method as “appending” a transformation to a chain.
Contravariant functor provides an operation called contramap that represents “prepending” an operation to a chain.

Invariant functors implement a method called imap that generates new type classes via a pair of bidirectional transformations that are informally equivalent to a combination of map and contramap.

```scala
trait Contravariant[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}
trait Invariant[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}
```
Using Invariant Functors one can generate one type constructor F[B] from another F[A].
