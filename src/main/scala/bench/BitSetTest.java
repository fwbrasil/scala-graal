package bench;

import java.util.BitSet;

public class BitSetTest {
  public static void main(String[] args) {

    long bitset = 0;

    bitset |= (1L << 33293731968l);
    bitset |= (1L << 12);

    boolean test = (bitset & (1L << 33293731968l)) != 0;

    BitSet b = new BitSet(1000);
    b.set(10);
    b.set(12);
    b.nextSetBit(10);
  }
}
