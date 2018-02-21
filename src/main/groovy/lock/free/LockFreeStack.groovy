package lock.free

import java.util.concurrent.atomic.AtomicReference

class LockFreeStack {

    AtomicReference<List<Integer>> stack = new AtomicReference<>(null)

    public void pop() {
        int i = 2**2
        int x = Math.log(i)
    }


}
