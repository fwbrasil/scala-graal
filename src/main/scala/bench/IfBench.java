package bench;

import java.util.Random;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class IfBench {

	Random r = new Random(0);

	@Benchmark
	public int bench() {
		int i;
		if (r.nextBoolean()) {
			i = a();
		} else {
			i = b();
		}
		return i + 1;
	}

	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	private int a() {
		return r.nextInt();
	}

	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	private int b() {
		return r.nextInt();
	}
}
