import java.util.concurrent.atomic.AtomicInteger;

public class BLFQueue<T> {
 private final static int QUEUE_SIZE = 64;
 private final T[] queue = new T[QUEUE_SIZE ];

         private final AtomicInteger writer = new
            AtomicInteger ();

         BLFQueue () {
         for (int i = 0; i < QUEUE_SIZE; i++) {
             queue[i] = new T();
             }
         }

         void enqueue(int value) {
         int ticket = writer.getAndIncrement ();
         int turn = (ticket / QUEUE_SIZE) * 2;
         int position = (ticket % QUEUE_SIZE);
         T it = queue[position];
         while(it.lastID != turn);
         it.value = value;
         it.lastID = turn + 1;
    }
}
