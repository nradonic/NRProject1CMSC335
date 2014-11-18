/** Cave.java
* Oct. 30, 2014
* Nick Radonic
* top level cave structure in Project 1 game
*/

package DataTree;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Cave extends GameElement{
    private String name;

    public Cave displayCave;
    public Cave unassignedCaveCreatures;
    public Cave unassignedCaveTreasures;
    public Cave unassignedCaveArtifacts;

    public Cave(String name){
        super(GameLayer.CAVE, 1);
        this.name = name;

        unassignedCaveCreatures = new Cave("Unassigned Cave Creatures", 0);
        unassignedCaveTreasures = new Cave("Unassigned Cave Treasures", 0);
        unassignedCaveArtifacts = new Cave("Unassigned Cave Artifacts", 0);
        displayCave = new Cave(name,0);
    }

    private Cave(String name, int dummy){
        this.name = name;
    }

    public void addGameElement(GameElement gameElement){
        boolean result = addGameElementTree(gameElement);
        if (!result){
            switch (gameElement.getGameLayer()){
                case CREATURE:{
                    unassignedCaveCreatures.add(gameElement);
//                    unassignedCreaturesTree.treeDidChange();
//                    unassignedCreaturesTree.updateUI();
                }
                break;
                case TREASURE:{
                    unassignedCaveTreasures.add(gameElement);
//                    unassignedTreasuresTree.treeDidChange();
//                    unassignedArtifactsTree.updateUI();
                }
                break;
                case ARTIFACT:{
                    unassignedCaveArtifacts.add(gameElement);
//                    unassignedArtifactsTree.treeDidChange();
//                    unassignedArtifactsTree.updateUI();
                }
                break;
                default:break;
            }
        }
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


}
