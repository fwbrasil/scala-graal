package bench;

import java.util.Random;
import java.util.function.Function;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class II {

  private static interface IT1 {
  }
  private static interface IT2 {
  }
  private static interface IT3 {
  }
  private static interface IT4 {
  }

  private static abstract class A implements Function<Integer, Integer> {

    public abstract Integer apply(Integer t);

  }

  private static class I0 extends A {
    
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 0;
    }
    
  }

  private static class I1 extends A {
    
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 1;
    }
  }

  private static class I2 extends A {
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 2;
    }
  }

  private static class I3 extends A {
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 4;
    }
  }

  private static class I4 extends A {
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 4;
    }
  }

  private static class I5 extends A {
    @Override
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Integer apply(Integer t) {
      return t + 5;
    }
  }

  Random r  = new Random(1);

  I0     i0 = new I0();
  I1     i1 = new I1();
  I2     i2 = new I2();
  I3     i3 = new I3();
  I4     i4 = new I4();
  I5     i5 = new I5();

  private Function<Integer, Integer> f() {
    switch (r.nextInt(5)) {
      case 0:
        return i0;
      case 1:
        return i1;
      case 2:
        return i2;
      case 3:
        return i3;
      case 4:
        return i4;
      case 5:
        return i5;
    }
    return null;
  }

  @Benchmark
  public int bench() {
    return f().apply(1);
  }

}
