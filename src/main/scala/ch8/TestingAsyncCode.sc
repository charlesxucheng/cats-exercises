import cats.Id
import cats.Traverse.ops.toAllTraverseOps
import cats.implicits.catsStdInstancesForList
import cats.Applicative
import cats.syntax.functor._ // for map

//type Id[A] = A

import scala.concurrent.Future
trait UptimeClient[F[_]] {
  def getUptime(hostname: String): F[Int]
}
trait RealUptimeClient extends UptimeClient[Future] {
  def getUptime(hostname: String): Future[Int]
}
trait TestUptimeClient extends UptimeClient[Id] {
  def getUptime(hostname: String): Int
}

class UptimeService[F[_]](client: UptimeClient[F])(implicit a: Applicative[F]) {
  def getTotalUptime(hostnames: List[String]): F[Int] =
  hostnames.traverse(client.getUptime).map(_.sum)
}

class UptimeService[F[_]: Applicative]
(client: UptimeClient[F]) {
  def getTotalUptime(hostnames: List[String]): F[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
}

//def testTotalUptime() = {
//  val hosts = Map("host1" -> 10, "host2" -> 6)
//  val client = new TestUptimeClient(hosts)
//  val service = new UptimeService(client)
//  val actual = service.getTotalUptime(hosts.keys.toList)
//  val expected = hosts.values.sum
//  assert(actual == expected)
//}
//testTotalUptime()