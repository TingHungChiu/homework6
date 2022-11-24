import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ULFQueue<T> {
        Node sentinel;
        AtomicReference<Node> head;
        AtomicReference<Node> tail;

        private AtomicInteger size = new AtomicInteger();

        public ULFQueue(){
            sentinel = new Node();
            head = new AtomicReference<>(sentinel);
            tail = new AtomicReference<>(sentinel);
        }
        public void enq(T item){
            if (item == null) throw new NullPointerException();
            Node node = new Node(item);
            while (true) {
                Node last = tail.get();
                Node next = last.next.get();

                if (last == tail.get()) {
                    if (next == null) {
                        if (last.next.compareAndSet(null, node)) {
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
        public T deq() {
            while (true) {
                Node first = head.get();
                Node last = tail.get();
                Node next = first.next.get();
                if (first == head.get()) {
                    if (first == last) {
                        if (next == null) {
                            return null;
                        }
                        tail.compareAndSet(last, next);
                    } else {
                        T value = next.value;
                        if (head.compareAndSet(first, next))
                            size.getAndDecrement();
                            return value;
                    }
                }
            }

        }
        public int size() {return size.get();}
public class Node{
        public T value;
        public AtomicReference<Node> next;
        public Node(T value){
            this.value = value;
            next = new AtomicReference<>();
        }
        public Node(){
            next = new AtomicReference<>();
        }
    }
}
