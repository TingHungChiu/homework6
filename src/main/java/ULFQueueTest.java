import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressWarnings("unchecked")
public class ULFQueueTest extends Thread {
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
            int data = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
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
