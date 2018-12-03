package bench;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

final class Counter {
  private long v = 0;

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public long incrementAndGet() {
    v++;
    return v;
  }
}

interface I {
  public long doIt(Counter c);
}

abstract class AbstractI {
  abstract public long doIt(Counter c);
}

class C2 extends AbstractI implements I {
  public long doIt(Counter c) {
    return c.incrementAndGet() + 1;
  }
}

class C1 extends AbstractI implements I {
  public long doIt(Counter c) {
    return c.incrementAndGet() + 2;
  }
}

class C3 extends AbstractI implements I {
  public long doIt(Counter c) {
    return c.incrementAndGet() + 3;
  }
}

class C4 extends AbstractI implements I {
  public long doIt(Counter c) {
    return c.incrementAndGet() + 4;
  }
}

interface J {
  public String test();
}

class C5 implements I {
  // public String test() {
  // return "a";
  // }

  public long doIt(Counter c) {
    return c.incrementAndGet() + 5;
  }
}

@State(Scope.Benchmark)
public class ITableBench {

  Random r = new Random(0);

  static Counter c = new Counter();

  long j = 0;

  @Setup
  public void warmup() {
    System.out.println("AAA");
    C1 c1 = new C1();
    C2 c2 = new C2();
    C3 c3 = new C3();
    C4 c4 = new C4();
    // C5 c5 = new C5();
    for (int i = 0; i < 10_000; i++) {
      j += c1.doIt(c);
      j += c2.doIt(c);
      j += c3.doIt(c);
      j += c4.doIt(c);
      // j += c5.doIt();
    }
  }

  long start = System.currentTimeMillis();
  int  max   = 4;

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  private long doItOuter(I i) {
    return i.doIt(c);
  }

  @Benchmark
  public long bench() {
    if (j != 0) {
      System.out.println(j);
      j = 0;
    }
    I i;
    switch (r.nextInt(max)) {
      case 0:
        i = new C1();
        break;
      case 1:
        i = new C2();
        break;
      case 3:
        i = new C4();
        break;
      case 4:
        i = new C5();
        break;
      default:
        i = new C3();
    }
    if (System.currentTimeMillis() - start > 30000) {
      if (max == 4) {
        System.out.println("force deopt");
        max = 5;
      }
    }
    return doItOuter(i) + 1;
  }

}
