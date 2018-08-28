package bench

import com.twitter.finagle.Http
import com.twitter.finagle.Service
import com.twitter.finagle.http
import com.twitter.util.Await
import com.twitter.util.Future
import org.openjdk.jmh.annotations._
import com.twitter.util.Promise

@State(Scope.Benchmark)
class FinagleBench {

  val f = Promise[Int]()

//  List(
//    f.fusedMap(_ + 1),
//    f.fusedFlatMap(i => Future.value(i + 1)),
//    f.fusedTransform(i => Future.value(i.get + 1)),
//    f.fusedRespond(i => {})
//    ).foreach { v =>
//      v.transform(i => Future.value(1))
//      v.respond(i => {})
//    }

  def resp =
    Future.value(http.Response()).map(identity).map(identity)

  val httpService = new Service[http.Request, http.Response] {
    def apply(req: http.Request): Future[http.Response] =
      resp
  }

  val server = Http.server.serve(":8080", httpService)
  //  Await.ready(server)

  val client = Http.client.newService("localhost:8080")

  @Benchmark
  def request =
    Await.result(client(http.Request("/foo/bar")))
}
