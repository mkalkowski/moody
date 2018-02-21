import groovy.util.logging.Slf4j

    import java.util.concurrent.Exchanger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
class FillAndEmpty {


    private static final int MAX_BUF_SIZE = 3;

    Exchanger<DataBuffer> exchanger = new Exchanger<>();
    DataBuffer initialEmptyBuffer = new DataBuffer(Collections.emptyList());
    DataBuffer initialFullBuffer = new DataBuffer(IntStream.rangeClosed(1, MAX_BUF_SIZE).boxed().collect(Collectors.toList()));

    class FillingLoop implements Runnable {
        void run() {
            int cnt = 0;
            DataBuffer currentBuffer = initialEmptyBuffer;
            try {
                while (currentBuffer != null) {
                    addToBuffer(currentBuffer, cnt++);
                    if (currentBuffer.isFull()) {
                        log.info("Buffer full, exchanging");
                        currentBuffer = exchanger.exchange(currentBuffer);
                        log.info("buffer exchanged ");
                    }
                }
            } catch (InterruptedException ex) {
                log.info("innnnn");
            }
        }

        private void addToBuffer(DataBuffer currentBuffer, int i) {
            currentBuffer.data.add(i);
        }
    }

    class EmptyingLoop implements Runnable {
        void run() {
            DataBuffer currentBuffer = initialFullBuffer;
            try {
                while (currentBuffer != null) {
                    takeFromBuffer(currentBuffer);
                    if (currentBuffer.isEmpty()) {
                        log.info("empti exchanging");
                        currentBuffer = exchanger.exchange(currentBuffer);
                        log.info("empti exchanged");

                    }
                }
            } catch (InterruptedException ex) {
                log.info("interr");
            }
        }

        private void takeFromBuffer(DataBuffer currentBuffer) {
//            log.info("consumed " + currentBuffer.data.poll()); ;
            try {
               currentBuffer.data.poll();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class DataBuffer {
        Queue<Integer> data = new ArrayDeque<>();

        DataBuffer(List<Integer> collect) {
            data.addAll(collect);
        }


        boolean isFull() {
            data.size() == MAX_BUF_SIZE;
        }

        boolean isEmpty() {
            return data.isEmpty();
        }
    }

    void start() {
        new Thread(new FillingLoop(), "filler").start();
        new Thread(new EmptyingLoop(), "emptier").start();
    }

    public static void main(String[] args) {
        new FillAndEmpty().start();
    }
}