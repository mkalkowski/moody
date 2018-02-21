package moody.code.sprint;

import java.nio.ByteBuffer;

public class Bytes {

    private static final int CAPACITY = 1024*1024*1500;

    public static void main(String[] args) {
        new Bytes().run();
    }

    private void run() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);

        for (int i=0; i<CAPACITY; i++) {
            buffer.put((byte) i);
        }

        measureRead(buffer, "direct");

        buffer = ByteBuffer.allocate(CAPACITY);

        for (int i=0; i<CAPACITY; i++) {
            buffer.put((byte) i);
        }
        measureRead(buffer, "heap");


    }

    private void measureRead(ByteBuffer buffer, String type) {
        System.out.println("start reading");
        long start = System.currentTimeMillis();
        long r = 0;
        for (int i=0; i<CAPACITY; i++) {
            r += buffer.get(i);
        }

        System.out.println(type + " - done reading in " + (System.currentTimeMillis() - start) + "ms - " + r);
    }
}
