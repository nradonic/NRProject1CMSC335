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

    Creature unassignedCaveCreatures;
    public JTree unassignedCreatures = new JTree(unassignedCaveCreatures);

    Treasure unassignedCaveTreasures;
    public JTree unassignedTreasures = new JTree(unassignedCaveTreasures);

    Artifact unassignedCaveArtifacts;
    public JTree unassignedArtifacts = new JTree(unassignedCaveArtifacts);

    public Cave(String name){
        super(GameLayer.CAVE, 1);
        this.name = name;
    }

    public void addGameElement(GameElement gameElement){
        boolean result = addGameElementTree(gameElement);
        if (!result){
            switch (gameElement.getGameLayer()){
                case CREATURE:{
                    unassignedCaveCreatures.add(gameElement);
                    unassignedCreatures.treeDidChange();
                    unassignedCreatures.updateUI();
                }
                break;
                case TREASURE:{
                    unassignedCaveTreasures.add(gameElement);
                    unassignedTreasures.treeDidChange();
                    unassignedTreasures.updateUI();
                }
                break;
                case ARTIFACT:{
                    unassignedCaveArtifacts.add(gameElement);
                    unassignedArtifacts.treeDidChange();
                    unassignedArtifacts.updateUI();
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
        caveOutput += "// Contains: "+this.getChildCount()+" part"+(this.getChildCount()>1?"ies":"y")+"\n";
        return caveOutput;
    }


}
