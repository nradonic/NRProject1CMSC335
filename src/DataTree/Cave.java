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

//    private ArrayList<GameElement> unassignedCreatures = new ArrayList<>();
//    private ArrayList<GameElement> unassignedTreasures = new ArrayList<>();
//    private ArrayList<GameElement> unassignedArtifacts = new ArrayList<>();

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

 /*   public Party getParty(int partyID){
        Party returnParty = null;
        for (GameElement p : gameElementArrayList){
            if (partyID == p.getID())
            {
                returnParty = p;
                break;
            }
        }
        return returnParty;
    }

    public Party getParty(String partyName){
        Party returnParty = null;
        for (Party p : parties){
            if (partyName.equals(p.getName()))
            {
                returnParty = p;
                break;
            }
        }
        return returnParty;
    }*/

    public String getName(){
        return name;
    }

    public String toString(){
        String caveOutput = "// Cave: "+name+"\n\n";
        caveOutput += "// Contains: "+this.getChildCount()+" part"+(this.getChildCount()>1?"ies":"y")+"\n";
/*
        for (GameElement p : gameElementArrayList){
            caveOutput += "\n";
            caveOutput += p.toString();
        }*/
//         caveOutput += "// Contains: "+this.getChildCount()+" part"+(this.getChildCount() > 1?"ies":"y")+"\n";
//
//
//        if (unassignedCaveCreatures.getChildCount()>0){
//            caveOutput += "\n\n// Unassigned Creatures\n";
//            caveOutput += "   // Contains: "+unassignedCaveCreatures.getChildCount()+" unassigned creature"+
//                    (unassignedCaveCreatures.getChildCount()>1?"s":"")+"\n";
//        }
//        for (GameElement c : unassignedCreatures){
//            caveOutput += c.toString()+"\n";
//        }
//        caveOutput += "\n\n  // Unassigned Treasure and Artifacts\n";
//        if (unassignedTreasures.size()>0) {
//            caveOutput += "        // Contains: " + unassignedTreasures.size() + " unassigned treasure"+(unassignedTreasures.size()>1?"s":"")+"\n";
//        }
//        for (GameElement t : unassignedTreasures){
//            caveOutput += t.toString();
//        }
//        if (unassignedArtifacts.size()>0) {
//            caveOutput += "        // Contains: " + unassignedArtifacts.size() + " unassigned artifact"+(unassignedArtifacts.size()>1?"s":"")+"\n";
//        }
//        for (GameElement art : unassignedArtifacts){
//            caveOutput += art.toString();
//        }

        return caveOutput;
    }

    public String sortTree(){
        String treeString = toString();
        String outputString = "// Data file for CMSC 335, Fall 2014\n// Nick Radonic\n";

        LocalDateTime timePoint = LocalDateTime.now();
        outputString += "// "+ timePoint.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

        outputString += getPartyDescriptions(treeString);
        outputString += getCreatureDescriptions(treeString);
        outputString += getTreasureDescriptions(treeString);
        outputString += getArtifactDescriptions(treeString);
        return outputString;
    }

    private String getPartyDescriptions(String treeString){
        String outputString = "// Parties format :\n";
        outputString += "//  p:<index>:<name>\n";
        ArrayList<String> treeLines = new ArrayList(Arrays.asList(treeString.split("\n")));
        for (String s: treeLines){
            String t = s.trim();
            if(t.startsWith("p :")) {
                outputString += t + "\n" ;
            }
        }
        return outputString;
    }

    private String getCreatureDescriptions(String treeString){
        String outputString = "// Creatures format :\n";
        outputString += "//    c:<index>:<type>:<name>:<party>:<empathy>:<fear>:<carrying capacity>\n";
        ArrayList<String> treeLines = new ArrayList(Arrays.asList(treeString.split("\n")));
        for (String s: treeLines){
            String t = s.trim();
            if(t.startsWith("c :")) {
                outputString += t + "\n" ;
            }
        }
        return outputString;
    }

    private String getTreasureDescriptions(String treeString){
        String outputString = "// Treasure format :\n";
        outputString += "//    t:<index>:<type>:<creature>:<weight>:<value>\n";
        ArrayList<String> treeLines = new ArrayList(Arrays.asList(treeString.split("\n")));
        for (String s: treeLines){
            String t = s.trim();
            if(t.startsWith("t :")) {
                outputString += t + "\n" ;
            }
        }
        return outputString;
    }

    private String getArtifactDescriptions(String treeString){
        String outputString = "// Artifact format :\n";
        outputString += "//    a:<index>:<type>:<creature>[:<name>]\n";
        ArrayList<String> treeLines = new ArrayList(Arrays.asList(treeString.split("\n")));
        for (String s: treeLines){
            String t = s.trim();
            if(t.startsWith("a :")) {
                outputString += t + "\n" ;
            }
        }
        return outputString;
    }

    private void addUnassigned(ArrayList<GameElement> age, GameElement gameElement){
        Optional<GameElement> oge = age.stream().filter(p->p.ID == gameElement.ID).findFirst();
        if(oge.isPresent()){
            GameElement tempGE = oge.get();
            tempGE = gameElement;
        } else {
            age.add(gameElement);
        }
    }
}
