package bench

import scala.annotation.tailrec

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(156); 
  def universalHash(x: Long, index: Long): Int = {
    (x >>> (index & 1) * 32).intValue()
  };System.out.println("""universalHash: (x: Long, index: Long)Int""");$skip(80); 

  val l = List(33286877224L, 33286879888L, 33286880448L).map(_ - 33286877224L)

  @tailrec;System.out.println("""l  : List[Long] = """ + $show(l ));$skip(159); 
  def find(i: Int): Int = {
    val s = l.map(v => universalHash(v, i) % l.size)
    if (s.toSet.size == 3)
      i
    else
      find(i + 1)
  };System.out.println("""find: (i: Int)Int""");$skip(18); 
  val i = find(0);System.out.println("""i  : Int = """ + $show(i ));$skip(32); 

  println(universalHash(5, i))}
}
