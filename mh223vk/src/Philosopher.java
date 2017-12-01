import java.util.Random;

public class Philosopher implements Runnable {
	
	private int id;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	
	private Random randomGenerator = new Random();
	
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
                eat(pseudo.nextInt(1001));
            }

        }

		/* TODO
		 * Add comprehensive comments to explain your implementation, including deadlock prevention/detection
		 */
	}

    private void eat(int i) { //TODO: impl

    }

    private boolean possibleToEat() {
	    return (leftChopStick.TAKEN == true) && (rightChopStick.TAKEN == true);
    }

    private void think(int i) { //TODO: impl

    }

    private boolean higherStateOfBeing() {
	    return hungryTime>=3000;
    }


}
