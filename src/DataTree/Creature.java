/**
 * Creature.java
 * Oct. 30, 2014
 * Nick Radonic
 * Creature structure in Project 1 game
 */
package DataTree;

import java.util.ArrayList;
import java.util.Optional;

public class Creature {
    private static int creaturesCreated = 0;
    int ID = 0;
    String name;
    String creatureType;
    int partyID;
    double empathy;
    double fear;
    double carryingCapacity;
    ArrayList<Treasure> treasures = new ArrayList<Treasure>();
    ArrayList<Artifact> artifacts = new ArrayList<Artifact>();


    public Creature(String name, String creatureType, double empathy, double fear, double carryingCapacity){
        creaturesCreated++;
        this.ID = creaturesCreated;
        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
    }

    public Creature(int ID, String creatureType, String  name, int partyID, double empathy, double fear, double carryingCapacity){
        this.ID = ID;
        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
        this.partyID = partyID;
    }

    public void addTreasure(Treasure treasure){
            treasure.setCreatureID(ID);
        Optional<Treasure> tExisting = treasures.stream().filter(p->p.ID == treasure.ID).findFirst();
        if(tExisting.isPresent()){
            Treasure tempT = tExisting.get();
            tempT = treasure;
        } else {
            treasures.add(treasure);
        }
    }

    public void addArtifact(Artifact artifact){
        artifact.setCreatureID(ID);
        Optional<Artifact> aExisting = artifacts.stream().filter(p->p.ID == artifact.ID).findFirst();
        if(aExisting.isPresent()){
            Artifact tempA = aExisting.get();
            tempA = artifact;
            } else {
            artifacts.add(artifact);
        }
    }

    public void setPartyID(int partyID){
        this.partyID = partyID;
    }

    public int getPartyID(){
        return partyID;
    }

    public String toString(){
        String creatureOutput = "    c : "+ID+" : "+creatureType+" : "+name+" : "+partyID+" : "+empathy+" : "+fear+" : "+carryingCapacity+"\n";
        if (treasures.size()>0) {
            creatureOutput += "        // "+name+" has " + treasures.size() + " treasure"+(treasures.size()>1?"s":"")+"\n";
        }
        for (Treasure tr : treasures){
            creatureOutput += tr.toString();
        }
        if (artifacts.size()>0){
            creatureOutput += "        // "+name+" has "+artifacts.size()+" artifact"+(artifacts.size()>1?"s":"")+"\n";
        }
        for (Artifact art: artifacts){
            creatureOutput += art.toString();
        }
        return creatureOutput;
    }

    public String creatureOnlyToString(){
        return "c : "+ID+" : "+name+"\n";
    }
}
