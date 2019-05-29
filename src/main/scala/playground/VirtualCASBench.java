package playground;

import java.lang.reflect.Field;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import sun.misc.Unsafe;

@State(Scope.Benchmark)
public class VirtualCASBench {

	@Benchmark
	public boolean testUnsafe() {
		TestClass t = new TestClass(0);
		return UnsafeUtil.unsafe.compareAndSwapInt(t, TestClass.valueOffsett, 0, 1);
	}

	@Benchmark
	public boolean testIfElse() {
		TestClass t = new TestClass(0);
		synchronized (t) {
			if (t.value != 0)
				return false;
			else {
				t.value = 1;
				return true;
			}
		}
	}
}

class TestClass {
	
	public static final long valueOffsett = UnsafeUtil.fieldOffset(TestClass.class, "value");
	
	public volatile int value;

	public TestClass(int value) {
		this.value = value;
	}
}

class UnsafeUtil {
	public static final Unsafe unsafe;

	static {
		Unsafe u = null;
		try {
			Field fld = Unsafe.class.getDeclaredField("theUnsafe");
			fld.setAccessible(true);
			u = (Unsafe) fld.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		unsafe = u;
	}

	public static final long fieldOffset(Class<?> cls, String name) {
		try {
			return unsafe.objectFieldOffset(cls.getDeclaredField(name));
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
