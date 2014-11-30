/**
 * Treasure.java
 * 10/30/14
 * Author Nick Radonic
 * Treasure class game element for Cave Game
 */
package DataTree;

public class Treasure extends GameElement{
    private static int treasuresCreated = 0;
    int creatureID;
    double weight;
    double value;
    String treasureType;

    public Treasure(String treasureType, double weight, double value){
        super(GameLayer.TREASURE, treasuresCreated+1);
        treasuresCreated++;
        this.weight = weight;
        this.value = value;
        this.treasureType = treasureType;
    }

    public Treasure(int ID, String treasureType, int creatureID, double weight, double value){
        super(GameLayer.TREASURE, ID);
        treasuresCreated++;
        this.creatureID = creatureID;
        this.weight = weight;
        this.value = value;
        this.treasureType = treasureType;
    }

    public String toString(){
        String treasureOutput = String.format("          t : %6d : %8s : %6d : %4.1f : %4.1f\n",
                ID, treasureType, creatureID, weight, value);
        return treasureOutput;
    }

    public String treasureOnlyToString(){
        return "t : " + ID + "\n";
    }

    public int getID(){
        return ID;
    }

    public int getCreatureID(){
        return creatureID;
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

    public Treasure makeCopy(){
        return new Treasure(ID, treasureType, creatureID, weight, value);
    }
}
