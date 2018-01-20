package moody.code.sprint;

import java.util.Random;

/**
 * Implements Xoroshiro128PlusRandom. Not synchronized (anywhere).
 *
 * @see "http://xoroshiro.di.unimi.it/"
 */
@SuppressWarnings("serial")
public class Xoroshiro128PlusRandom extends Random {
    private static final double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53);
    private static final float FLOAT_UNIT = 0x1.0p-24f; // 1.0 / (1L << 24);

    private long s0, s1;


    public static void main(String[] args) {

        Random r = new Random(1);



        int trial = 100_000_000;

        int[] T = new int[trial];

        long start = System.currentTimeMillis();
        for (int i=0; i< trial; i++) {
            T[i] = r.nextInt();
        }


        System.out.println("done random in " + (System.currentTimeMillis() - start));

        Random r2 = new Xoroshiro128PlusRandom(1);
        start = System.currentTimeMillis();
        for (int i=0; i< trial; i++) {
            T[i] = r.nextInt();
        }


        System.out.println("done xoroshiro in " + (System.currentTimeMillis() - start));


    }
    public Xoroshiro128PlusRandom(long seed) {
        // Must be here, the only Random constructor. Has side-effects on setSeed, see below.
        super(0);

        s0 = hash(seed);
        s1 = hash(s0);

        if (s0 == 0 && s1 == 0) {
            s0 = hash(0xdeadbeefL);
            s1 = hash(s0);
        }
    }

    public static int hash(int k) {
        k ^= k >>> 16;
        k *= 0x85ebca6b;
        k ^= k >>> 13;
        k *= 0xc2b2ae35;
        k ^= k >>> 16;
        return k;
    }

    /**
     * Hashes an 8-byte sequence (Java long).
     */
    public static long hash(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;

        return k;
    }

    @Override
    public void setSeed(long seed) {
        // Called from super constructor and observing uninitialized state?
        if (s0 == 0 && s1 == 0) {
            return;
        }

        throw new RuntimeException("seed fail");
    }

    @Override
    public boolean nextBoolean() {
        return nextLong() >= 0;
    }

    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len; ) {
            long rnd = nextInt();
            for (int n = Math.min(len - i, 8); n-- > 0; rnd >>>= 8) {
                bytes[i++] = (byte) rnd;
            }
        }
    }

    @Override
    public double nextDouble() {
        return (nextLong() >>> 11) * DOUBLE_UNIT;
    }

    @Override
    public float nextFloat() {
        return (nextInt() >>> 8) * FLOAT_UNIT;
    }

    @Override
    public int nextInt() {
        return (int) nextLong();
    }

    @Override
    public int nextInt(int n) {
        // Leave superclass's implementation.
        return super.nextInt(n);
    }

    @Override
    public double nextGaussian() {
        // Leave superclass's implementation.
        return super.nextGaussian();
    }

    @Override
    public long nextLong() {
        final long s0 = this.s0;
        long s1 = this.s1;
        final long result = s0 + s1;
        s1 ^= s0;
        this.s0 = Long.rotateLeft(s0, 55) ^ s1 ^ s1 << 14;
        this.s1 = Long.rotateLeft(s1, 36);
        return result;
    }

    @Override
    protected int next(int bits) {
        return ((int) nextLong()) >>> (32 - bits);
    }
}
