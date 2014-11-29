/**
 * Cave.java
 * Oct. 30, 2014
 * Nick Radonic
 * gaming party (as in group) structure, used in Project 1 game
 */
package DataTree;

public class Party extends  GameElement{
    private static int partiesCreated = 0;
    String name;

    public Party(String name){
        super(GameLayer.PARTY, partiesCreated+1);
        partiesCreated++;

        this.name = name;
    }

    public Party(int ID, String name){
        super(GameLayer.PARTY, ID);
        this.name = name;
        partiesCreated++;

    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        String partyOutput = String.format("p : %6d : %s",ID,name);
        int creatureCount = this.getChildCount();
        if(creatureCount > 0) {
            partyOutput += " has " +  creatureCount + " creature"+(creatureCount>1?"s":"");
        }

        partyOutput += "\n";

        return partyOutput;
    }

    public String partyOnlyToString(){
        return "p : "+ ID +" : "+name+"\n";
    }

    public Party makeCopy(){
        return new Party(getID(), name);
    }
}
