import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class SLQueue<T> {
    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;

    public AtomicInteger eCount = new AtomicInteger();
    public AtomicInteger dCount = new AtomicInteger();
    private int semiNum;

    public SLQueue(int semiNum) {
        Node sentinel = new Node(null);
        this.head = new AtomicReference<Node>(sentinel);
        this.tail = new AtomicReference<Node>(sentinel);
        this.semiNum = semiNum;
    }

    public void enq(T item) {
        if (item == null) throw new NullPointerException();
        Node node = new Node(item);
        while (true) {
            Node last = tail.get();
            Node next = last.next.get();
            if (last == tail.get()) {
                if (next == null) {
                    if (last.next.compareAndSet(next, node)) {
                        eCount.getAndIncrement();
                        tail.compareAndSet(last, node);
                        return;
                    }
                } else {
                    tail.compareAndSet(last, next);
                }
            }
        }
    }
    public T deq() throws Exception {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        threadLocal.set(0);
        while (true) {
            Node first = head.get();
            Node last = tail.get();
            Node next = first.next.get();
            int ranNum = 0;
            if (first == head.get()) {
                if (first == last) {
                    if (next == null) {
                        throw new Exception();
                    }

                    tail.compareAndSet(last, next);
                } else {
                    if (semiNum > 1) {
                        if (threadLocal.get() < semiNum) {

                            ranNum = random.nextInt(semiNum);
                            if (ranNum > 0) {
                                T item = deq(ranNum, next);
                                if (item != null) {
                                    dCount.getAndIncrement();
                                }
                                return item;
                            }


                            if ((next != null) && next.mark.get()) {

                                if (next != last && head.compareAndSet(first, next)) {
                                    first = next;
                                    next = next.next.get();
                                }
                                if (next == null)
                                    return null;

                                if (next.mark.compareAndSet(false, true)) {
                                    dCount.getAndIncrement();
                                    return next.item;
                                }
                            }

                            threadLocal.set(threadLocal.get() + 1);
                        } else {

                            if (first == head.get()) {
                                if (first == last) {
                                    if (next == null) {
                                        throw new Exception();
                                    }
                                    // tail is behind, try to advance
                                    tail.compareAndSet(last, next);
                                } else {
                                    T item = next.item;
                                    if (head.compareAndSet(first, next)) {
                                        dCount.getAndIncrement();
                                    }
                                    return item;
                                }
                            }

                        }
                    }
                    else {
                        if (first == head.get()) {
                            if (first == last) {
                                if (next == null) {
                                    throw new Exception();
                                }

                                tail.compareAndSet(last, next);
                            } else {
                                T item = next.item;
                                if (head.compareAndSet(first, next)) {
                                    dCount.getAndIncrement();
                                }
                                return item;
                            }
                        }
                    }
                }
            }
        }
    }

    public T deq(int val, Node next) throws Exception {

        int temp=0;
        while(temp < val){
            if(next.next.get() == null){
                break;
            }
            next = next.next.get();
            temp++;
        }
        if(next.mark.compareAndSet(false,true)){
            return next.item;
        }
        return null;

    }



    private class Node {

        T item;

        AtomicReference<Node> next;

        AtomicBoolean mark = new AtomicBoolean();
        Node(T item) {      // usual constructor
            this.item = item;
            this.next = new AtomicReference<Node>(null);
            this.mark.set(false);
        }

    }

}



