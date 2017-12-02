import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Philosopher implements Runnable {
	
	private int id;
    public static final File logFile = new File("src/Log.txt");
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
        new Thread(this).start();
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


	public double getAverageThinkingTime() {
		/* TODO
		 * Return the average thinking time
		 * Add comprehensive comments to explain your implementation
		 */
		return 0;
	}

	public double getAverageEatingTime() {
		/* TODO
		 * Return the average eating time
		 * Add comprehensive comments to explain your implementation
		 */
		return 0;
	}

	public double getAverageHungryTime() {
		/* TODO
		 * Return the average hungry time
		 * Add comprehensive comments to explain your implementation
		 */
		return 0;
	}

	@Override
	public void run() {

		while(!Thread.currentThread().isInterrupted() && !higherStateOfBeing()){ // if session not interrupted and philosopher did not starve to Nirvana
            Random pseudo = new Random();
            think(pseudo.nextInt(1001));

            //first to finish thinking and feel hungry reaches for the Chopsticks and checks
            if(!possibleToEat()) {
                try {
                    Thread.currentThread().wait();  //checks also death.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                eat(pseudo.nextInt(1001)); // philosopher takes the chopsticks (lock them ?)
            }

        }

		/* TODO
		 * Add comprehensive comments to explain your implementation, including deadlock prevention/detection
		 */
	}

    public void setSeed(long seed) { this.seed = seed; }

    private void eat(int eating) {
        this.eatingTime += eating;
        writeToLog("EATING",eating);
        sleep(eating);
    }

    private boolean possibleToEat() {
	    return (leftChopStick.TAKEN) && (rightChopStick.TAKEN);
    }

    private void think(int thinking) {
        this.thinkingTime+=thinking;
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

    private boolean higherStateOfBeing() { // philosopher dies after three consecutive rounds of waiting
	    return hungryTime>=3000;
    }

    private void sleep(int duration) {
        try { Thread.sleep( duration ); }
        catch ( InterruptedException e ) { Thread.currentThread().interrupt(); }
    }


}
