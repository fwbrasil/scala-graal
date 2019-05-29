package playground

object A {

  sealed trait IO[T] {
    def map[U](f: T => U): IO[U] = ???
  }
  case class Value[T](v: T) extends IO[T]
  case class Map[T, U](io: IO[T], f: T => U) extends IO[U]
  object IO {
    def value[T](v: T): IO[T] = ???
  }

  IO.value(20).map(_ + 1).map(_ + 1).map(_ + 1)
    .map(_ + 1).map(_ + 1).map(_ + 1).map(_ + 1)
    .map(_ + 1).map(_ + 1).map(_ + 1) match {
      case io: Value[Int]  => io.v
      case io: Map[_, Int] => io.f(run(io.io))
    }

  def plus10(n: Int): IO[Int] =
    IO.value(n).map(_ + 1).map(_ + 1).map(_ + 1)
      .map(_ + 1).map(_ + 1).map(_ + 1).map(_ + 1)
      .map(_ + 1).map(_ + 1).map(_ + 1)

  def run[U](io: IO[U]): U =
    io match {
      case io: Value[U]  => io.v
      case io: Map[_, U] => io.f(run(io.io))
    }

}