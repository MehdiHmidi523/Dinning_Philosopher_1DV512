import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Mehdi on 03/12/2017 for the Dinning_Philosopher_1DV512 project.
 */
public class Log{

    public static final File logFile = new File("Log.txt");
    public static FileWriter log;
    private int startTime;

    public Log(int startTime){
        this.startTime=startTime;

    }

    public void writeToLog(String action, int time) {
        try {
            synchronized (logFile){ log.write((System.currentTimeMillis()-startTime) + "ms : Philosopher_"+Thread.currentThread().getId()+" is "+action+" for " + time + "\n"); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Delay(int ms){
        try{
            Thread.currentThread().sleep(ms);
        } catch(InterruptedException ex){ }
    }
}
