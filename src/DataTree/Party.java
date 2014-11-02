/**
 * Cave.java
 * Oct. 30, 2014
 * Nick Radonic
 * gaming party (as in group) structure in Project 1 game
 */
package DataTree;

import java.util.ArrayList;
import java.util.Optional;

public class Party {
    private static int partiesCreated = 0;
    int ID = 0;
    String name;
    ArrayList<Creature> creatures = new ArrayList<Creature>();

    public Party(String name){
        partiesCreated++;
        this.ID = partiesCreated;
        for (Creature  c: creatures){
            c.setPartyID(ID);
        }
        this.name = name;
    }

    public int getID(){
        return ID;
    }

    public Party(int ID, String name){
        this.ID = ID;
        this.name = name;
        partiesCreated++;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        String partyOutput = "p : "+ ID +" : "+name+"\n";
        if (creatures.size()>0) {
            partyOutput += "  // Contains: " + creatures.size() + "  creatures\n";
        }
        for (Creature c : creatures){
            partyOutput += c.toString();
        }
        return partyOutput;
    }

    public String partyOnlyToString(){
        return "p : "+ ID +" : "+name+"\n";
    }

    public void addCreature(Creature creature){
        creature.setPartyID(ID);
        Optional<Creature> cExisting = creatures.stream().filter(p->p.ID == creature.ID).findFirst();
        if(cExisting.isPresent()){
            Creature tempC = cExisting.get();
            tempC = creature;
        } else {
            creatures.add(creature);
        }
    }
}
