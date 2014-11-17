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
        partiesCreated++;
        this.ID = partiesCreated;

        this.name = name;
        gameLayer = GameLayer.PARTY;
    }

    public int getID(){
        return ID;
    }

    public Party(int ID, String name){
        this.ID = ID;
        this.name = name;
        gameLayer = GameLayer.PARTY;
        partiesCreated++;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        String partyOutput = "p : "+ ID +" : "+name+"\n";
//        if (gameElementArrayList.size()>0) {
//            //partyOutput += "  // Contains: " + gameElementArrayList.size() + "  creature"+(gameElementArrayList.size()>1 ? "s" : "")+"\n";
//            partyOutput += "  // Contains: " + this.getChildCount() + "  creature"+(this.getChildCount()>1 ? "s" : "")+"\n";
//        }
        //partyOutput += super.toString();
        return partyOutput;
    }

    public String partyOnlyToString(){
        return "p : "+ ID +" : "+name+"\n";
    }
}
