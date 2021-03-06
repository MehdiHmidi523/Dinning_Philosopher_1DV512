import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DiningPhilosopher {
	
	/*
	 * Controls whether logs should be shown on the console or not. 
	 * Logs should print events such as: state of the philosopher, and state of the chopstick
	 * 		for example: philosopher # is eating; 
	 * 		philosopher # picked up his left chopstick (chopstick #) 
	 */
	public boolean DEBUG = false;
	private final int NUMBER_OF_PHILOSOPHERS = 5;
	private int SIMULATION_TIME = 10000;
	private int SEED = 0;

	ExecutorService executorService = null;
	ArrayList<Philosopher> philosophers = null;
	ArrayList<ChopStick> chopSticks = null;


	public void start() throws InterruptedException {
		try {
			/*
			 * First we start two non-adjacent threads, which are T1 and T3
			 */
			for (int i = 1; i < NUMBER_OF_PHILOSOPHERS; i+=2) {
				executorService.execute(philosophers.get(i));
				Thread.sleep(50); //makes sure that this thread kicks in before the next one
			}

			/*
			 * Now we start the rest of the threads, which are T0, T2, and T4
			 */
			for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i+=2) {
				executorService.execute(philosophers.get(i));
				Thread.sleep(50); //makes sure that this thread kicks in before the next one
			}
			
			// Main thread sleeps till time of simulation
			//Thread.sleep(SIMULATION_TIME);

			while ((System.currentTimeMillis()-Philosopher.startTime) <= SIMULATION_TIME) {
                try {
                    Thread.sleep(500L);  // Sleep 1/2 second
                } catch (InterruptedException e) {
                    // Someone woke us up during sleep, that's OK
                }
			}

			executorService.shutdownNow(); // Interrupt all threads

		} finally {
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MILLISECONDS);
			System.out.println("Finished.");
		}
	}

	public void initialize(int simulationTime, int randomSeed) {
		SIMULATION_TIME = simulationTime;
		SEED = randomSeed;
		
		philosophers = new ArrayList<>(NUMBER_OF_PHILOSOPHERS);
		chopSticks = new ArrayList<>(NUMBER_OF_PHILOSOPHERS);
		
		//create the executor service
		executorService = Executors.newFixedThreadPool(NUMBER_OF_PHILOSOPHERS);

		//Add the needed number of Chopsticks to the Table which corresponds to the number of sitting philosophers.
		for(int i = 1; i<=NUMBER_OF_PHILOSOPHERS;i++) chopSticks.add(new ChopStick(i));

		//Add the number of philosophers corresponding to this session and assign left and right chopstick to them.
		for(int i =1; i<=NUMBER_OF_PHILOSOPHERS;i++)
            philosophers.add(new Philosopher(i-1,chopSticks.get(i% NUMBER_OF_PHILOSOPHERS),chopSticks.get(i-1),SEED,DEBUG));

        // initialize logfile
        try {
            PrintWriter writer = new PrintWriter(Philosopher.logFile);
            writer.print("|");
            writer.close();
            Philosopher.log = new FileWriter(Philosopher.logFile); // initialize logWriter
        } catch (IOException e) { e.printStackTrace(); }
        // initialize time for log file
        Philosopher.startTime = System.currentTimeMillis();
	}

	public ArrayList<Philosopher> getPhilosophers() { return philosophers; }
	
	/*
	 * The following code prints a table where each row corresponds to one of the Philosophers, 
	 * Columns correspond to the Philosopher ID (PID), average think time (ATT), average eating time (AET), average hungry time (AHT), number of thinking turns(#TT), number of eating turns(#ET), and number of hungry turns(#HT).
	 * This table should be printed regardless of the DEBUG value
	 */
	public void printTable() {
		DecimalFormat df2 = new DecimalFormat(".##");
		System.out.println("\n---------------------------------------------------");
		System.out.println("PID \tATT \tAET \tAHT \t#TT \t#ET \t#HT");
		
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "\t"
					+ df2.format(p.getAverageThinkingTime()) + "\t"
					+ df2.format(p.getAverageEatingTime()) + "\t"
					+ df2.format(p.getAverageHungryTime()) + "\t"
					+ p.getNumberOfThinkingTurns() + "\t"
					+ p.getNumberOfEatingTurns() + "\t"
					+ p.getNumberOfHungryTurns() + "\t");
		}
		
		System.out.println("---------------------------------------------------\n");
	}

}
