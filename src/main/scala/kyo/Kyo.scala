package kyo

import scala.reflect.runtime.universe._
import scala.annotation.tailrec
import scala.reflect.ClassTag
import org.openjdk.jmh.annotations._
import scala.collection.mutable.Queue
import scala.collection.mutable.Stack

@State(Scope.Benchmark)
class Kyo {//extends App {

  // compileers + partial evaluation
  // tower of compileers
  // Ls -> TLs -> Kyo -> Syo
  // bootstrap?
  // non-jvm

  // A VM should eat itself
  // THIS ^^^^^^^^^^^^^^^^^

  sealed trait Ast

  sealed trait Function extends Ast
  final case class DefFunction(params: Seq[Ident], body: Block) extends Function
  final case class App(func: Function, values: Seq[Function]) extends Function
  final case class Val(name: Ident, function: Function) extends Ast
  final case class Ident(name: String) extends Function
  final case class Block(vals: Seq[Val], Function: Function) extends Function

  final case class IntFunction(v: Int) extends Function
  final object IntFunction {
    final def unapply(f: Function): Option[Int] = {
      f match {
        case f: IntFunction => Some(f.v)
        case Ident(name) =>
          try Some(name.toInt)
          catch {
            case e: NumberFormatException => None
          }
        case _ => None
      }
    }
  }

  final case class StringFunction(v: String) extends Function

  //    object Parse {
  //
  //      import fastparse.all._
  //
  //      val lowercase = P(CharIn("a-z"))
  //      val uppercase = P(CharIn("A-Z"))
  //      val letter = P(lowercase | uppercase)
  //      val digit = P(CharIn("0-9"))
  //
  //      val ident: P[Ident] = P((letter | "_") ~ (letter | digit | "_").rep).!.map(Ident)
  //      val valp: P[Val] = P("val" ~ ident ~ "=" ~ func).map(Val.tupled)
  //      val app: P[App] = P(func ~ func).map(t => App(t._1, t._2 :: Nil))
  //      val block = P(valp.rep ~ func).map(Block.tupled)
  //      val bblock: P[Block] = P(("{" ~ block ~ "}") | block)
  //      val dfunc: P[Function] = P("(" ~ ident.!.rep(sep = ",") ~ ") =>" ~ block).map(DefFunction.tupled)
  //
  //      val func: P[Function] = P(ident | dfunc | app)
  //
  //      def apply(source: String): Function = {
  //        func.parse(source) match {
  //          case p: Parsed.Success[_] => p.value
  //          case p: Parsed.Failure =>
  //            println(p.extra.traced.fullStack)
  //            fail(p.toString())
  //        }
  //      }
  //    }
  //
  //    println(Parse("""(i) => 1 + 2"""))

  val `a + b` = App(App(Ident("a"), StringFunction("+") :: Nil), Ident("b") :: Nil)
  val `(a, b) => a + b` = DefFunction(Ident("a") :: Ident("b") :: Nil, Block(Nil, `a + b`))
  val `sum` = Val(Ident("sum"), `(a, b) => a + b`)
  val `sum(1, 2)` = App(Ident("sum"), Ident("1") :: Ident("2") :: Nil)
  val `sum(1, 2) + 1` = App(App(`sum(1, 2)`, StringFunction("+") :: Nil), Ident("1") :: Nil)
  val `main` = DefFunction(Nil, Block(`sum` :: Nil, `sum(1, 2) + 1`))

  final def fail[T](msg: String): T = throw new IllegalStateException(msg)

  final case class BetaReduction(cache: Map[Function, Function]) {

    final def apply(func: Function): (Function, BetaReduction) = {
      val rfunc = apply(func, Map.empty)
      (rfunc, BetaReduction(cache + (func -> rfunc)))
    }

    private final def apply(func: Function, replace: Map[Ident, Function]): Function =
      cache.get(func).getOrElse {
        func match {
          case i: Ident if (replace.contains(i)) => replace(i)
          case DefFunction(Nil, body)            => apply(body, replace)
          case app @ App(func, values) =>
            App(apply(func, replace), values.map(apply(_, replace))) match {
              case App(DefFunction(params, body), values) =>
                val r = apply(body, replace ++ params.zip(values).map(t => t._1 -> t._2))
                r
              case `app` => app
              case app   => apply(app, replace)
            }
          case Block(vals, func) =>
            apply(func, replace ++ vals.map(v => v.name -> v.function).toMap)
          case _ => func
        }
      }
  }

