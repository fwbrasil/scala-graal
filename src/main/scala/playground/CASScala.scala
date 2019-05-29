//package playground
//
//import org.openjdk.jmh.annotations.Benchmark
//
//class CASScala {
//  
//  object Unsafe {
//    def compareAndSwapInt(a: Any, b: Any, c: Any, d: Any) = ???
//  }
//  
//  val valueOffset = 1
//  
//  class Test(init: Int) {
//    @volatile var value = init
//  }
//  
//  @Benchmark
//  def testUnsafe() = {
//    val t = new Test(0)
//    Unsafe.compareAndSwapInt(t, valueOffset, 0, 1)
//  }
//  
//  @Benchmark
//  def testIfElse() = {
//    val t = new Test(0)
//    t.synchronized {
//      if(t.value != 0)
//        false
//      else {
//        t.value = 1
//        true
//      }
//    }
//  }
//}