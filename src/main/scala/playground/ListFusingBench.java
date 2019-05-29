package playground;

import java.util.function.Function;

import org.openjdk.jmh.annotations.Benchmark;

public class ListFusingBench {

  @Benchmark
  public List<Integer> bench() {
    return (new ListFusing<Integer>()).stage().map(v -> v + 1).map(v -> v + 2).apply(new List(1, 2, 3));
  }

  static class List<T> {
    T[] values;

    public List(T... values) {
      this.values = values;
    }

    public int size() {
      return values.length;
    }

    public <U> List<U> map(Function<T, U> f) {
      U[] n = (U[]) new Object[values.length];
      for (int i = 0; i < values.length; i++)
        n[i] = f.apply(values[i]);
      return new List(n);
    }
  }

  static interface Fusing<T, S extends Fusing.Stage<T, T>> {

    static interface Stage<T, U> {
      U apply(T v);
    }

    public S stage();
  }

  static class ListFusing<T> implements Fusing<List<T>, ListStage<T, T>> {
    @Override
    public ListStage<T, T> stage() {
      return new ListStage.Id();
    }
  }

  static abstract class ListStage<T, U> implements Fusing.Stage<List<T>, List<U>> {

    public <V> ListStage<T, V> map(Function<U, V> f) {
      return new Map(this, f);
    }

    static class Id<T> extends ListStage<T, T> {
      @Override
      public List<T> apply(List<T> v) {
        return v;
      }
    }

    private static class Map<T, U, V> extends ListStage<T, V> {
      private final ListStage<T, U> chain;
      private final Function<U, V> func;

      public Map(ListStage<T, U> chain, Function<U, V> func) {
        super();
        this.chain = chain;
        this.func = func;
      }

      @Override
      public List<V> apply(List<T> v) {
        return chain.apply(v).map(func);
      }

      @Override
      public <X> ListStage<T, X> map(Function<V, X> f) {
        return new Map(chain, func.andThen(f));
      }
    }
  }
}
