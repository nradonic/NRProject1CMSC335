/** Cave.java
 * Oct. 30, 2014
 * Nick Radonic
 * top level cave structure in Project 1 game
 */

package DataTree;

import java.util.*;

public class Cave extends GameElement{
    private final String name;

    private Cave displayCave;
    public Cave unassignedCaveCreatures;
    public Cave unassignedCaveTreasuresArtifactsJobs;
//    public Cave unassignedCaveArtifacts;

    public Cave(String name){
        super(GameLayer.CAVE, 1);
        this.name = name;

        unassignedCaveCreatures = new Cave("Unassigned Cave Creatures", 0);
        unassignedCaveTreasuresArtifactsJobs = new Cave("Unassigned Cave Treasures, Artifacts and Jobs", 0);
//        unassignedCaveArtifacts = new Cave("Unassigned Cave Artifacts", 0);
        displayCave = new Cave(name,0);
    }

    private Cave(String name, int dummy){
        this.name = name;
    }

    public void addGameElement(GameElement gameElement){
        boolean result = addGameElementTree(gameElement);
        if (!result){
            switch (gameElement.getGameLayer()){
                case CREATURE: {
                    addToUnassignedElements(gameElement, unassignedCaveCreatures);
                    break;
                }
                case TREASURE:{
                    addToUnassignedElements(gameElement, unassignedCaveTreasuresArtifactsJobs);
                    break;
                }
                case ARTIFACT:{
                    addToUnassignedElements(gameElement, unassignedCaveTreasuresArtifactsJobs);
                    break;
                }
                case JOB:{
                    addToUnassignedElements(gameElement, unassignedCaveTreasuresArtifactsJobs);
                    break;
                }
                default:break;
            }
        }
    }

    private void addToUnassignedElements(GameElement gameElement, Cave unassignedCave){
        Enumeration<GameElement> gameElementEnumeration = (Enumeration<GameElement>) unassignedCave.children();

        while (gameElementEnumeration.hasMoreElements()) {
            GameElement ge = gameElementEnumeration.nextElement();
            if(ge.getID() == gameElement.getID()) {
                unassignedCave.remove(ge);
                break;
            }
        }
        unassignedCave.add(gameElement);
    }

    public String getName(){
        return name;
    }

    public String toString(){
        String caveOutput = "// Cave: "+name+"\n\n";
        String items = " Item" +((this.getChildCount()>1 || this.getChildCount()==0) ? "s" : "");
        String party = (" part"+(this.getChildCount()>1?"ies":"y"));
        String item_message = name.startsWith("Unassigned Cave") ? items :  party;
        caveOutput += "// Contains: "+this.getChildCount()+item_message+"\n";
        return caveOutput;
    }


    public Cave makeCopy(){
        return new Cave(name);
    }

    public void resetDataStructures(){
        removeAllChildren();
        unassignedCaveCreatures.removeAllChildren();
        unassignedCaveTreasuresArtifactsJobs.removeAllChildren();
        clearTreeMaps();
    }

}
