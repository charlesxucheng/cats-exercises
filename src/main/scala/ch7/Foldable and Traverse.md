Two type classes that capture iteration over collections:
* Foldable abstracts the familiar foldLeft and foldRight operations;
* Traverse is a higher‐level abstraction that uses Applicatives to iterate with less pain than folding.

Foldable provides useful methods such as combineAll and foldMap that make use of Monoid's combine capability.

Foldable defines foldRight using Eval monad. Therefore, it is stack safe.

Finally, we can compose Foldables to support deep traversal of nested sequences.
