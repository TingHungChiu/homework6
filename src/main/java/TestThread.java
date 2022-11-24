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
    static ConcurrentLinkedQueue<Integer> JLQueue;
    static SLQueue<Integer> SLQueue;
    static ULFQueue<Integer> ULFQueue;
    static BLFQueue BLFQueue;
    private int n;

    public TestThread(String queue, int iters,  int op, int n) {
        id = ID_GEN++;
        this.queue = queue;
        this.iters = iters;
        this.op = op;
        this.threadLocalRandom = ThreadLocalRandom.current();
        this.n = n;
        this.JLQueue = new ConcurrentLinkedQueue<>();
        this.SLQueue = new SLQueue<>(n);
        this.ULFQueue = new ULFQueue<>();
        this.BLFQueue = new BLFQueue();
    }

    long start = 0;

    public void run() {
        switch(queue)
        {
            case "JLQueue":
                switch (op) {
                    case 0:
                        start = System.currentTimeMillis();
                        while (true)
                        {
                            int random = threadLocalRandom.nextInt(0, Integer.MAX_VALUE);
                            JLQueue.add(random%100);
                            enqcount++;
                            if (System.currentTimeMillis() - start >= iters * 1000)
                                break;
                        }
                        break;
                    case 1:
                        start = System.currentTimeMillis();
                        while(true)
                        {
                            Integer item = JLQueue.poll();
                            if (item != null)
                                deqcount++;
                            if (System.currentTimeMillis() - start >= iters * 1000)
                                break;
                        }
                        break;
                    default:
                        System.out.println("error");
                }
                break;
            case "SLQueue":
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
                break;
            case "ULFQueue":
                switch (op) {
                    case 0:
                        start = System.currentTimeMillis();
                        while (true)
                        {
                            int random = threadLocalRandom.nextInt(0, Integer.MAX_VALUE);
                            ULFQueue.enq(random%100);
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
                                item = ULFQueue.deq();
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
                break;
            case "BLFQueue":
                switch (op) {
                    case 0:
                        start = System.currentTimeMillis();
                        while (true)
                        {
                            int random = threadLocalRandom.nextInt(0, Integer.MAX_VALUE);
                            BLFQueue.enq(random%100);
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
                                item = BLFQueue.deq();
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
                break;
        }
    }

    public int getEnqCount() {
        return enqcount;
    }

    public int getDeqCount() {
        return deqcount;
    }
    public int getSizeJLQueue() {
        return JLQueue.size();
    }
    public int getSizeSLQueue() {
        return SLQueue.size();
    }
    public int getSizeULFQueue() {
        return ULFQueue.size();
    }
}
