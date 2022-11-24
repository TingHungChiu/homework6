import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.Exception;
public class SLQueue<T> {
    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;
    private static int n;
    private ThreadLocalRandom threadLocalRandom;
    private AtomicInteger size = new AtomicInteger();

    public SLQueue(int n) {
        Node sentinel = new Node(null);
        this.head = new AtomicReference<>(sentinel);
        this.tail = new AtomicReference<>(sentinel);
        this.threadLocalRandom = ThreadLocalRandom.current();
        this.n = n;
    }

    private T travel(int index, Node succ) {
        for (int i = 0; i < index; i++)
        {
            if (succ.next.get() == null)
                break;
            succ = succ.next.get();
        }
        if (succ.marked.compareAndSet(false, true))
            return succ.value;
        else
            return null;
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
                        tail.compareAndSet(last, node);
                        size.getAndIncrement();
                        return;
                    }
                } else {
                    tail.compareAndSet(last, next);
                }
            }
        }
    }

    public T deq() throws Exception {
        int tmp = 0;
        if (tmp!=0)
            System.out.println(tmp);
        while (true) {
            Node first = head.get();
            Node last = tail.get();
            Node succ = first.next.get();
            if (first == head.get()) {
                if (first == last) {
                    if (succ == null)
                        throw new Exception();
                    tail.compareAndSet(last, succ);
                } else {
                    if (n > 1) {
                        if (tmp < n) {
                            int random = ThreadLocalRandom.current().nextInt(0,n);
                            T value = travel(random, succ);
                            if (value != null)
                            {
                                size.getAndDecrement();
                                return value;
                            }
                            if (succ != null && succ.marked.get() == true) {
                                if (succ != last && head.compareAndSet(first, succ)) {
                                    first = succ;
                                    succ = succ.next.get();
                                }
                                if (succ == null) {
                                    return null;
                                }
                                if (succ.marked.compareAndSet(false, true))
                                {
                                    size.getAndDecrement();
                                    return succ.value;
                                }
                            }
                            tmp++;
                        } else {
                            T value = succ.value;
                            if (head.compareAndSet(first, succ)) {
                                size.getAndDecrement();
                                return value;
                            }
                        }
                    } else {
                        T value = succ.value;
                        if (head.compareAndSet(first, succ)) {
                            size.getAndDecrement();
                            return value;
                        }
                    }
                }
            }
        }
    }

    public int size() {
        return size.get();
    }

    protected class Node {
        public T value;
        public AtomicReference<Node> next;
        AtomicBoolean marked = new AtomicBoolean();
        public Node(T value) {
            this.value = value;
            this.next  = new AtomicReference<>(null);
            marked.set(false);
        }
    }
}
