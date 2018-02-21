package lock.free;

import org.openjdk.jmh.annotations.Benchmark;

public class LockFreeStackBench {

    @Benchmark
    public void testStack() {
        LockFreeStack a = new LockFreeStack();

        int i=0;
        int b=3;
        int x = i+b;
        a.pop();

    }

}
