# Monoid
Formally, a monoid for a type A is:
* an operation combine with type (A, A) => A
* an element empty of type A

Monoids must formally obey these laws: 

For all values x, y, and z, in A, 
* combine must be associative 
* empty must be an identity element

```scala
def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
  m.combine(x, m.combine(y, z)) ==
  m.combine(m.combine(x, y), z)
}

def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
  (m.combine(x, m.empty) == x) &&
  (m.combine(m.empty, x) == x)
}
```

A semigroup is just the combine part of a monoid, *without the empty part*. While many semigroups are also monoids, there are some data types for which
we cannot define an empty element.

Packages required:
Monoid:
```scala
import cats.Monoid
import cats.instances.string._ // For Monoid String instance
Monoid[String].combine("Hi", "there") // Using Monoid instance
Monoid.apply[String].combine("Hi ", "there") // Equivalent to previous line
Monoid[String].empty
Monoid.apply[String].empty

// Using syntax
import cats.syntax.semigroup._ // for |+|
val stringResult = "Hi " |+| "there"

```

Semigroup
```scala
import cats.Semigroup
import cats.instances.string._ // For Semigroup String instance
Semigroup[String].combine("Hi ", "there")
```
