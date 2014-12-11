/**
 * Creature.java
 * Oct. 30, 2014
 * Nick Radonic
 * Creature structure in Project 1 game
 */
package DataTree;

import java.util.Enumeration;

public class Creature extends GameElement{
    private static int creaturesCreated = 0;
    final String name;
    final String creatureType;
    int partyID;
    final double empathy;
    final double fear;
    final double carryingCapacity;
    double age;
    double height;
    double weight;

    public Creature(String name, String creatureType, double empathy, double fear, double carryingCapacity){
        super(GameLayer.CREATURE, creaturesCreated+1);
        creaturesCreated++;

        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
    }

    public Creature(int ID, String creatureType, String  name, int partyID, double empathy, double fear,
                    double carryingCapacity, double age, double height, double weight){
        super(GameLayer.CREATURE, ID);
        creaturesCreated++;

        this.name = name;
        this.creatureType = creatureType;
        this.empathy = empathy;
        this.fear = fear;
        this.carryingCapacity = carryingCapacity;
        this.partyID = partyID;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }


    public void setPartyID(int partyID){
        this.partyID = partyID;
    }

    public int getPartyID(){
        return partyID;
    }

    public String toString(){
        String creatureOutput = String.format("    c : %5d : %9s : %9s : %5d : %4.0f : %4.0f : %5.0f : %4.0f : %5.0f : %5.0f",
                ID, creatureType, name, partyID, empathy, fear, carryingCapacity, age, height, weight);

                //"    c : "+ID+" : "+creatureType+" : "+name+" : "+partyID+" : "+empathy+" : "+fear+" : "+carryingCapacity;
        //creatureOutput += " : "+age +" : "+height +" : "+weight ;

        String treasureOutput = "";
        int treasureCount = 0;

        String artifactOutput = "";
        int artifactCount = 0;

        String jobOutput = "";
        int jobCount = 0;

        Enumeration<GameElement> e = (Enumeration<GameElement>) this.breadthFirstEnumeration();
        while(e.hasMoreElements()){

            GameElement k = e.nextElement();

            if(k.gameLayer == GameLayer.TREASURE) {
                treasureCount++;
            } else if (k.gameLayer == GameLayer.ARTIFACT) {
                artifactCount++;
            } else if (k.gameLayer == GameLayer.JOB) {
                jobCount++;
            }
        }

        if (treasureCount>0 || artifactCount > 0 ){ creatureOutput +=  "  // "+name.trim()+" has " ;}
        creatureOutput += (treasureCount>0) ?  treasureCount + " treasure"+(treasureCount>1?"s":"") : "";

        creatureOutput += (treasureCount>0 && artifactCount > 0) ? " and " : "";
        creatureOutput += (artifactCount>0) ?  artifactCount + " artifact"+(artifactCount>1?"s":"") : "";

        creatureOutput += ((treasureCount>0 || artifactCount>0) && jobCount > 0) ? " and " : "";
        creatureOutput += (jobCount>0) ?  jobCount + " job"+(jobCount>1?"s":"") : "";

        creatureOutput += "\n";
        return creatureOutput;
    }

    public String creatureOnlyToString(){
        return "c : "+ID+" : "+name+"\n";
    }

    public Creature makeCopy(){
        return new Creature(ID, creatureType, name, partyID, empathy, fear, carryingCapacity, age, height, weight);
    }
}
