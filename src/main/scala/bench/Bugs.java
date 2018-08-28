package bench;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

final class Value {
  public final Integer a, b, c;

  public Value() {
    this.a = 0;
    this.b = 1;
    this.c = 2;
  }

  public void check() {
    if (a == null || b == null || c == null)
      throw new NullPointerException();
  }
}

final class Chain {
  private final AtomicReference<Chain> next = new AtomicReference<>();

  public Chain chain() {
    Chain r = new Chain();
    while (!next.compareAndSet(next.get(), r)) {
    }
    return r;
  }

  public void setValue(Value v) {
    v.check();
    Chain curr = next.get();
    curr.setValue(v);
  }
}

public class Bugs {

  public static void main(String[] args) {
    Bugs bugs = new Bugs();
    while (true) {
      bugs.test();
    }
  }

  ExecutorService ex = Executors.newFixedThreadPool(10);

  private static void a(Chain c) {
    c.chain().chain();
  }

  private static void b(Chain c) {
    c.setValue(new Value());
  }

  private void test() {
    Chain c = new Chain();
    ex.submit(() -> {
      a(c);
    });
    ex.submit(() -> {
      b(c);
    });
  }
}
