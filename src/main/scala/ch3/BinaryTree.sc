import cats.Functor
import cats.Functor.ops.toAllFunctorOps

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A])
  extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)
  def leaf[A](value: A): Tree[A] =
    Leaf(value)
}

implicit val btreeFunctor: Functor[Tree] = new Functor[Tree] {
  override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
    case Leaf(a) => Leaf(f(a))
    case Branch(a, b) => Branch(map(a)(f), map(b)(f))
  }
}

Tree.leaf(100).map(_ * 2)

Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)