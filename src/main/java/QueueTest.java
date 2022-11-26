import java.util.concurrent.ConcurrentLinkedQueue;


public class QueueTest {

    private static final String JLQUEUE = "JLQueue";
    private static final String ULFQUEUE = "ULFQueue";
    private static final String BLFQUEUE = "BLFQueue";
    private static final String SLQUEUE = "SLQueue";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "JLQueue" : args[0];
        int threadCount = (args.length <= 1 ? 8 : Integer.parseInt(args[1]));
        int totalIters = (args.length <= 2 ? 64000 : Integer.parseInt(args[2]));
        int n = (args.length <= 3 ? 8 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;

        run(mode, threadCount, iters, n);

    }

    private static void run(String mode, int threadCount, int iters, int n) throws Exception {
        for (int i = 0; i < 1; i++) {
            switch (mode.trim()) {
                case JLQUEUE:
                    runJLQueueTest(threadCount, iters);
                    break;
                case ULFQUEUE:
                    runULFQueueTest(threadCount, iters);
                    break;
                case BLFQUEUE:
                    runBLFQueueTest(threadCount, iters);
                    break;
                case SLQUEUE:
                    runSLQueueTest(threadCount, iters);
                    break;
            }
        }
    }

    private static void runJLQueueTest(int threadCount, int iters) throws Exception {
        final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        final JLQueueTest[] threads = new JLQueueTest[threadCount];

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new JLQueueTest(queue, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        int enq_index = 0;
        int deq_index = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
            enq_index += threads[t].getEnq();
            deq_index += threads[t].getDeq();
        }
        System.out.println(enq_index +" " + deq_index + " " + (enq_index - deq_index) + " ");
        System.out.println("Throughput is " + (iters*threadCount) / (totalTime*0.001)+ "ops");
        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runULFQueueTest(int threadCount, int iters) throws Exception {
        final ULFQueueTest[] threads = new ULFQueueTest[threadCount];
        final ULFQueue<Integer> queue = new ULFQueue<>();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new ULFQueueTest(queue, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        int enq_index = 0;
        int deq_index = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
            enq_index += threads[t].getEnqueues();
            deq_index += threads[t].getDequeues();
        }
        System.out.println(enq_index +" " + deq_index + " " + (enq_index - deq_index) + " ");
        System.out.println("Throughput is " + (iters*threadCount) / (totalTime*0.001)+ "ops");
        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runBLFQueueTest(int threadCount, int iters) throws Exception {
        final BLFQueueTest[] threads = new BLFQueueTest[threadCount];
        final BLFQueue<Integer> queue = new BLFQueue<>();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new BLFQueueTest(queue, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        int enq_index = 0;
        int deq_index = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
            enq_index += threads[t].getEnq();
            deq_index += threads[t].getDeq();
        }
        System.out.println(enq_index +" " + deq_index + " " + (enq_index - deq_index) + " ");
        System.out.println("Throughput is " + (iters*threadCount) / (totalTime*0.001)+ "ops");
        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runSLQueueTest(int threadCount, int iters) throws Exception {
        final SLQueueTest[] threads = new SLQueueTest[threadCount];
        int n = 0;
        final SLQueue<Integer> queue = new SLQueue<>(n);

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new SLQueueTest(queue, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        int enq_index = 0;
        int deq_index = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
            enq_index += threads[t].getEnq();
            deq_index += threads[t].getDeq();
        }
        System.out.println(enq_index +" " + deq_index + " " + (enq_index - deq_index) + " ");
        System.out.println("Throughput is " + (iters*threadCount) / (totalTime*0.001)+ "ops");
        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
}


