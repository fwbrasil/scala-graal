package bench

import scala.collection.generic.CanBuildFrom
import scala.reflect.ClassTag
import scala.util.Random

//object Main2 extends App {
//
//  val r = new Random(0)
//  val list = List.fill(1000)(r.nextFloat().toString)
//
//  val f1: String => String = _.dropWhile(_ != '.')
//  val f2: String => String = _.drop(1)
//  val f3: String => String = _.padTo(7, '0')
//
//    println(list.map(f1).map(f2).map(f3))
//
//    println(list.map(f1.andThen(f2).andThen(f3)))
//
//    println((new ListFusion.Value(list)).map(f1).map(f2).map(f3).fuse)
//}

//class ListFusion[A](l: List[A]) {
//
//  private final val array = l.toArray(implicitly[ClassTag[Any]]).asInstanceOf[Array[A]]
//  
//  def fuse = array.toList
//
//  def map[B, That](f: A => B)(implicit bf: CanBuildFrom[List[A], B, That]): ListFusion[B] = {
//    var i = 0
//    val r = this.as[B]
//    while (i < array.length) {
//      r.array(i) = f(array(i))
//      i += 1
//    }
//    r
//  }
//  private def as[B] = this.asInstanceOf[ListFusion[B]]
//}
//
//object ListFusion {
//  def apply[A](l: List[A]) = new ListFusion(l)
//}

//@Fusion(cls = ListFusion.class)

class F {
  def map_map[T, U, V](l: List[T], f1: T => U, f2: U => V) =
    l.map(f1.andThen(f2))
    
  def map_flatMap[T, U, V](l: List[T], f1: T => U, f2: U => List[V]) =
    l.flatMap(f1.andThen(f2))
}

trait ListFusion[A] extends Fusion[List[A]] {

  def fuse: List[A]

  def map[B, That](f: A => B)(implicit bf: CanBuildFrom[List[A], B, That]): ListFusion[B] =
    new ListFusion.Map(this, f)
}

object ListFusion {
  def apply[A](l: List[A]): ListFusion[A] = new Value(l)

  class Value[A](l: List[A]) extends ListFusion[A] {
    override def fuse = l
  }

  class Map[A, B, That](
    l: ListFusion[A], f: A => B)(implicit bf: CanBuildFrom[List[A], B, That])
      extends ListFusion[B] {

    override def map[C, That](f2: B => C)(implicit bf: CanBuildFrom[List[B], C, That]) =
      new Map(l, f.andThen(f2))

    override def fuse =
      l.fuse.map(f)
  }
}

class ArrayListFusion[A](a: Array[A]) extends Fusion[List[A]] {

  def fuse: List[A] = {
    var r = List.empty[A]
    var i = a.length - 1
    while (i >= 0) {
      r = a(i) :: r 
      i -= 1
    }
    r
  }

  def map[B, That](f: A => B)(implicit bf: CanBuildFrom[List[A], B, That]): ArrayListFusion[B] = {
    val b = a.asInstanceOf[Array[B]]
    var i = 0
    while (i < a.length) {
      b(i) = f(a(i))
      i += 1
    }
    new ArrayListFusion(b)
  }
}

object ArrayListFusion {

  private[this] final val ct = implicitly[ClassTag[Any]]

  def apply[A](l: List[A]): ArrayListFusion[A] =
    new ArrayListFusion(l.toArray(ct).asInstanceOf[Array[A]])
}
