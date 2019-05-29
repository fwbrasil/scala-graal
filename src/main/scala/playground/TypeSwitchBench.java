package playground;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class TypeSwitchBench {

  static interface I {
    public int apply(int i);
  }

  static class C1 implements I {
    @Override
    public int apply(int i) {
      return i + 1;
    }
  }

  static class C2 implements I {
    @Override
    public int apply(int i) {
      return i + 2;
    }
  }

  static class C3 implements I {
    @Override
    public int apply(int i) {
      return i + 3;
    }
  }

  static class C4 implements I {
    @Override
    public int apply(int i) {
      return i + 4;
    }
  }

  static class C5 implements I {
    @Override
    public int apply(int i) {
      return i + 5;
    }
  }

  static class C6 implements I {
    @Override
    public int apply(int i) {
      return i + 6;
    }
  }

  static class C7 implements I {
    @Override
    public int apply(int i) {
      return i + 7;
    }
  }

  static class C8 implements I {
    @Override
    public int apply(int i) {
      return i + 8;
    }
  }

  static class C9 implements I {
    @Override
    public int apply(int i) {
      return i + 9;
    }
  }

  static class C10 implements I {
    @Override
    public int apply(int i) {
      return i + 10;
    }
  }

  static class C11 implements I {
    @Override
    public int apply(int i) {
      return i + 11;
    }
  }

  Random r = new Random(0);

  @Param({ "11" })
  public int n;

  @Benchmark
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public void bench() {
    I i;
    switch (r.nextInt(n)) {
      case 0:
        i = new C6();
        break;
      case 1:
        i = new C6();
        break;
      case 2:
        i = new C6();
        break;
      case 3:
        i = new C4();
        break;
      case 4:
        i = new C2();
        break;
      case 5:
        i = new C2();
        break;
      case 6:
        i = new C3();
        break;
      case 7:
        i = new C3();
        break;
      case 8:
        i = new C1();
        break;
      case 9:
        i = new C10();
        break;
      default:
        i = new C11();
        break;
    }
    i.apply(r.nextInt());
  }

}
