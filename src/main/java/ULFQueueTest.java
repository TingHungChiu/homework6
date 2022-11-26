import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ULFQueueTest extends Thread {
    /**
     * @author Mohamed M. Saad
     */
    private static int ID_GEN = 0;
    private int id;
    long elapsed;
    private int iter;
    static ULFQueue<Integer> queue = new ULFQueue<>();
    AtomicInteger enqCount = new AtomicInteger();
    AtomicInteger deqCount = new AtomicInteger();

    public ULFQueueTest(ULFQueue queue, int iter) {
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
                try {
                    queue.deq();
                } catch (Exception e) {}
                deqCount.getAndIncrement();
            }
            long end = System.currentTimeMillis();
            elapsed = end - start;
        }
    }
    public long getElapsedTime() {
        return elapsed;
    }

    public int getEnqueues() {
        return enqCount.get();
    }

    public int getDequeues() {
        return deqCount.get();
    }
}
