// Cave.java
// Oct. 30, 2014
// Nick Radonic
// top level cave structure in Project 1 game

package DataTree;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Cave {
    private ArrayList<Party> parties = new ArrayList<Party>();
    private String name;
    private ArrayList<Creature> unassignedCreatures = new ArrayList<Creature>();
    private ArrayList<Treasure> unassignedTreasures = new ArrayList<Treasure>();
    private ArrayList<Artifact> unassignedArtifacts = new ArrayList<Artifact>();

    public Cave(String name){
        this.name = name;
    }

    public String addParty(Party party){
        parties.add(party);
        return party.getName() + " " + party.getPartyID();
    }

    public Party getParty(int partyID){
        Party returnParty = null;
        for (Party p : parties){
            if (partyID == p.getPartyID())
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
    }

    public String getName(){
        return name;
    }

    public String toString(){
        String caveOutput = "// Cave: "+name+"\n";
        caveOutput += "// Contains: "+parties.size()+" parties\n";

        for (Party p : parties){
            caveOutput += p.toString();
        }
        if (unassignedCreatures.size()>0){
            caveOutput += "   // Contains: "+unassignedCreatures.size()+" unassigned creatures\n";
        }
        for (Creature c : unassignedCreatures){
            caveOutput += c.toString();
        }
        if (unassignedTreasures.size()>0) {
            caveOutput += "        // Contains: " + unassignedTreasures.size() + " unassigned treasures\n";
        }
        for (Treasure t : unassignedTreasures){
            caveOutput += t.toString();
        }
        if (unassignedArtifacts.size()>0) {
            caveOutput += "        // Contains: " + unassignedArtifacts.size() + " unassigned artifacts\n";
        }
        for (Artifact art : unassignedArtifacts){
            caveOutput += art.toString();
        }

        return caveOutput;
    }

    public void addCreature (Creature creature){
        Optional<Party> party;
        if (creature.getPartyID() != 0) {
            party = parties.stream().filter(s -> s.getPartyID() == creature.getPartyID()).findAny();
            if (!party.isPresent()) { addCreatureToUnassignedList(creature);}
            else { party.get().addCreature(creature);}
        }
        else
        {
            addCreatureToUnassignedList(creature);
        }
    }

    private void addCreatureToUnassignedList(Creature creature) {
        creature.setPartyID(0);
        unassignedCreatures.add(creature);
    }

    public void addTreasure(Treasure treasure){
        Creature tempCreature = null;
        for (Party party : parties){
            for (Creature creature: party.creatures){
                if (treasure.creatureID == creature.ID){
                    tempCreature = creature;
                    break;
                }
            }
        }
        if (tempCreature == null){
            for (Creature creature: unassignedCreatures){
                if (treasure.creatureID == creature.ID){
                    tempCreature = creature;
                    break;
                }
            }
        }
        
        if (tempCreature != null) {
            tempCreature.addTreasure(treasure);
        }
        else {
            unassignedTreasures.add(treasure);
        }
    }

    public void addArtifact(Artifact artifact){
        Creature tempCreature = null;
        for (Party party : parties){
            for (Creature creature: party.creatures){
                if (artifact.creatureID == creature.ID){
                    tempCreature = creature;
                    break;
                }
            }
        }

        if (tempCreature == null){
            for (Creature creature: unassignedCreatures){
                if (artifact.creatureID == creature.ID){
                    tempCreature = creature;
                    break;
                }
            }
        }

        if (tempCreature != null) {
            tempCreature.addArtifact(artifact);
        }
        else {
            unassignedArtifacts.add(artifact);
        }
    }

    public String sortTree(){
        String treeString = toString();
        String outputString = "// Data file for CMSC 335, Fall 2014\n// Nick Radonic\n";

        LocalDateTime timePoint = LocalDateTime.now();
        outputString += "// "+ timePoint.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).toString();

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
}
