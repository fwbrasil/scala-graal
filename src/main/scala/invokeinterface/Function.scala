//package invokeinterface
//
//import scala.util.Random
//import org.openjdk.jmh.annotations.CompilerControl.Mode
//import org.openjdk.jmh.annotations._
//
//@State(Scope.Thread)
//class Function {
//
//  @Param(Array("1", "2", "4", "10"))
//  var numberOfImpls = 0
//
//  var currentImpl = 0
//
//  @CompilerControl(Mode.DONT_INLINE)
//  def impl: Int => Int = {
//    currentImpl += 1
//    currentImpl match {
//      case 1  => (_: Int) + 1
//      case 2  => (_: Int) + 2
//      case 3  => (_: Int) + 3
//      case 4  => (_: Int) + 4
//      case 5  => (_: Int) + 5
//      case 6  => (_: Int) + 6
//      case 7  => (_: Int) + 7
//      case 8  => (_: Int) + 8
//      case 9  => (_: Int) + 9
//      case 10 => (_: Int) + 10
//      case _ =>
//        currentImpl = 0
//        impl
//    }
//  }
//
//  @Benchmark
//  def bench =
//    impl(1)
//}