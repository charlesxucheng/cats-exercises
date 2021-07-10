import cats.data.OptionT

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
type ListOption[A] = OptionT[List, A]

import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure
val result1: ListOption[Int] = OptionT(List(Option(10), Option(20)))
//val result2: ListOption[Int] = 32.pure[ListOption]
val result2: ListOption[Int] = OptionT(List(Option(32), Option(42)))

val result3 = result1.flatMap { (x: Int) =>
  result2.map { (y: Int) =>
    x + y
  }
}

import cats.data.EitherT
import cats.instances.future._ // for Monad
import scala.concurrent.ExecutionContext.Implicits.global

type Response[A] = EitherT[Future, String, A]

val powerLevels = Map(
  "Jazz" -> 6,
  "Bumblebee" -> 8,
  "Hot Rod" -> 10
)

def getPowerLevel(autobot: String): Response[Int] = {
  powerLevels.get(autobot) match {
    case Some(arg) => EitherT.right(Future(arg))
    case None => EitherT.left(Future(s"$autobot unreachable"))
  }
}

def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
  for {
    lvl1 <- getPowerLevel(ally1)
    lvl2 <- getPowerLevel(ally2)
  } yield {
    (lvl1 + lvl2) > 15
  }

def tacticalReport(ally1: String, ally2: String): String = {
  val stack = canSpecialMove(ally1, ally2).value
  Await.result(stack, 1.second) match {
    case Left(msg) => s"Comm error: $msg"
    case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
    case Right(false) =>
      s"$ally1 and $ally2 need a recharge."
  }
}
tacticalReport("Jazz", "Bumblebee")
// res13: String = "Jazz and Bumblebee need a recharge."
tacticalReport("Bumblebee", "Hot Rod")
// res14: String = "Bumblebee and Hot Rod are ready to roll out!"
tacticalReport("Jazz", "Ironhide")
// res15: String = "Comms error: Ironhide unreachable"