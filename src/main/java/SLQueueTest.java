import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SLQueueTest extends Thread {
    private static int ID_GEN = 0;
    private int id;
    long elapsed;
    private int iter;
    private static int n;
    static SLQueue<Integer> queue = new SLQueue<>(n);
    AtomicInteger enqCount = new AtomicInteger();
    AtomicInteger deqCount = new AtomicInteger();

    public SLQueueTest(SLQueue queue, int iter) {
        id = ID_GEN++;
        this.queue = queue;
        this.iter = iter;
        this.n = n;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            int data = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE)%100;
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

    public int getEnq() {
        return enqCount.get();
    }

    public int getDeq() {
        return deqCount.get();
    }
}
