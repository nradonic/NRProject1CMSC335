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
        String partyOutput = "p : "+ ID +" : "+name;
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
