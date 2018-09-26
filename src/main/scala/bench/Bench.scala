

trait Test {
  def a = println(1)
}

class TestB extends Test {
  def b = println(1)
}

//package bench
//
//import org.openjdk.jmh.annotations._
//import org.openjdk.jmh.infra.Blackhole
//// import com.twitter.util.Promise
//import scala.annotation.tailrec
//// import com.twitter.util.Future
//// import com.twitter.util.Await
//import java.util.concurrent.atomic.AtomicLong
//import java.util.Arrays
//import scala.reflect.ClassTag
//import scala.util.Random
//import java.lang.invoke.MethodHandle
//
//case class Call(m: String, params: Any*)
//
//object LFusion {
//  def apply(obj: Any, calls: Call*) = {
//    var i = 0
//    while(i < calls.length) {
//      val call = calls(i)
//      call.m match {
//        case "map" => 
//      }
//      i += 1
//    }
//  }
//}
//
//@State(Scope.Benchmark)
//class CASBench {
//
//  var b = 0
//
//  @Benchmark
//  def baseline = {
//    if (b == 0)
//      b = 1
//  }
//
//  @Benchmark
//  def nonEscaping = {
//    val a = new AtomicLong(b)
//    a.compareAndSet(0, 1)
//  }
//
//  val a = new AtomicLong(b)
//
//  @Benchmark
//  def escaping = {
//    a.compareAndSet(0, 1)
//  }
//
//  @Benchmark
//  def returning = {
//    val a = new AtomicLong(b)
//    a.compareAndSet(0, 1)
//    a
//  }
//}
//
//@State(Scope.Benchmark)
//class ListBench {
//
//  val r = new Random(0)
//  val list = List.fill(1000)(r.nextFloat().toString)
//
//  val f1: String => String = _.dropWhile(_ != '.')
//  val f2: String => String = _.drop(1)
//  val f3: String => String = _.padTo(7, '0')
//
//  @Benchmark
//  def notFused =
//    list.map(f1).map(f2).map(f3)
//
//  @Benchmark
//  def fused =
//    list.map(f1.andThen(f2).andThen(f3))
//
//  @Benchmark
//  def usingFusionClass =
//    ListFusion(list).map(f1).map(f2).map(f3).fuse
//
////  @Benchmark
////  def usingArrayFusionClass =
////    ArrayListFusion(list).map(f1).map(f2).map(f3).fuse
//
//}
//
////  @Benchmark
////  def foldLeft =
////    list.foldLeft(1) {
////      (a, b) => a + b.size
////    }
////
////  @Benchmark
////  def iteratorFoldLeft: Int = {
////    var it = list.iterator
////    var r = 0
////    while (it.hasNext) {
////      r += it.next().size
////    }
////    r
////  }
////
////  def foldLeftImpl[B](z: B)(@deprecatedName('f) op: (B, String) => B): B = {
////    var acc = z
////    var these = list
////    while (!these.isEmpty) {
////      acc = op(acc, these.head)
////      these = these.tail
////    }
////    acc
////  }
////
////  @Benchmark
////  def function2FoldLeft =
////    foldLeftImpl(0)((a, b) => a + b.size)
////
////  @Benchmark
////  def inlinedFoldLeft = {
////    var acc = 0
////    var these = list
////    while (!these.isEmpty) {
////      acc += these.head.size
////      these = these.tail
////    }
////    acc
////  }
////
////  @Benchmark
////  def iterator(bh: Blackhole) = {
////    val it = list.iterator
////    while (it.hasNext) {
////      bh.consume(it.next())
////    }
////  }
////
////  @Benchmark
////  def foreach(bh: Blackhole) =
////    for (i <- list)
////      bh.consume(i)
////
////  @Benchmark
////  def map =
////    list.map(_ + 1)
////
////  @Benchmark
////  def append =
////    list :+ "a"
////
////  @Benchmark
////  def prepend =
////    "a" +: list
////}
//
//class StringOp {
//
//  @Benchmark
//  def appendInline = {
//    val sb = new StringBuffer
//    sb.append("a")
//    sb.append("b")
//    sb.append("c")
//    sb.append("d")
//    sb.append("e")
//    sb.append("f")
//    sb.append("g")
//    sb.append("h")
//    sb.append("i")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.toString
//  }
//
//  @Benchmark
//  def appendCont = {
//    val sb = new StringBuffer
//    sb.append("a")
//    cont(sb)
//  }
//
//  def cont(sb: StringBuffer) = {
//    sb.append("b")
//    sb.append("c")
//    sb.append("d")
//    sb.append("e")
//    sb.append("f")
//    sb.append("g")
//    sb.append("h")
//    sb.append("i")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.append("j")
//    sb.append("k")
//    sb.append("l")
//    sb.toString
//  }
//
//}
//// @State(Scope.Benchmark)
//// class Fusing {
//
////   class List(a: Array[Int]) {
////     def map(f: Int => Int) = {
////       val n = Arrays.copyOf(a, a.length)
////       var i = 0
////       while (i < n.length) {
////         n(i) = f(a(i))
////         i += 2
////       }
////       new List(n)
////     }
////   }
//
////   object List {
////     def apply(values: Int*) =
////       new List(values.toArray)
////   }
//
////   val l = List(1, 2, 3)
//
////   @CompilerControl(CompilerControl.Mode.DONT_INLINE)
////   def _fused(l: List) =
////     l.map(_ + 1 + 1)
//
////   @CompilerControl(CompilerControl.Mode.DONT_INLINE)
////   def pattern(l: List, f1: Int => Int, f2: Int => Int) =
////     l.map(f1).map(f2)
//
////   def map(f: Int => Int): Future[Int] = ???
//
////   @Benchmark
////   def fused = {
////     _fused(l)
////   }
//
////   @Benchmark
////   def patternB = {
////     pattern(l, _ + 1, _ + 2)
////   }
//
////   def inc(a: Array[Int]) = {
////     var i = 0
////     while (i < a.length) {
////       a(i) = a(i) + 1
////       i += 1
////     }
////     a
////   }
//
////   @Benchmark
////   def array = {
////     val a = new Array[Int](3)
////     a(0) = 1
////     a(1) = 2
////     a(2) = 3
////     inc(a)
////   }
//// }
//
//// @State(Scope.Benchmark)
//// class FutureBench {
//
////   @Param(Array("10", "1000"))
////   var ops = 0
//
////   @tailrec
////   final def loop(f: Future[String], i: Int): Future[String] =
////     if (i == 0)
////       f
////     else
////       loop(f.map(_ + "a"), i - 1)
//
////   @Benchmark
////   def mapConst =
////     Await.result(loop(Future.value("a"), ops))
//
////   @Benchmark
////   def mapPromise = {
////     val p = Promise[String]
////     val f = loop(p, ops)
////     p.setValue("a")
////     Await.result(f)
////   }
//
////   val futs = List.fill(ops)(Future.value("a"))
//
////   @Benchmark
////   def collect =
////     Future.collect(futs)
//
////   @Benchmark
////   def poolConst =
////     Future.value("a").poll.isDefined
//
////   val p = Promise[Int]
//
////   @Benchmark
////   def maps =
////     p.map(_ + 1).map(_ + 1).map(_ + 1)
//
////   @Benchmark
////   def notFused =
////     Promise[Int].map(_ + 1).map(_ + 1)
//
////   def transformation(f: Future[Int]): Future[String] =
////     f.map(_ + 1).map(_.toString)
//
////   @Benchmark
////   def fused =
////     Promise[Int].map(_ + 2)
//// }
//
//@State(Scope.Benchmark)
//class LangBench {
//
//  @Benchmark
//  def varargs =
//    List("a", "a", "a", "a", "a", "a")
//
//  case class Test(a: Int, b: String)
//  val test = Test(1, "a")
//
//  @Benchmark
//  def patmatch = {
//    test match {
//      case Test(1, b)   => 1
//      case Test(1, "a") => 2
//      case Test(i, "a") => 3
//      case _            => 4
//    }
//  }
//
//  @Benchmark
//  def evidence = {
//    def test[T](v: T)(implicit ev: T => Int) = ev(v)
//    test(1)
//  }
//
//  @Benchmark
//  def nestedMethod = {
//    def test(i: Int) = i + 1
//    test(1)
//  }
//
//  @Benchmark
//  def closure = {
//    def m(f: Test => Boolean) = f(test)
//    m(_ == test)
//  }
//
//  @Benchmark
//  def tuple = (1, 2)
//}