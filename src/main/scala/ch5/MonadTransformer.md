Monad Transformers allows composition of a monad with others.
Cats provides transformers for many monads, each named with a T suffix:
EitherT composes Either with other monads, OptionT composes Option, and so on.

Here’s an example that uses OptionT to compose List and Option. We can use OptionT[List, A], aliased to ListOption[A] for convenience, to transform a List[Option[A]] into a single monad:
```scala
import cats.data.OptionT
type ListOption[A] = OptionT[List, A]
```
Note how we build ListOption **from the inside out**: we pass List, the type of the outer monad, as a parameter to OptionT, the transformer for the inner monad.

We can create instances of ListOption using the OptionT constructor or pure:
```scala
import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure
val result1: ListOption[Int] = OptionT(List(Option(10)))
// result1: ListOption[Int] = OptionT(List(Some(10)))
val result2: ListOption[Int] = 32.pure[ListOption]
// result2: ListOption[Int] = OptionT(List(Some(32)))
```

The map and flatMap methods combine the corresponding methods of List
and Option into single operations:
```scala
result1.flatMap { (x: Int) =>
  result2.map { (y: Int) =>
    x + y
  }
}
// res1: OptionT[List, Int] = OptionT(List(Some(42)))
```
This is the basis of all monad transformers. The combined map and flatMap
methods allow us to use both component monads without having to recursively
unpack and repack values at each stage in the computation.

Many monads and all transformers have at least two type parameters, so we often have to define type aliases for intermediate stages.

We can create transformed monad stacks using the relevant monad transformer’s apply method or the usual pure syntax.
We can unpack a monad transformer stack using value method. Each call to value unpacks a single monad transformer.

One approach involves creating a single “super stack” and sticking to it throughout our code base. 
This works if the code is simple and largely uniform in nature. 
For example, in a web application, we could decide that all request handlers are asynchronous and all can fail with the same set of HTTP error codes. 
We could design a custom ADT representing the errors and use a fusion Future and Either everywhere in our code.

Another design pattern that makes more sense in these contexts uses monad transformers as local “glue code”. 
We expose untransformed stacks at module boundaries, transform them to operate on them locally, and untransform them before passing them on.