//package playground
//
//import org.openjdk.jmh.annotations.Benchmark
//import java.util.concurrent.atomic.AtomicLong
//import org.openjdk.jmh.annotations._
//import org.openjdk.jmh.infra.Blackhole
//import scala.annotation.tailrec
//import java.util.concurrent.atomic.AtomicLong
//import java.util.Arrays
//import scala.reflect.ClassTag
//import java.lang.invoke.MethodHandle
//import sun.misc.Unsafe
//import java.util.Random
//import java.util.concurrent.atomic.AtomicBoolean
//
//@State(Scope.Benchmark)
//class CASBench {
//
//  //  @volatile var b = Random.nextInt
//
//  var g = System.currentTimeMillis()
//
//  @Benchmark
//  def iff = {
//    var r = false
//    if (g < System.currentTimeMillis()) {
//      g = System.currentTimeMillis()
//      r ||= true
//    } else {
//      r &&= false
//    }
//    r
//  }
//
//  //  var i = 0
//  //  
//  ////  @Benchmark
//  ////  def lock = {
//  ////    val a = new Object
//  ////    a.synchronized(i += 1)
//  ////    a
//  ////  }
//  ////    
//  ////
//  ////  @Benchmark
//  ////  def nonEscaping = {
//  ////    val a = new AtomicLong(b)
//  ////    a.compareAndSet(0, 1)
//  ////  }
//  ////
//  ////  val a = new AtomicLong(b)
//  ////
//  ////  @Benchmark
//  ////  def escaping = {
//  ////    a.compareAndSet(0, 1)
//  ////  }
//  ////
//
//  case class Test(var v: Long) {
//    def compareAndSet(e: Long, n: Long) =
//      if (v == e) {
//        v = n
//        true
//      } else
//        false
//  }
//
//  @Benchmark
//  def baseline = {
//    val t = new Test(0)
//    t.compareAndSet(0, 1)
//  }
//
//  @Benchmark
//  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
//  def returning = {
//    val a = new AtomicLong(0)
//    a.compareAndSet(0, 1)
//    a
//  }
//
//  @Benchmark
//  def unsafe = {
//    val a = new UnsafeTest(0)
//    a.compareAndSet(0, 1)
//  }
//  
//  @Benchmark
//  def unsafeScala = {
//    val a = new UnsafeTest(0)
//    a.compareAndSet2(0, 1)
//  }
//  
//  @Benchmark
//  def random = {
//    val r = new Random(112);
//    r.nextFloat();
//  }
//  
//  
//  @Benchmark=
//  def flip = {
//    val b = Test(1)
//    R.r = -R.r
//    b.compareAndSet(R.r, R.r);
//  }
//}
//
//object R {
//  var r = 1
//  
//}
//
//case class UnsafeTest(var v: Long) {
//  def compareAndSet(e: Long, n: Long) = {
//    TestOffset.u.compareAndSwapLong(this, TestOffset.vOffset, e, n)
//  }
//  
//  def compareAndSet2(e: Long, n: Long) =
//    UnsafeTest.u.compareAndSwapLong(this, UnsafeTest.aaaa, e, n)
//}
//
//object UnsafeTest {
//  val u = TestOffset.u
//  lazy val aaaa = TestOffset.vOffset
//}