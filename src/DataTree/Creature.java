/**
 * Creature.java
 * Oct. 30, 2014
 * Nick Radonic
 * Creature structure in Project 1 game
 */
package DataTree;

import java.util.ArrayList;
import java.util.Optional;

public class Creature extends GameElement{
    private static int creaturesCreated = 0;
    String name;
    String creatureType;
    int partyID;
    double empathy;
    double fear;
    double carryingCapacity;

    public Creature(String name, String creatureType, double empathy, double fear, double carryingCapacity){
        this.ID = creaturesCreated;
        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
        gameLayer = GameLayer.CREATURE;
    }

    public Creature(int ID, String creatureType, String  name, int partyID, double empathy, double fear, double carryingCapacity){
        creaturesCreated++;
        this.ID = ID;
        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
        this.partyID = partyID;
        gameLayer = GameLayer.CREATURE;
    }

    public void addTreasure(Treasure treasure){
            treasure.setCreatureID(ID);
        Optional<GameElement> tExisting = gameElementArrayList.stream().filter(p->p.ID == treasure.ID).findFirst();
        if(tExisting.isPresent()){
            GameElement tempT = tExisting.get();
            tempT = treasure;
        } else {
            gameElementArrayList.add(treasure);
        }
    }

    public void addArtifact(Artifact artifact){
        artifact.setCreatureID(ID);
        Optional<GameElement> aExisting = gameElementArrayList.stream().filter(p->p.ID == artifact.ID).findFirst();
        if(aExisting.isPresent()){
            GameElement tempA = aExisting.get();
            tempA = artifact;
            } else {
            gameElementArrayList.add(artifact);
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

        String treasureOutput = "";
        int treasureCount = 0;

        String artifactOutput = "";
        int artifactCount = 0;

        for (GameElement gameElement: gameElementArrayList){
            if(gameElement.getGameLayer()==GameLayer.TREASURE){
                treasureOutput += gameElement.toString();
                treasureCount++;
            }

            if(gameElement.getGameLayer()==GameLayer.ARTIFACT){
                artifactOutput += gameElement.toString();
                artifactCount++;
            }
        }
        creatureOutput += (treasureCount>0) ? "        // "+name+" has " + treasureCount + " treasure"+(treasureCount>1?"s":"")+"\n" : "";
        creatureOutput += treasureOutput;
        creatureOutput += (artifactCount>0) ? "        // "+name+" has " + artifactCount + " artifact"+(artifactCount>1?"s":"")+"\n" : "";
        creatureOutput += artifactOutput;

        return creatureOutput;
    }

    public String creatureOnlyToString(){
        return "c : "+ID+" : "+name+"\n";
    }
}
