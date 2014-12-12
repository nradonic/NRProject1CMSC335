package DataTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * Created by NickRadonic on 11/28/14.
 */
public class Job extends GameElement implements Runnable {

    final String jobType;
    int creatureID = 0;
    private HashMap<String, Double> resources = new HashMap<>();

    private JobState jobState = JobState.NEW;

    private final int MILLISPERSECOND = 1000;

    private double time = 0;
    private long startTime;
    private long elapsedTime; // runs till this goes to input 'time'

    private boolean pause = false;
    private boolean cancel = false;
    private static ReentrantLock lock = new ReentrantLock();
    private static HashMap<Integer, Boolean> creaturesList = new HashMap<>();

    public Job(int ID, String jobType, int creatureID, Double time, HashMap<String, Double> resources ){
        super(GameLayer.JOB, ID);
        this.jobType = jobType;
        this.creatureID = creatureID;
        this.resources = resources;
        this.time = time;
        synchronized (creaturesList){
            if(creaturesList.containsKey(creatureID) == false){
                creaturesList.put(creatureID, (Boolean) false);
            }
        }
    }

    public void run(){
        long intervalTime = (long) (time * 1000) - elapsedTime;
        jobState = JobState.BLOCKED;

        Boolean creatureIsActive = false;
//        while(creatureIsActive == false) {
//            synchronized (creaturesList) {
//                //System.out.printf("Job Synch 1: Creature ID: %6d  Job ID: %6d creatures list value: %B\n",creatureID, this.getID(), creaturesList.get(creatureID));
//                if(!creaturesList.get(creatureID)) {
//                    creaturesList.put(creatureID, Boolean.TRUE);
//                    creatureIsActive = true;
//                }
//            }
//            if(!creatureIsActive){try{Thread.sleep(100);} catch(Exception ex){};}
//        }
        cancel = false;
        pause = false;
        startTime = System.currentTimeMillis();

        synchronized (lock){
            jobState = JobState.RUNNING;

            long runTime = 0;

//        elapsedTime = 0;

            System.out.printf("Start Creature ID %6d Job %12s  Job ID %6d  Time %4.0f IntervalTime %8d Run Time %5d Elapsed Time %5d Progress %3d Status %8s\n",
                    creatureID, getName(), getID(), time, intervalTime, runTime, elapsedTime, getProgress(), jobState.name());

//            while (elapsedTime < intervalTime && pause != true && cancel != true) {
//                startTime = System.currentTimeMillis();
//                try {
//                    System.out.printf("Job %12s  Job ID %6d  Time %4.0f IntervalTime %8d Run Time %5d Elapsed Time %5d Progress %3d Status %8s\n",
//                            getName(), getID(), time, intervalTime, runTime, elapsedTime, getProgress(), jobState.name());
//                    runTime = System.currentTimeMillis() - startTime;
//                    sleep(Math.max(Math.min(1000, (int) Math.floor(intervalTime - elapsedTime)), 0));
//                    runTime = System.currentTimeMillis() - startTime;
//
//                } catch (InterruptedException ex) {
//                    runTime = System.currentTimeMillis() - startTime;
//                    System.out.printf("timeout exception job: %d  Elapsed(millisec): %d\n", this.getID(), elapsedTime + runTime);
//
//                } finally {
//                    elapsedTime += System.currentTimeMillis() - startTime; // add increment in operating time
//                    // System.out.printf("finally sleep block: job: %d exited  Elapsed(millisec): %d\n", this.getID(), elapsedTime);
//                }
//            }
            long elapsedTimeReference = elapsedTime;
            while (System.currentTimeMillis() - startTime < intervalTime && !pause && !cancel){
                elapsedTime = elapsedTimeReference + System.currentTimeMillis() - startTime;
            }
            elapsedTime = elapsedTimeReference + System.currentTimeMillis() - startTime;
            System.out.printf("Exit block: Creature ID %6d job: %d exited  Elapsed(millisec): %d\n", creatureID, this.getID(), elapsedTime);
        }
        synchronized (creaturesList) {
            creaturesList.put(creatureID, Boolean.FALSE);
            creatureIsActive = false;
        }

        if (elapsedTime >= time * MILLISPERSECOND) {
            jobState = JobState.FINISHED;
        } else if (pause == true) {
            jobState = JobState.PAUSED;
        } else if (cancel == true) {
            jobState = JobState.CANCELLED;
        }
        System.out.printf("Exit job %10s  Job ID %6d  Time %4.0f IntervalTime %8d Elapsed Time %5d Progress %3d Status %8s\n",
                getName(), getID(), time, intervalTime, elapsedTime, getProgress(), jobState.name());
    }

    public void pause(){
        pause = true;
    }

    public void cancel(){
        cancel = true;
    }

    public int getID(){
        return ID;
    }

    String getName(){
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

    public int getProgress(){
        int progress = 0;
        if (time == 0 ) {
            progress = 1;
        } else {
            progress = (int) Math.floor(Math.min(elapsedTime / time / MILLISPERSECOND, 1) * 100);
        }
        return progress;
    }

    public double getTime(){return time;}

    public void changeJobState(JobState js){
        this.jobState = js;
    }

    public JobState getJobState(){
        return jobState;
    }
}
