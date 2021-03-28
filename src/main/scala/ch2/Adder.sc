def add(items: List[Int]): Int =
  items.foldLeft(0)(_ + _)

//import cats.Monoid
//import cats.syntax.semigroup._ // for |+|
import cats._
import cats.syntax.monoid._

def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid.empty[A])(_ |+| _)

import cats.instances.int._

add(List(1, 2, 3))

import cats.instances.option._

add(List(Some(1), None, Some(2), Some(3)))

//add(List(Some(1), Some(2), Some(3)))

case class Order(totalCost: Double, quantity: Double)

implicit val orderAdd: Monoid[Order] = new Monoid[Order] {
  def combine(o1: Order, o2: Order): Order =
    Order(o1.totalCost + o2.totalCost, o1.quantity + o2.quantity)
  def empty() = Order(0, 0)
}

val orderTotal = add(List(Order(200, 10), Order(100, 5)))
println(orderTotal)

