# Dealing with Cats imports
Unless we have a good reason to import individual instances, we can just import everything.
```scala
import cats._
import cats.implicits._
```

# Higher Kinds and Type Constructors
Kinds are like types for types. They describe the number of “holes” in a type. We distinguish between regular types that have no holes and “type constructors”
that have holes we can fill to produce types.

For example, List is a type constructor with one hole. We fill that hole by 
specifying a parameter to produce a regular type like List[Int] or List[A].
The trick is not to confuse type constructors with generic types. List is a type
constructor, List[A] is a type.

In Scala, we declare type constructors using underscores (F[_]).

# Partial Unification
The partial unification in the Scala compiler works by fixing type parameters from left to right. 
Partial unification is the default behaviour in Scala 2.13. In earlier versions of Scala we need to add the -Ypartial-unification compiler flag. 

# FP Patterns
There are two functional programming patterns that we should consider when defining a trait:
* we can make it a typeclass, or;
* we can make it an algebraic data type (and hence seal it).