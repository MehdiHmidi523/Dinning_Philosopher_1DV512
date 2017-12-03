import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
	private final int id;
    private Lock lock;

    public ChopStick(int id) { //acts as a Boolean semaphore.
	    this.id = id;
        lock = new ReentrantLock();
	}
	public int getId() {return id;}
    public Lock getLock() { return lock; }
    public void setLock(Lock lock) { this.lock = lock; }
    public String toString() { return "Chopstick (" + id + ")"; }
}
