package DataTree;

import ResourcePool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * Created by NickRadonic on 11/28/14.
 */
public class Job extends GameElement implements Runnable {

    final String jobType;
    int creatureID = 0;
    private HashMap<String, Integer> resources = new HashMap<>();

    private JobState jobState = JobState.NEW;
    private Creature creature;

    private final int MILLISPERSECOND = 1000;

    private final double time;
    private long startTime;
    private long elapsedTime; // runs till this goes to input 'time'

    private boolean pause = false;
    private boolean cancel = false;
    private static ReentrantLock lock = new ReentrantLock();
    private static HashMap<Integer, ReentrantLock> creaturesList = new HashMap<>();

    private PartyResourcePools partyResourcePool;

    public Job(int ID, String jobType, int creatureID, Double time, HashMap<String, Integer> resources ){
        super(GameLayer.JOB, ID);
        this.jobType = jobType;
        this.creatureID = creatureID;
        this.resources = copyHashMap(resources);
        this.time = time;
        synchronized (creaturesList){
            if(creaturesList.containsKey(creatureID) == false){
                creaturesList.put(creatureID, new ReentrantLock());
            }
        }
    }

    public void run(){
        long intervalTime = 0;
        HashMap<String, Integer> tempResources = copyHashMap(resources);
        while (!partyResourcePool.checkResources(tempResources)){
            jobState = JobState.NEEDSRESOURCES;
            synchronized (new ReentrantLock()){
                try{
                    sleep(100);
                }catch(Exception ex){}
            }
        }
        jobState = JobState.BLOCKED;
        intervalTime = executionLoop();

        if (elapsedTime >= time * MILLISPERSECOND) {
            jobState = JobState.FINISHED;
        } else if (pause == true) {
            jobState = JobState.PAUSED;
        } else if (cancel == true) {
            jobState = JobState.CANCELLED;
            elapsedTime = 0;
        }
        partyResourcePool.releaseResources(tempResources);
        System.out.printf("Exit Creature %5d job %10s  Job ID %6d  Time %4.0f IntervalTime %8d Elapsed Time %5d Progress %3d Status %8s\n",
                creatureID, getName(), getID(), time, intervalTime, elapsedTime, getProgress(), jobState.name());
        //System.out.printf("Cancel: %B Pause %B JobStatus %S\n", cancel, pause, jobState.toString());
    }

    private HashMap<String, Integer>  copyHashMap(HashMap<String, Integer> hm){
        HashMap<String, Integer> localResources = new HashMap<>();
        for(String string : hm.keySet()){
            localResources.put(string, hm.get(string));
        }
        return localResources;
    }

    public HashMap<String, Integer> getResourceList(){
        return resources;
    }

    private long executionLoop() {
        long intervalTime = (long) (time * 1000) - elapsedTime;
        long localInterval = 0;
        cancel = false;
        pause = false;

        ReentrantLock tempRL = creaturesList.get(creatureID);
        synchronized (tempRL){
            jobState = JobState.RUNNING;

            long runTime = 0;
            System.out.printf("Start Creature ID %6d Job %12s  Job ID %6d  Time %4.0f IntervalTime %8d Run Time %5d Elapsed Time %5d Progress %3d Status %8s\n",
                    creatureID, getName(), getID(), time, intervalTime, runTime, elapsedTime, getProgress(), jobState.name());
            System.out.printf("Startup: %B Pause %B JobStatus %S\n", cancel, pause, jobState.toString());
            while (localInterval < intervalTime && pause != true && cancel != true) {
                startTime = System.currentTimeMillis();
                try {
//                    System.out.printf("Job %12s  Job ID %6d  Time %4.0f IntervalTime %8d Run Time %5d Elapsed Time %5d Progress %3d Status %8s\n",
//                            getName(), getID(), time, intervalTime, runTime, elapsedTime, getProgress(), jobState.name());
                    sleep(Math.max(Math.min(1000, (int) Math.floor(intervalTime - elapsedTime)), 0));
                } catch (InterruptedException ex) {
                    runTime = System.currentTimeMillis() - startTime;
                    System.out.printf("timeout exception job: %d  Elapsed(millisec): %d\n", this.getID(), elapsedTime + runTime);

                } finally {
                    runTime = System.currentTimeMillis()-startTime;
                    // add increment in operating time
                    // System.out.printf("finally sleep block: job: %d exited  Elapsed(millisec): %d\n", this.getID(), elapsedTime);
                }
                elapsedTime += runTime;
                localInterval += runTime;
            }
            System.out.printf("Exit block: Creature ID %6d job: %d exited  Elapsed(millisec): %d\n", creatureID, this.getID(), elapsedTime);
        }
        return intervalTime;
    }

    public synchronized void pause(){
        JobState jobState1 = getJobState();
        if(jobState1==JobState.BLOCKED ||
                jobState1==JobState.RUNNING) {
            pause = true;
        }
    }

    public synchronized void cancel(){
        JobState jobState1 = getJobState();
        if (jobState1==JobState.PAUSED) {
            cancel = true;
            pause = false;
            jobState = JobState.CANCELLED;
            elapsedTime = 0;
        } if (jobState1 == JobState.RUNNING ||
                jobState1 ==  JobState.BLOCKED){
            pause = true;
            cancel = false;
        }
    }

    public void setResourcePool(PartyResourcePools partyResourcePool){

        this.partyResourcePool = partyResourcePool;
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

        for (Map.Entry<String, Integer> me : resources.entrySet()){
            jobOutput += String.format(": %8s : %4d", me.getKey(), me.getValue());
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
