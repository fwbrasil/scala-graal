package bench

import com.twitter.finagle.Http
import com.twitter.finagle.Service
import com.twitter.finagle.http
import com.twitter.util.Await
import com.twitter.util.Future
import org.openjdk.jmh.annotations._
import com.twitter.util.Promise
import java.net.ServerSocket

@State(Scope.Benchmark)
class FinagleBench {

  val f = Promise[Int]()

  def resp =
    Future.value(http.Response()).map(identity).map(identity)

  val httpService = new Service[http.Request, http.Response] {
    def apply(req: http.Request): Future[http.Response] =
      resp
  }

  val port = {
    var socket: ServerSocket = null;
    try {
      socket = new ServerSocket(0)
      socket.setReuseAddress(true);
      socket.getLocalPort();
    } finally
      if (socket != null)
        socket.close();
  }

  val server = Http.server.serve(s":$port", httpService)
  //Await.ready(server)

  val client = Http.client.newService(s"localhost:$port")

  @Benchmark
  def request =
    Await.result(client(http.Request("/foo/bar")))
}
