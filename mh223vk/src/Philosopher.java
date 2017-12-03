import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class Philosopher implements Runnable {
	
	private int id;
    public static  final File logFile = new File("Log.txt");
    public static FileWriter log;
    public static long startTime;
	private boolean debug;
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	private Random randomGenerator;
    private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;
	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed,boolean DEBUG) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution
		 */
		randomGenerator = new Random(id+seed);
		this.debug=DEBUG;

	}

	public int getId() {
		return id;
	}
	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}
	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}
	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}
	public double getTotalThinkingTime() {
		return thinkingTime;
	}
	public double getTotalEatingTime() {
		return eatingTime;
	}
	public double getTotalHungryTime() {
		return hungryTime;
	}
	public double getAverageThinkingTime(){
		if (thinkingTime == 0) return 0;
		return (getTotalThinkingTime()/numberOfThinkingTurns);
	}
	public double getAverageEatingTime(){
		if (eatingTime == 0) return 0;
		return (getTotalEatingTime()/numberOfEatingTurns);
	}
	public double getAverageHungryTime(){
		if (hungryTime == 0) return 0;
		return (getTotalHungryTime()/numberOfHungryTurns);
	}

	//Since the chopsticks are shared between the philosophers,
	// access to the chopsticks must be protected, but if care isn't taken, concurrency problems arise.
	// Most notably, **starvation** via **deadlock** can occur:
	// if each philosopher picks up a chopstick as soon as it is available and waits indefinitely for the other,
	// eventually they will all end up holding only one chopstick with no chance of acquiring another.
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			think(randomGenerator.nextInt(1000));

			long hungryStartTime = System.nanoTime();
			long hungryEndTime;
			Lock rightLock = rightChopStick.getLock();
			Lock leftLock = leftChopStick.getLock();

			//the last philosopher who picks up his left Chopstick should pick up his right.
			rightLock.lock();
			writeToLog("TAKING THE RIGHT CHOPSTICK",0,debug);
			try {
                leftLock.lock();			// Try taking left chopstick if fails then relinquish right one.
                writeToLog("TAKING THE LEFT CHOPSTICK",0,debug);

                hungryEndTime = System.nanoTime();
                hungryTime+= Math.round((hungryEndTime-hungryStartTime)/1000000);
				writeToLog("HUNGRY",Math.round((hungryEndTime-hungryStartTime)/1000000),debug);
				numberOfHungryTurns++;

				eat(randomGenerator.nextInt(1000));   // eat and release chopsticks

                leftLock.unlock();
                writeToLog("PUTTING DOWN THE LEFT CHOPSTICK",0,debug);

            } finally {
				//Philosopher 1 gets Chopstick 1, Philosopher 2 gets Chopstick 2, and so on until we get to Philosopher n.
				// They have to choose between Chopstick 1 and n.
				// Chopstick 1 is already held up by philosopher 1, so philosopher n can't and won't pick up that Chopstick,
				// We have broken the circular wait()! Ultimately this means that a deadlock isn't possible.
                rightLock.unlock();
                writeToLog("PUTTING DOWN THE RIGHT CHOPSTICK",0,debug);
            }
		}
	}

    private void eat(int eating) {
        this.eatingTime += eating;
        writeToLog("EATING",eating,debug);
        sleep(eating);
		numberOfEatingTurns++;
    }

	private void think(int thinking) {
		this.thinkingTime+=thinking;
		writeToLog("THINKING",thinking,debug);
		sleep(thinking);
		numberOfThinkingTurns++;
	}

    private void writeToLog(String action, int time,boolean record) {
		//You have to have a critical section that is protected and allows only a single writer at a time.
		// Just look up the source code for any logging writer that supports concurrency and you will see that there is only a single thread that writes to the file.
		try {
        	if(record) {//Writing to a normal file by definition is a serialized operation.
				 log.write((System.currentTimeMillis()-startTime) + "ms : Philosopher_"+getId()+" is "+action+" for " + time + "\n");
				 System.out.println(System.currentTimeMillis()-startTime+"ms : Philosopher_"+getId()+" is "+action+" for " + time + "\n");
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    private void sleep(int duration) {
        try { Thread.sleep( duration ); }
        catch ( InterruptedException e ) { Thread.currentThread().interrupt(); }
    }
}
