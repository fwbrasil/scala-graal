package playground;
//package bench
//
//import scala.annotation.tailrec
//
//object Main extends App {
//
//  class SparseTable(items: List[Long]) {
//
//    //    val (hash, m) = SparseTable.hashFuntion(items)
//  }
//
//  trait Hash {
//    def apply(x: Long, s: Long): Int
//    def effort: Int
//  }
//
//  //
//  case object ToIntHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      x.intValue()
//    def effort = 0
//  }
//
//  //
//  case object MinusSHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (x - s).intValue()
//    def effort = 1
//  }
//
//  //
//  case class MultiplicativeHash(p: Int) extends Hash {
//    override def apply(x: Long, s: Long) =
//      ((x * p) >> s).intValue()
//    def effort = 3
//  }
//
//  //
//  case object ShiftHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (x >> s).intValue()
//    def effort = 1
//  }
//
//  //
//  case object MinusShiftHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (-x >> s).intValue()
//    def effort = 2
//  }
//
//  //
//  case object MinusXorHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (-x ^ s).intValue()
//    def effort = 2
//  }
//
//  //
//  case object ShiftAndHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (x >> (x & s)).intValue()
//    def effort = 2
//  }
//
//  //
//  case object AndHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      (x & s).intValue()
//    def effort = 1
//  }
//
//  //
//  case object ShiftAndXorHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      ((x >> s) ^ x).intValue()
//    def effort = 2
//  }
//
//  //
//  case object ShiftAndMultHash extends Hash {
//    override def apply(x: Long, s: Long) =
//      ((x >> s) * x).intValue()
//    def effort = 2
//  }
//
//  //
//  case class RotateHash(p: Int) extends Hash {
//    override def apply(x: Long, s: Long) = {
//      java.lang.Long.rotateRight(x, p).intValue()
//    }
//    def effort = 2
//  }
//  
//  //
//  case class RotatePlusXHash(p: Int) extends Hash {
//    override def apply(x: Long, s: Long) = {
//      (java.lang.Long.rotateRight(x, p) + x).intValue()
//    }
//    def effort = 3
//  }
//
//  
//  case class RotateMinusXHash(p: Int) extends Hash {
//    override def apply(x: Long, s: Long) = {
//      (java.lang.Long.rotateRight(x, p) - x).intValue()
//    }
//    def effort = 3
//  }
//
//  case class RotateXorXHash(p: Int) extends Hash {
//    override def apply(x: Long, s: Long) = {
//      (java.lang.Long.rotateRight(x, p) ^ x).intValue()
//    }
//    def effort = 3
//  }
//
//  val mersennePrimes = List[Int](3, 7, 31, 127, 8191, 131071, 524287, 2147483647)
//
//  val hashes = ToIntHash :: MinusSHash :: ShiftHash :: MinusShiftHash :: MinusXorHash :: ShiftAndHash :: AndHash :: ShiftAndXorHash :: ShiftAndMultHash :: mersennePrimes.map(MultiplicativeHash) ::: mersennePrimes.map(RotateHash) ::: mersennePrimes.map(RotatePlusXHash) /*::: mersennePrimes.map(RotateMinusXHash) ::: mersennePrimes.map(RotateXorXHash) */
//
//  def hashFuntions(items: List[Long]) =
//    items.headOption.map { s =>
//      hashes.map { hash =>
//        val r = items.map(_ - s)
//        @tailrec
//        def findM(i: Int): Int = {
//          if (i > r.size * 8)
//            -1
//          else {
//            val d = r.map(hash(_, s) & i)
//            if (d.toSet.size == r.size)
//              i
//            else
//              findM(i + 1)
//          }
//        }
//        val m = findM(1)
//        (hash, s, m)
//      }.filter(_._3 > 0).sortBy(_._1.effort).sortBy(_._3)
////      .map {
////        case (hash, s, m) =>
////          ((v: Long) => hash(v, s) & m, m)
////      }
//    }.getOrElse(Nil)
//
//  class SparseMap private (items: List[Long], m: Int, hash: Long => Int) {
//    val sparse = new Array[Int](m)
//    val dense = items.toArray
//    
//    for(i <- 0 until dense.length) {
//      val v = dense(i)
//      sparse(hash(v)) = i
//    }
//    
//    def indexOf(v: Long): Option[Int] = {
//      val idx = sparse(hash(v))
//      if(dense(idx) == v)
//        Some(idx)
//      else
//        None
//    }
//  }
//
////  object SparseMap {
////    def apply(items: List[Long]) =
////      hashFuntions(items).headOption.map {
////        case (hash, m) =>
////          new SparseMap(items, m, hash)
////      }
////  }
//  
////  val s = SparseMap(List(33286002384L, 33289851280L, 33291888384L)).get
////  
////  println(s.indexOf(33291888184L))
//
//    println(data.flatMap(d => hashFuntions(d)).groupBy(_._1.getClass).mapValues(_.size).mkString("\n"))
//
//  //  data.foreach { d =>
//  //    println(d)
//  //    println("=> " + SparseTable.hashFuntions(d).sortBy(_._1.effort).sortBy(_._3).take(3))
//  //    println()
//  //  }
//
//  //  }
//  //
//  //  val m = findM(2, modHash)
//  //
//  //  val sparse = new Array[Int](m + 1)
//  //  val check = new Array[Long](l.size)
//  //
//  //  l.zipWithIndex.foreach {
//  //    case (v, i) =>
//  //      sparse(hash(v, m).intValue()) = i
//  //      check(i) = v
//  //  }
//  //
//  //  def get(v: Long) = {
//  //    val i = sparse(hash(v, m).intValue())
//  //    if (check(i) == v) i
//  //    else -1
//  //  }
//  //
//  //  println(m)
//  //  println(get(33286880448L))
//
//  //  
//  //  def contains(l: Long) =
//  //    table((l % m).intValue()) == l
//
//  //  println(contains(33286880448L))
//
//  //  @tailrec
//  //  def find(i: Int): Int = {
//  //    val s = l.map(v => universalHash(v, i) % l.size)
//  //    if (s.toSet.size == 3)
//  //      i
//  //    else
//  //      find(i + 1)
//  //  }
//  //  val i = find(0)
//  //  
//  //  println(universalHash(5, i))
//
//  def data =
//    List(
//      List(33286002384L, 33286062112L),
//      List(33286002384L, 33286379960L),
//      List(33286002384L, 33289851280L, 33291888384L),
//      List(33286002384L, 33291145072L),
//      List(33286097944L, 33286446448L, 33287160216L),
//      List(33286110520L, 33286172520L),
//      List(33286110520L, 33286172520L, 33286173624L, 33287060856L, 33290580264L, 33290660544L),
//      List(33286122232L, 33286665720L, 33286960440L, 33286961000L, 33288853544L),
//      List(33286122232L, 33286961000L, 33288853544L),
//      List(33286154752L, 33286541328L),
//      List(33286229560L, 33286446448L),
//      List(33286229560L, 33286544632L),
//      List(33286269152L, 33286271608L),
//      List(33286273472L, 33286319848L, 33286332224L, 33287906712L),
//      List(33286273472L, 33286324464L, 33286446448L, 33287122344L, 33288949408L, 33293857784L, 33293889576L),
//      List(33286332224L, 33287160216L),
//      List(33286334024L, 33286571880L),
//      List(33286356168L, 33286384576L),
//      List(33286357208L, 33286388384L, 33287254648L),
//      List(33286357208L, 33286538344L),
//      List(33286360328L, 33286361416L),
//      List(33286360328L, 33286385096L, 33286538344L),
//      List(33286360328L, 33286386224L, 33286538344L, 33286539856L),
//      List(33286360760L, 33286448248L, 33286461504L, 33288624664L),
//      List(33286360760L, 33286448248L, 33288624664L),
//      List(33286360760L, 33288646360L, 33288736336L, 33288807920L),
//      List(33286446448L, 33287160216L),
//      List(33286448248L, 33286546712L),
//      List(33286532432L, 33289278040L),
//      List(33286539856L, 33287254096L, 33287255720L),
//      List(33286545712L, 33286857992L),
//      List(33286877384L, 33286878424L, 33286882584L),
//      List(33286877384L, 33286882584L),
//      List(33286878424L, 33286882024L, 33286882584L, 33286919760L),
//      List(33286878424L, 33286882584L, 33286919760L),
//      List(33286878424L, 33286919760L),
//      List(33286882024L, 33286883104L, 33286919760L),
//      List(33286882024L, 33286919760L, 33286921952L),
//      List(33286883104L, 33286919760L, 33286920320L),
//      List(33286919760L, 33286920320L),
//      List(33286919760L, 33287290232L, 33287291856L),
//      List(33286919760L, 33287290232L, 33287291856L), List(33286386224L, 33287255200L),
//      List(33286920872L, 33287289672L, 33287290232L, 33287291856L),
//      List(33286920872L, 33287290232L, 33287291856L),
//      List(33287170952L, 33287690072L, 33288655384L, 33288680536L, 33288815928L, 33288999408L),
//      List(33287199136L, 33289975024L),
//      List(33287199136L, 33289979816L),
//      List(33287254096L, 33287255720L),
//      List(33287290232L, 33287291856L),
//      List(33287352072L, 33287352752L),
//      List(33287361560L, 33293136968L, 33293141056L, 33293144080L, 33293488552L),
//      List(33287524152L, 33289155832L, 33289221872L, 33289523240L),
//      List(33287662616L, 33287703592L),
//      List(33287662616L, 33287703592L, 33287718224L, 33287724320L, 33289523240L, 33290843408L),
//      List(33287703592L, 33287918208L),
//      List(33287785864L, 33293442144L),
//      List(33287918208L, 33289375792L),
//      List(33287918208L, 33289395152L),
//      List(33288052152L, 33288240176L),
//      List(33288052152L, 33288240176L, 33288245544L),
//      List(33288240176L, 33293436216L),
//      List(33288344488L, 33288777768L, 33288778792L),
//      List(33288355096L, 33291216376L),
//      List(33288355096L, 33291216376L, 33293701456L, 33293703048L, 33293706600L, 33293851472L, 33293881496L),
//      List(33288355096L, 33291216376L, 33293706600L, 33293851472L, 33293881496L),
//      List(33288434016L, 33288444664L, 33288466592L, 33288470976L, 33288633432L, 33288703424L, 33288765504L, 33288769992L),
//      List(33288451480L, 33289744544L),
//      List(33288550872L, 33288589352L, 33288591240L),
//      List(33288550872L, 33288591240L),
//      List(33288550872L, 33290063960L, 33290748440L, 33293308168L),
//      List(33288578088L, 33288932392L, 33288933416L, 33288934440L),
//      List(33288610824L, 33289359216L, 33290897208L, 33291076568L, 33291077824L, 33291228320L, 33291231272L, 33291243496L),
//      List(33288610824L, 33289359216L, 33290897208L, 33291076568L, 33291231272L, 33291243496L),
//      List(33288807176L, 33288870024L),
//      List(33288807920L, 33288893560L),
//      List(33288855128L, 33288855648L, 33293478792L, 33293480496L),
//      List(33288855648L, 33293478792L, 33293480496L),
//      List(33289003048L, 33289110568L, 33289636904L, 33289825320L),
//      List(33289009856L, 33289961512L, 33289962536L),
//      List(33289190080L, 33289199552L, 33289250472L, 33289256520L, 33289258104L, 33289265256L, 33289313856L, 33290387600L),
//      List(33289346088L, 33289354296L),
//      List(33289349896L, 33289356760L),
//      List(33289419296L, 33290141024L),
//      List(33289489448L, 33290030120L, 33290031144L, 33290032168L),
//      List(33289581864L, 33289589800L, 33290065960L, 33290374184L, 33290377400L, 33290379208L, 33294003240L),
//      List(33289851280L, 33291888384L),
//      List(33289912816L, 33293375168L, 33293451872L, 33293551600L, 33293789528L, 33293799968L, 33293807512L, 33293810960L),
//      List(33289912816L, 33293451872L),
//      List(33289977600L, 33293146240L, 33293797376L, 33293839152L),
//      List(33289991640L, 33293128280L),
//      List(33290027944L, 33290138928L),
//      List(33291096120L, 33293637040L),
//      List(33291109192L, 33291115576L),
//      List(33291109192L, 33291216376L),
//      List(33291324824L, 33291847552L, 33293192232L, 33293253088L, 33293359144L, 33293364264L),
//      List(33291324824L, 33291899784L, 33293192232L, 33293208000L, 33293326856L, 33293359144L, 33293364264L),
//      List(33291324824L, 33293359144L, 33293364264L),
//      List(33291421568L, 33291493416L, 33291515288L, 33291519704L, 33291890128L, 33291909336L, 33293392824L, 33293559168L),
//      List(33291634440L, 33293693800L, 33293728480L, 33293775664L, 33293786824L),
//      List(33291730064L, 33291732344L, 33291734752L),
//      List(33291732344L, 33291734752L),
//      List(33291732344L, 33293610368L, 33293747568L, 33293775664L),
//      List(33291734752L, 33293611304L),
//      List(33293136968L, 33293141056L, 33293144080L),
//      List(33293136968L, 33293141056L, 33293144080L, 33293488552L),
//      List(33293136968L, 33293144080L),
//      List(33293136968L, 33293144080L, 33293488552L),
//      List(33293141056L, 33293488552L),
//      List(33293409296L, 33293911624L),
//      List(33293458152L, 33293469264L),
//      List(33293487816L, 33293847256L, 33293930624L),
//      List(33293616040L, 33293751048L),
//      List(33293627912L, 33293628472L),
//      List(33293632720L, 33293753320L),
//      List(33293632720L, 33293753320L, 33293776672L),
//      List(33293693800L, 33293696000L, 33293777992L, 33293782696L),
//      List(33293696000L, 33293782696L),
//      List(33293701456L, 33293703048L),
//      List(33293705856L, 33293706600L, 33293848544L, 33293851472L),
//      List(33293705856L, 33293848544L),
//      List(33293706600L, 33293851472L),
//      List(33293719160L, 33293720128L),
//      List(33293722112L, 33293855744L),
//      List(33293745176L, 33293820808L),
//      List(33293767640L, 33293820808L),
//      List(33293864648L, 33293874848L, 33293877944L, 33293933480L))
//}