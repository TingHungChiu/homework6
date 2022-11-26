import java.util.concurrent.atomic.AtomicInteger;

public class BLFQueue<T> {
    private final static int QUEUE_SIZE = 64;
    private final Item<T>[] queue = new Item[QUEUE_SIZE];
    private final AtomicInteger writer = new AtomicInteger ();
    private final AtomicInteger read = new AtomicInteger ();


    BLFQueue () {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            queue[i] = new Item();
        }
    }

    public void enq(int value) {
        int ticket = writer.getAndIncrement ();
        int turn = (ticket / QUEUE_SIZE) * 2;
        int position = (ticket % QUEUE_SIZE);
        Item it = queue[position];
        while(it.lastID != turn);
        it.value = value;
        it.lastID=turn + 1;
    }
    int deq(){
        int ticket = read.getAndIncrement();
        int turn = (ticket / QUEUE_SIZE)*2+1;
        int position = (ticket % QUEUE_SIZE);
        Item it = queue[position];
        while(it.lastID != turn);
        it.lastID = turn + 1;
        int tmp = it.value;
        it.value=0;
        return tmp;
    }
    class Item<T>{
        int value;
        public int lastID;
        public Item(int value){
            this.value = value;
            this.lastID = 0;
        }
        public Item(){
            this.lastID = 0;
        }
    }
}
