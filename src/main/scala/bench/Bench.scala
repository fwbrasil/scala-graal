package bench

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import com.twitter.util.Promise
import scala.annotation.tailrec
import com.twitter.util.Future
import com.twitter.util.Await

@State(Scope.Benchmark)
class ListBench {

  @Param(Array("10", "1000"))
  var size = 0

  val list = List.fill(size)("a")

  @Benchmark
  def foreach(bh: Blackhole) =
    for (i <- list)
      bh.consume(i)

  @Benchmark
  def map =
    list.map(_ + 1)

  @Benchmark
  def append =
    list :+ "a"

  @Benchmark
  def prepend =
    "a" +: list
}

@State(Scope.Benchmark)
class FutureBench {

  @Param(Array("10", "1000"))
  var ops = 0

  @tailrec
  final def loop(f: Future[String], i: Int): Future[String] =
    if (i == 0)
      f
    else
      loop(f.map(_ + "a"), i - 1)

  @Benchmark
  def mapConst =
    Await.result(loop(Future.value("a"), ops))

  @Benchmark
  def mapPromise = {
    val p = Promise[String]
    val f = loop(p, ops)
    p.setValue("a")
    Await.result(f)
  }

  val futs = List.fill(ops)(Future.value("a"))

  @Benchmark
  def collect =
    Future.collect(futs)

  @Benchmark
  def poolConst =
    Future.value("a").poll.isDefined
}

@State(Scope.Benchmark)
class LangBench {

  @Benchmark
  def varargs =
    List("a", "a", "a", "a", "a", "a")

  case class Test(a: Int, b: String)
  val test = Test(1, "a")

  @Benchmark
  def patmatch = {
    test match {
      case Test(1, b)   => 1
      case Test(1, "a") => 2
      case Test(i, "a") => 3
      case _            => 4
    }
  }

  @Benchmark
  def evidence = {
    def test[T](v: T)(implicit ev: T => Int) = ev(v)
    test(1)
  }

  @Benchmark
  def nestedMethod = {
    def test(i: Int) = i + 1
    test(1)
  }

  @Benchmark
  def closure = {
    def m(f: Test => Boolean) = f(test)
    m(_.a == 0)
  }

  @Benchmark
  def tuple = (1, 2)
}