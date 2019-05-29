package playground

import scala.collection.mutable.ArrayBuffer
import org.openjdk.jmh.annotations._
import scala.collection.mutable.PriorityQueue

case class myint(value: Int, t: String)

@State(Scope.Benchmark)
class Regression {

  var tsArray: ArrayBuffer[myint] = _
  var emptySlots: PriorityQueue[Int] = _

  @Setup(Level.Trial)
  def indexOfSetup: Unit = {
    tsArray = new ArrayBuffer[myint](5000000)
    (0 to 4999998).foreach(i => tsArray += myint(i, "s"))
    tsArray += null
  }

  @Benchmark
  def indexOf: Int = {
    tsArray.indexOf(null)
  }

  @Setup(Level.Iteration)
  def queueSetup: Unit = {
    emptySlots = PriorityQueue.empty[Int](Ordering[Int].reverse)
    (0 to 4999999).foreach(i => emptySlots.enqueue(i))
  }

  @Benchmark
  def queue = {
    val loop = 1 to 10000
    loop foreach { _ => emptySlots.dequeue() }
  }
}
