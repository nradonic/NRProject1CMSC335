/**
 * Treasure.java
 * Created by Nick Radonic on 10/30/14.
 * Treasure class
 */
package DataTree;

public class Treasure extends GameElement{
    private static int treasuresCreated = 0;
    int creatureID;
    double weight;
    double value;
    String treasureType;

    public Treasure(String treasureType, double weight, double value){
        treasuresCreated++;
        this.ID = treasuresCreated;

        this.weight = weight;
        this.value = value;
        this.treasureType = treasureType;
        gameLayer = GameLayer.TREASURE;
    }

    public Treasure(int ID, String treasureType, int creatureID, double weight, double value){
        treasuresCreated++;
        this.ID = ID;
        this.creatureID = creatureID;
        this.weight = weight;
        this.value = value;
        this.treasureType = treasureType;
        gameLayer = GameLayer.TREASURE;
    }

    public String toString(){
        String treasureOutput = "          t : "+ID+" : "+treasureType+" : "+creatureID+" : "+weight+" : "+value+"\n";
        return treasureOutput;
    }

    public String treasureOnlyToString(){
        return "t : " + ID + "\n";
    }

    public int getID(){
        return ID;
    }

    public int getCreatureID(){
        return ID;
    }

    public void setCreatureID(int creatureID){
        this.creatureID = creatureID;
    }

    public double getWeight(){
        return weight;
    }

    public double getValue(){
        return value;
    }

    public String getTreasureType(){
        return treasureType;
    }

}