  trait CBuilder {
    def f0(impl: () => CFunction, func: Function): CFunction0
    def f1(impl: (CFunction) => CFunction, func: Function): CFunction1
    def f2(impl: (CFunction, CFunction) => CFunction, func: Function): CFunction2
  }

  object UCBuilder extends CBuilder {
    def f0(impl: () => CFunction, func: Function) = UCFunction0(impl, func)
    def f1(impl: (CFunction) => CFunction, func: Function) = UCFunction1(impl, func)
    def f2(impl: (CFunction, CFunction) => CFunction, func: Function) = UCFunction2(impl, func)
  }

  object OCBuilder extends CBuilder {
    def f0(impl: () => CFunction, func: Function) = OCFunction0(impl, func)
    def f1(impl: (CFunction) => CFunction, func: Function) = OCFunction1(impl, func)
    def f2(impl: (CFunction, CFunction) => CFunction, func: Function) = OCFunction2(impl, func)
  }

  final def compile(function: Function, builder: CBuilder, scope: Map[Ident, CFunction] = Map.empty): CFunction = {

    def loop(function: Function, scope: Map[Ident, CFunction]) =
      compile(function, builder, scope)

    function match {
      case DefFunction(params, body) =>
        params match {
          case Nil =>
            lazy val c = shallowEval[CFunction0](loop(body, scope))
            builder.f0(() => c, function)
          case p1 :: Nil =>
            builder.f1(c1 => {
              shallowEval[CFunction0](loop(body, scope + (p1 -> c1)))
            }, function)
          case p1 :: p2 :: Nil =>
            builder.f2((c1, c2) => {
              shallowEval[CFunction0](loop(body, scope + (p1 -> c1) + (p2 -> c2)))
            }, function)
        }
      case App(func, values) =>
        val cvalues = values.map(loop(_, scope))
        val cfunc = loop(func, scope)
        val app = App(cfunc.func, cvalues.map(_.func))
        cvalues match {
          case Nil => cfunc
          case p1 :: Nil =>
            lazy val c = shallowEval[CFunction1](cfunc).apply(p1)
            builder.f0(() => c, app)
          case p1 :: p2 :: Nil =>
            lazy val c = shallowEval[CFunction2](cfunc).apply(p1, p2)
            builder.f0(() => c, app)
        }
      case IntFunction(v)    => CIntFunction(v)
      case StringFunction(v) => CStringFunction(v)
      case i: Ident =>
        scope.getOrElse(i, fail(s"Can't find identifier $i"))
      case Block(vals, function) =>
        val s =
          vals.foldLeft(scope) {
            case (scope, Val(name, body)) =>
              val cbody = loop(body, scope)
              scope + (name -> cbody)
          }
        loop(function, s)
    }
  }

  sealed trait CFunction {
    def func: Function
  }

  sealed trait CFunction0 extends CFunction {
    def apply(): CFunction
  }

  sealed trait CFunction1 extends CFunction {
    def apply(v1: CFunction): CFunction
  }

  sealed trait CFunction2 extends CFunction {
    def apply(v1: CFunction, v2: CFunction): CFunction
  }

  val stack = Stack.empty[CFunction]

  final case class UCFunction0(var f: Function0[CFunction], var func: Function) extends CFunction0 {
    stack.push(this)
    final def apply(): CFunction = f.apply()
  }
  final case class UCFunction1(var f: Function1[CFunction, CFunction], var func: Function) extends CFunction1 {
    stack.push(this)
    final def apply(v1: CFunction): CFunction = f.apply(v1)
  }
  final case class UCFunction2(var f: Function2[CFunction, CFunction, CFunction], var func: Function) extends CFunction2 {
    stack.push(this)
    final def apply(v1: CFunction, v2: CFunction): CFunction = f.apply(v1, v2)
  }

