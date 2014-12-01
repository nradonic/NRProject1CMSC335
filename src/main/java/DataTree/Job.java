package DataTree;

import java.util.HashMap;
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

    JobState jobState = JobState.NEW;
    int MILLISPERSECOND = 1000;

    long startTime;
    long elapsedTime;

    boolean pause = false;
    boolean cancel = false;

    public Job(int ID, String jobType, int creatureID, Double time, HashMap<String, Double> resources ){
        super(GameLayer.JOB, ID);
        this.jobType = jobType;
        this.creatureID = creatureID;
        this.resources = resources;
        this.time = time;
    }

    public void run(){
        cancel = false;
        pause = false;

        startTime = System.currentTimeMillis();
        jobState = JobState.RUNNING;
//        elapsedTime = 0;

        System.out.printf("Start Job %12s  Job ID %6d  Time %4.0f Elapsed Time %5d Progress %4.4f Status %8s\n",
                getName(), getID(), time, elapsedTime, getProgress(), jobState.name());

        while (elapsedTime < time * MILLISPERSECOND && pause != true && cancel != true) {
            try {
//                System.out.printf("Job %12s  Job ID %6d  Time %4.0f Elapsed Time %5d Progress %4.4f Status %8s\n",
//                        getName(), getID(), time, elapsedTime, getProgress(), jobState.name());
                elapsedTime = System.currentTimeMillis() - startTime;
                sleep(Math.min( 1000,(int) Math.floor(time*MILLISPERSECOND - elapsedTime) ));
                elapsedTime = System.currentTimeMillis() - startTime;

            } catch (InterruptedException ex) {
                System.out.printf("timeout exception job: %d  Elapsed(millisec): %d\n", this.getID(), elapsedTime);

            } finally {
                elapsedTime = System.currentTimeMillis() - startTime;
               // System.out.printf("finally sleep block: job: %d exited  Elapsed(millisec): %d\n", this.getID(), elapsedTime);
            }
        }

        if( elapsedTime >= time * MILLISPERSECOND) {
            jobState = JobState.FINISHED;
        } else if (pause == true ){
            jobState = JobState.PAUSED;
        } else if (cancel == true){
            jobState = JobState.CANCELLED;
        }
        System.out.printf("Exit job %10s  Job ID %6d  Time %4.0f Elapsed Time %5d Progress %4.4f Status %8s\n",
                getName(), getID(), time, elapsedTime, getProgress(), jobState.name());

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
        this.jobState = js;
    }

    public JobState getJobState(){
        return jobState;
    }

}
