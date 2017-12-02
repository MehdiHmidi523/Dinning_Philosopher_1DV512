
public class ChopStick {
	private final int id;
	public volatile boolean TAKEN=false;
    private Philosopher holder = null;

	public ChopStick(int id) {

	    this.id = id;
	}

	public int getId() {return id;}

    synchronized public void putDown(){
        Philosopher p = (Philosopher) Thread.currentThread();
        if( p != holder)
            throw new RuntimeException( "Exception: " + p + " attempted to put down a chopstick he wasn't holding." );
        else{   //letting go
            holder = null;
            this.notify();
        }
    }

    synchronized public boolean pickedUp(){
        if(this.holder == null){ // No one is holding the chopstick
            holder = (Philosopher) Thread.currentThread();
            return true;
        }
        else return false;
    }

}
