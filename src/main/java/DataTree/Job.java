package DataTree;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by NickRadonic on 11/28/14.
 */
public class Job extends GameElement implements Runnable {

    String jobType;
    int creatureID = 0;
    double time = 0;
    HashMap<String, Double> resources = new HashMap<>();

    double progress = 0.0;
    JobState js = JobState.NEVERSTARTED;
    int MILLISPERSECOND = 1000;
    long startTime;
    long elapsedTime;

    public Job(int ID, String jobType, int creatureID, Double time, HashMap<String, Double> resources ){
        super(GameLayer.JOB, ID);
        this.jobType = jobType;
        this.creatureID = creatureID;
        this.resources = resources;
        this.time = time;
    }

    public void run(){
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        while (elapsedTime < time * MILLISPERSECOND) {
            try {
                elapsedTime = System.currentTimeMillis() - startTime;
                sleep(1000);
            } catch (InterruptedException ex) {System.out.println("timeout exception "+ex); }
        }
    }

    public int getID(){
        return ID;
    }

    public String getName(){
        return jobType;
    }

    public int getCreatureID(){
        return creatureID;
    }

    public String toString(){
        String jobOutput = String.format("          j : %6d : %8s : %6d : %4.1f ", ID, jobType, creatureID, time);

        for (Map.Entry<String, Double> me : resources.entrySet()){
            jobOutput += String.format(": %8s : %4.1f", me.getKey(), me.getValue());
        }
        jobOutput += "\n";
        return jobOutput;
    }

    public Job makeCopy(){
        return new Job(ID, jobType, creatureID, time, resources);
    }

    public String getJobType(){
        return jobType;
    }

    public double getProgress(){
        double progress = 0;
        if (time == 0 ) {
            progress = 1;
        } else {
            progress = Math.min(elapsedTime / time / MILLISPERSECOND, 1);
        }
        return progress;  // todo
    }

    public void changeJobState(JobState js){
        this.js = js;
    }

}
