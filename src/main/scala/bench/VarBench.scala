package bench

import com.twitter.util.Var
import com.twitter.util.Closable
import com.twitter.finagle.netty4.util.Netty4Timer
import com.twitter.util.Timer
import com.twitter.util.Updatable
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
class VarBench {

  val (u, v) = {
    var updatable: Updatable[Int] = null
    val v =
      Var.async(1) { up =>
        updatable = up
        Closable.nop
      }.map(_ + 1).map(_ + 2)
    v.sample()
    (updatable, v)
  }

  @Benchmark
  def bench() = {
    u.update(10)
    v.sample()
  }
}