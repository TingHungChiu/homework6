import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestThread extends Thread  {
    private static int ID_GEN = 0;
    private int id;
    private String queue;
    private int  op;
    private long iters;
    private int enqcount = 0;
    private int deqcount = 0;
    private ThreadLocalRandom threadLocalRandom;
    static ConcurrentLinkedQueue<Integer> LQueue;
    static SLQueue<Integer> SLQueue;
    private int n;

    public TestThread(String queue, int iters,  int op, int n) {
        id = ID_GEN++;
        this.queue = queue;
        this.iters = iters;
        this.op = op;
        this.threadLocalRandom = ThreadLocalRandom.current();
        this.n = n;
        this.LQueue = new ConcurrentLinkedQueue<>();
        this.SLQueue = new SLQueue<>(n);
    }

    long start = 0;

    public void run() {
        if (queue.equals("LQueue")) {
            switch (op) {
                case 0:
                    start = System.currentTimeMillis();
                    while (true)
                    {
                        int random = threadLocalRandom.nextInt(0, Integer.MAX_VALUE);
                        LQueue.add(random%100);
                        enqcount++;
                        if (System.currentTimeMillis() - start >= iters * 1000)
                            break;
                    }
                    break;
                case 1:
                    start = System.currentTimeMillis();
                    while(true)
                    {
                        Integer item = LQueue.poll();
                        if (item != null)
                            deqcount++;

                        if (System.currentTimeMillis() - start >= iters * 1000)
                            break;
                    }
                    break;
                default:
                    System.out.println("error");
            }
        }
        else if (queue.equals("SLQueue")) {
            switch (op) {
                case 0:
                    start = System.currentTimeMillis();
                    while (true)
                    {
                        int random = threadLocalRandom.nextInt(0, Integer.MAX_VALUE);
                        SLQueue.enq(random%100);
                        enqcount++;
                        if (System.currentTimeMillis() - start >= iters * 1000)
                            break;
                    }
                    break;
                case 1:
                    start = System.currentTimeMillis();
                    while(true)
                    {
                        Integer item = null;
                        try {
                            item = SLQueue.deq();
                        } catch (Exception e)
                        {}
                        if (item != null)
                            deqcount++;
                        if (System.currentTimeMillis() - start >= iters * 1000)
                            break;
                    }
                    break;
                default:
                    System.out.println("error");
            }
        }
    }

    public int getEnqCount() {
        return enqcount;
    }

    public int getDeqCount() {
        return deqcount;
    }
    public int getSizeLQueue() {
        return LQueue.size();
    }
    public int getSizeSLQueue() {
        return SLQueue.size();
    }
}
