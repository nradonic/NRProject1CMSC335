package DataTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by NickRadonic on 11/28/14.
 */
public class Job extends GameElement implements Runnable {

    String jobType;
    int creatureID = 0;
    double time = 0;
    HashMap<String, Double> resources = new HashMap<>();

    public Job(int ID, String jobType, int creatureID, Double time, HashMap<String, Double> resources ){
        super(GameLayer.JOB, ID);
        this.jobType = jobType;
        this.creatureID = creatureID;
        this.resources = resources;
        this.time = time;
    }

    public void run(){

    }

    public int getID(){
        return ID;
    }

    public String getName(){
        return jobType;
    }

    public int getCreatureID(){
        return ID;
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
}
