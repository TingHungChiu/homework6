public class QueueTest {
    private static int totalthread = 0;
    private static int iters = 0;
    private static int n = 1;

    public static void main(String[] args) throws Exception {
        totalthread = Integer.parseInt(args[1]);
        iters = Integer.parseInt(args[2]);
        if (args.length == 4)
            n = Integer.parseInt(args[3]);
        else
            n =5;

        int enqthread = 0;
        int deqthread = 0;
        if (totalthread == 1)
            enqthread = 1;
        else {
            enqthread = totalthread/2;
            deqthread = totalthread - enqthread;
        }

        final TestThread[] threads = new TestThread[totalthread];

        for (int t = 0; t < enqthread; t++) {
            threads[t] = new TestThread(args[0], iters, 0, n);
        }

        for (int t = 0; t < deqthread; t++) {
            threads[enqthread + t] = new TestThread(args[0], iters, 1, n);
        }

        for (int t = 0; t < totalthread; t++) {
            threads[t].start();
        }

        long enqCount = 0;
        long deqCount = 0;
        int nodeCount = 0;
        for (int t = 0; t < totalthread; t++) {
            threads[t].join();
            enqCount += threads[t].getEnqCount();
            deqCount += threads[t].getDeqCount();
        }
        switch(args[0])
        {
            case "JLQueue":
                nodeCount = threads[0].getSizeJLQueue();
                break;
            case "SLQueue":
                nodeCount = threads[0].getSizeSLQueue();
                break;
            case "ULFQueue":
                nodeCount = threads[0].getSizeULFQueue();
                break;
        }
        System.out.println(enqCount + " " + deqCount + " " + nodeCount);
        System.out.println((enqCount+deqCount)/iters);
    }
}
