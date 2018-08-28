package bench;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class TestOffset {

  public static final Unsafe u;
  public static final long   vOffset;

  static {
    Unsafe unsafe = null;
    long offset = 0L;
    try {
      Field fld = Unsafe.class.getDeclaredField("theUnsafe");
      fld.setAccessible(true);
      unsafe = (Unsafe) fld.get(null);
      offset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("v"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    u = unsafe;
    vOffset = offset;
  }
}
