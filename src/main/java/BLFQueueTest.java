import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class BLFQueueTest extends Thread {
    private static int ID_GEN = 0;
    private int id;
    long elapsed;
    private int iter;
    static BLFQueue<Integer> queue = new BLFQueue<>();
    AtomicInteger enqCount = new AtomicInteger();
    AtomicInteger deqCount = new AtomicInteger();

    public BLFQueueTest(BLFQueue queue, int iter) {
        id = ID_GEN++;
        this.queue = queue;
        this.iter = iter;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            int data = ThreadLocalRandom.current().nextInt(0, 100);
            if (i % 2 == 0) {
                queue.enq(data);
                enqCount.getAndIncrement();
            } else {
                queue.deq();
                deqCount.getAndIncrement();
            }
            long end = System.currentTimeMillis();
            elapsed = end - start;
        }
    }
    public long getElapsedTime() {
        return elapsed;
    }

    public int getEnq() {
        return enqCount.get();
    }

    public int getDeq() {
        return deqCount.get();
    }
}

