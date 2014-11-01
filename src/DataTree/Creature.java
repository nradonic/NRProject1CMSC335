/**
 * Creature.java
 * Oct. 30, 2014
 * Nick Radonic
 * Creature structure in Project 1 game
 */
package DataTree;

import java.util.ArrayList;

public class Creature {
    private static int creaturesCreated = 0;
    final int ID;
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
        ID = creaturesCreated;
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
            treasures.add(treasure);
    }

    public void addArtifact(Artifact artifact){
        artifact.setCreatureID(ID);
        artifacts.add(artifact);
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
            creatureOutput += "        // Contains " + treasures.size() + " treasures\n";
        }
        for (Treasure tr : treasures){
            creatureOutput += tr.toString();
        }
        if (artifacts.size()>0){
            creatureOutput += "        // Contains "+artifacts.size()+" artifacts\n";
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
