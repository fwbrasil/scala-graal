package bench

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
class ITableBench {

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)  
  def call(a: A) = a.doIt
  
  var a: A = new B

  @Benchmark
  def testIt = {
    a.doIt
  }
}

trait A {
  def doIt: Int
}

class B extends A {
  def doIt = 1
}

class C extends A {
  def doIt = 2
}