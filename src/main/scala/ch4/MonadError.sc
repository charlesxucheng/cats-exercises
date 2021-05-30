import cats.MonadError
import cats.implicits.{catsStdInstancesForEither, catsStdInstancesForTry, catsSyntaxApplicativeErrorId, catsSyntaxApplicativeId}

import scala.util.Try

def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable
]): F[Int] = {
  if (age >= 18) age.pure[F]
  else new IllegalArgumentException("Age must be greater or equal to 18").raiseError[F, Int]
}

validateAdult[Try](18)
validateAdult[Try](8)
type ExceptionOr[A] = Either[Throwable, A]
validateAdult[ExceptionOr](-1)