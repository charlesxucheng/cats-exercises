Monadic comprehension only allows us to run them in sequence.
map and flatMap make the assumption that each computation is dependent on the previous one. 

We need weaker constructs if we don't need strict sequencing of computations. That is, computations are **independent** of each other.

* Semigroupal encompasses the notion of composing pairs of contexts.
* Parallel converts types with a Monad instance to a related type with a Semigroupal instance.
* Applicative extends Semigroupal and Functor. It provides a way of applying functions to parameters within a context.

Semigroupal provides product and tuple2 through tuple22 methods to join contexts, and map2 through map22 for applying a function to the joined contexts.

Law for Semigroupal: the product method must be associative.
```scala
product(a, product(b, c)) == product(product(a, b), c)
```

Cat's provides an apply syntax that provides:
* a tupled method in place of tuple*n*; 
* a mapN method in place of map*n*. mapN can be used to supply values to construct a class;
* contramapN and imapN methods that accept Contravariant and Invariant functors

Applying product on monads (Lists, Futures, etc) will still have sequential computations because the implementation of product make use of flatMap. 

Parallel provides methods such as parTupled and parMapN for error handling that is not fail-fast.

The Hierarchy of Sequencing Type Classes:
```
Semigroupal(product) -----\               /----- Applicative(pure)-----\
                           Apply(ap) -----                              -----Monad                                                                  
Functor(map) -------------/               \----- FlatMap(flatMap) -----/
```

Apply defines product in terms of ap and map; Monad defines product, ap, and map, in terms of pure and flatMap.

Traverse provides a general tool to iterative over something and "shift" the context to the whole of the result.
```scala
def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
  list.foldLeft(List.empty[B].pure[F]) { (accum, item) => (accum, func(item)).mapN(_ :+ _)
}
def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] = {
  listTraverse(list)(identity)
}

// More generalized
trait Traverse[F[_]] {
  def traverse[G[_]: Applicative, A, B]
  (inputs: F[A])(func: A => G[B]): G[F[B]]
  def sequence[G[_]: Applicative, B]
  (inputs: F[G[B]]): G[F[B]] =
    traverse(inputs)(identity)
}
```
Using Traverse we can turn an F[G[A]] into a G[F[A]] for any F with an instance of Traverse and any G with an instance of Applicative.

Cats provides instances of Traverse for List, Vector, Stream, Option, Either, and a variety of other types.
