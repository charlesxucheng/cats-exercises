Informally, a monad is anything with a constructor and a flatMap method. Monads have for comprehensions.

A monad is a mechanism for sequencing computations.

A monad’s flatMap method allows us to specify what happens next, taking into account an intermediate complication. 
The flatMap method of Option takes intermediate Options into account. The flatMap method of List handles intermediate Lists. And so on.
In each case, the function passed to flatMap specifies the application‐specific part of the computation, and flatMap itself takes care of the complication allowing us to flatMap again.

Monadic behaviour is formally captured in two operations:
* pure, of type A => F[A];
* flatMap¹, of type (F[A], A => F[B]) => F[B].

pure and flatMap must obey a set of laws that allow us to sequence operations freely without unintended glitches and side‐effects:
* Left identity: calling pure and transforming the result with func is the same as calling func:
```scala
pure(a).flatMap(func) == func(a)
```
* Right identity: passing pure to flatMap is the same as doing nothing:
```scala
m.flatMap(pure) == m
```
* Associativity: flatMapping over two functions f and g is the same as flatMapping over f and then flatMapping over g:
```scala
m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
```

Every monad is a functor because we can always use flatMap and pure to define map in the same way.

Cats.Monad extends two other type classes: FlatMap, which provides the flatMap method, and Applicative, which provides pure. Applicative also extends Functor.

Id Monad allows us to call our monadic method using plain values.

Either is another Monad. Since Scala 2.12, Either takes the right side as the success case and supports map and flatMap directly.

Cats provides an additional type class called MonadError that abstracts over Either‐like data types that are used for error handling.
Cats provides instances of MonadError for numerous data types including Either, Future, and Try.

Cats provides Eval monad to abstract over different models of evaluation. 
Eval is also stack-safe (uses heap instead of stack), which means we can use it in very deep recursions.
Eval has three subtypes: 
* Now: call‐by‐value which is eager and memoized;
* Always: call‐by‐name which is lazy and not memoized; and
* Later: call‐by‐need which is lazy and memoized.

We can use Write monad to record messages, errors, or additional data about a computation, and extract the log alongside the final result.

Reader monads provide a tool for doing dependency injection. We write steps of our program as instances of Reader, chain them together with map and flatMap, and build a function that accepts the dependency as input.
By representing the steps of our program as Readers we can test them as easily as pure functions, plus we gain access to the map and flatMap combinators.

cats.data.State allows us to pass additional state around as part of a computation.
We define State instances representing atomic state operations and thread them together using map and flatMap. 
In this way we can model mutable state in a purely functional way, without using actual mutation.

We can define a Monad for a custom type by providing implementations of three methods: flatMap, pure, and tailRecM.
The tailRecM method is an optimisation used in Cats to limit the amount of stack space consumed by nested calls to flatMap.