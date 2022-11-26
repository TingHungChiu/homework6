import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressWarnings("unchecked")
public class JLQueueTest extends Thread {
    private static int ID_GEN = 0;
    private int id;
    long elapsed;
    private int iter;
    static ConcurrentLinkedQueue<Integer> queue ;
    AtomicInteger enqCount = new AtomicInteger();
    AtomicInteger deqCount = new AtomicInteger();

    public JLQueueTest(ConcurrentLinkedQueue queue, int iter) {
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
                queue.add(data);
                enqCount.getAndIncrement();
            } else {
                queue.poll();
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