  final case class OCFunction0(f: Function0[CFunction], func: Function) extends CFunction0 {
    final def apply(): CFunction = f.apply()
  }
  final case class OCFunction1(f: Function1[CFunction, CFunction], func: Function) extends CFunction1 {
    final def apply(v1: CFunction): CFunction = f.apply(v1)
  }
  final case class OCFunction2(f: Function2[CFunction, CFunction, CFunction], func: Function) extends CFunction2 {
    final def apply(v1: CFunction, v2: CFunction): CFunction = f.apply(v1, v2)
  }

  @tailrec final def jit(b: BetaReduction): Unit = {
//    Thread.sleep(1000)
    if (stack.nonEmpty) {
      val f = stack.pop()
      println(s"compiling " + f)
      f match {
        case f: UCFunction0 =>
          val (rf, nb) = b(f.func)
          val o = deepEval[CFunction0](compile(rf, OCBuilder))
          f.f = o.apply _
          f.func = o.func
          println(s"compiled " + f)
          jit(nb)
        case f: UCFunction1 =>
          val (rf, nb) = b(f.func)
          val o = deepEval[CFunction1](compile(rf, OCBuilder))
          f.f = o.apply _
          f.func = o.func
          println(s"compiled " + f)
          jit(nb)
        case f: UCFunction2 =>
          val (rf, nb) = b(f.func)
          val o = deepEval[CFunction2](compile(rf, OCBuilder))
          f.f = o.apply _
          f.func = o.func
          println(s"compiled " + f)
          jit(nb)
        case _ =>
          jit(b)
      }
    } else
      println("jit done")
  }

  @tailrec final def shallowEval[T <: CFunction: ClassTag](f: CFunction): T = {
    f match {
      case f: T          => f
      case f: CFunction0 => shallowEval(f.apply())
      case f             => fail(s"Expected a ${implicitly[ClassTag[T]]} but got $f")
    }
  }

  final def deepEval[T <: CFunction: ClassTag](f: CFunction): T = {

    import scala.reflect.classTag
    f match {
      case f: CFunction0 =>

        @tailrec def loop(prev: CFunction0): (CFunction0, CFunction) =
          prev match {
            case prev: OCFunction0 => (prev, prev.apply())
            case prev =>
              prev.apply() match {
                case `prev`        => (prev, prev)
                case f: CFunction0 => loop(f)
                case f             => (prev, f)
              }
          }

        val (f0, fn) = loop(f)
        (f0, fn) match {
          case (_, fn: T) => fn
          case (f0: T, _) => f0
          case _          => fail(s"Expected a ${implicitly[ClassTag[T]]} but got $f")
        }
      case f: T => f
      case f =>
        fail(s"Expected a ${implicitly[ClassTag[T]]} but got $f")
    }
  }

  final case class CStringFunction(v: String) extends CFunction {
    val func = StringFunction(v)
  }

  final case class CIntFunction(v: Int) extends CFunction0 with CFunction1 {
    val func = IntFunction(v)

    def apply() = this

    def apply(v1: CFunction) =
      shallowEval[CStringFunction](v1).v match {
        case "+" => OCFunction1(v2 => {
          val i2 = shallowEval[CIntFunction](v2).v
          CIntFunction(v + i2)
        }, App(Ident(s"$v"), v1.func :: Nil))
        case s =>
          fail(s"Method not found Int.$s")
      }
  }

  val f = compile(`main`, UCBuilder)

  val t = new Thread() {
    override def run = {
      jit(BetaReduction(Map.empty))
      println("jit done")
    }
  }

    t.start()

  val n = () => {
    val sum = (a: Int, b: Int) => a + b
    sum(1, 2) + 1
  }

  @Benchmark
  def bi() = deepEval[CIntFunction](f).v

//  println(bi())
//
//  jit(BetaReduction(Map.empty))
//
//  println(bi())

  @Benchmark
  def bn() = n()
}