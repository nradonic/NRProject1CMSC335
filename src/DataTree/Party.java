/**
 * Cave.java
 * Oct. 30, 2014
 * Nick Radonic
 * gaming party (as in group) structure in Project 1 game
 */
package DataTree;

import java.util.ArrayList;

public class Party {
    private static int partiesCreated = 0;
    String name;
    final int partyID;
    ArrayList<Creature> creatures = new ArrayList<Creature>();

    public Party(String name){
        partiesCreated++;
        partyID = partiesCreated;
        for (Creature  c: creatures){
            c.setPartyID(partyID);
        }
        this.name = name;
    }

    public int getPartyID(){
        return partyID;
    }

    public Party(int ID, String name){
        this.partyID = ID;
        this.name = name;
        partiesCreated++;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        String partyOutput = "p : "+partyID+" : "+name+"\n";
        if (creatures.size()>0) {
            partyOutput += "  // Contains: " + creatures.size() + "  creatures\n";
        }
        for (Creature c : creatures){
            partyOutput += c.toString();
        }
        return partyOutput;
    }

    public String partyOnlyToString(){
        return "p : "+partyID+" : "+name+"\n";
    }

    public void addCreature(Creature creature){
        creature.setPartyID(partyID);
        creatures.add(creature);
    }
}
