package lock.free

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

import java.util.concurrent.atomic.AtomicReference

@Slf4j
class LockFreeStack {

    AtomicReference<Node> head = new AtomicReference<>(null)

    def pop() {
        int tryCounter
        Node result
        while ({
            def oldHead = head.get()
            if (oldHead == null) return null
            result = oldHead
            def newHead = oldHead.next
            !head.compareAndSet(oldHead, newHead)
        }()) continue
        log.info "Finished popping after ${tryCounter} tries"

        result.value
    }

    def push(Integer newVal) {
        int tryCounter
        while ({
            ++tryCounter
            def old = head.get()
            def newNode = new Node(newVal, old)
            !head.compareAndSet(old, newNode)
        }()) continue
        log.info "Finished pushing ${newVal} after ${tryCounter} tries"
    }

    @Canonical
    class Node {
        Integer value
        Node next
    }


    static void main(String[] args) {
        def stack = new LockFreeStack()

        stack.push(3)
        stack.push(2)
        stack.push(1)

        assert stack.pop() == 1
        assert stack.pop() == 2
        assert stack.pop() == 3

    }
}
