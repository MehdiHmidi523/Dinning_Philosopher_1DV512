import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class Philosopher implements Runnable {
	
	private int id;
    public static final File logFile = new File("Log.txt");
    public static FileWriter log;
    public static long startTime;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	
	private Random randomGenerator = new Random();
    private long seed = 1000;

    private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;

	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;

		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution 
		 */
		randomGenerator.setSeed(id+seed);

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

	//TODO: impl
	public double getAverageThinkingTime(){
		if (thinkingTime == 0) return 0;
		return (thinkingTime/numberOfThinkingTurns);
	}
	public double getAverageEatingTime(){
		if (eatingTime == 0) return 0;
		return (eatingTime/numberOfEatingTurns);
	}
	public double getAverageHungryTime(){
		if (hungryTime == 0) return 0;
		return (hungryTime/numberOfHungryTurns);
	}

	@Override
	public void run() {
		while (true) {
			Lock rightLock = rightChopStick.getLock();
			Lock leftLock = leftChopStick.getLock();

			rightLock.lock();
			writeToLog("TAKING THE RIGHT CHOPSTICK",0);
			try {
                leftLock.lock();
                writeToLog("TAKING THE LEFT CHOPSTICK",0);
                eat(randomGenerator.nextInt(1000));
                leftLock.unlock();
                writeToLog("PUTTING DOWN THE LEFT CHOPSTICK",0);
            } finally {
                rightLock.unlock();
                writeToLog("PUTTING DOWN THE RIGHT CHOPSTICK",0);
            }
			think(randomGenerator.nextInt(1000));
		}

		/* TODO
		 * Add comprehensive comments to explain your implementation, including deadlock prevention/detection
		 */
	}

    private void eat(int eating) {
        this.eatingTime += eating;
		numberOfEatingTurns++;
        writeToLog("EATING",eating);
        sleep(eating);
    }

      private void think(int thinking) {
		this.thinkingTime+=thinking;
		numberOfThinkingTurns++;
		writeToLog("THINKING",thinking);
        sleep(thinking);
    }

    private void writeToLog(String action, int time) {
        try {
            synchronized (logFile){ log.write((System.currentTimeMillis()-startTime) + "ms : Philosopher_"+getId()+" is "+action+" for " + time + "\n"); }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    private void sleep(int duration) {
        try { Thread.sleep( duration ); }
        catch ( InterruptedException e ) { Thread.currentThread().interrupt(); }
    }
}
