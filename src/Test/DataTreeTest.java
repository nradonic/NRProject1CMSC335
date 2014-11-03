/**
 * DataTreeTest.java
 * Oct. 30, 2014
 * Nick Radonic
 * Cave game data structure mechanical creation test in Project 1, Cave Game
 */

package Test;

import DataTree.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

public class DataTreeTest {

    public static void main(String[] args){

        //Test1AddComponents();

        GetDataFromFile();
    }

    private static void GetDataFromFile() {
        final String PCOLON = "p:";
        final String CCOLON = "c:";
        final String ACOLON = "a:";
        final String TCOLON = "t:";
        Cave dataTree = new Cave("Game Zone");


        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Cave Game Data Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
        }
        File file = chooser.getSelectedFile();
        ArrayList<String> gameData = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String s;
            while ((s=br.readLine())!=null){
                String t = s.trim();
                String u = t.replace(" ","");
                if (u.startsWith(CCOLON) || u.startsWith(PCOLON) || u.startsWith(TCOLON) || u.startsWith(ACOLON)){
                    gameData.add(t);
                }
            }

            br.close();
            fr.close();
        }
        catch (IOException ex)
        {System.out.println("File opening error "+chooser.getSelectedFile().getName()+" "+ex.toString()); }
        System.out.println("Data file dump:");

        for (String ss: gameData){
            String s = ss.trim();
            String[] attributes = s.split(":");
            String temp = "";
            for (String st : attributes){
                temp = st;
                int x = temp.length();
            }
            if (s.startsWith("p")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    Party party = new Party( ID, attributes[2]);
                    dataTree.addGameElement(party);
                } catch (Exception ex) {System.out.println("Party failed to parse data: "+s);}
            }

            if (s.startsWith("c")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    int PID = Integer.parseInt(attributes[4].trim());
                    double empathy = Double.parseDouble(attributes[5].trim());
                    double fear = Double.parseDouble(attributes[6].trim());
                    double carryingCapacity = Double.parseDouble(attributes[7].trim());
                    Creature creature = new Creature(ID, attributes[2], attributes[3], PID, empathy, fear, carryingCapacity);
                    dataTree.addGameElement(creature);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }

            if (s.trim().startsWith("t")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    int creatureID = Integer.parseInt(attributes[3].trim());
                    double weight = Double.parseDouble(attributes[4].trim());
                    double value = Double.parseDouble(attributes[5].trim());
                    Treasure treasure = new Treasure(ID, attributes[2], creatureID, weight, value);
                    dataTree.addGameElement(treasure);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }

            if (s.trim().startsWith("a")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    int creatureID = Integer.parseInt(attributes[3].trim());
                    String name = (attributes.length <5) ? "" : attributes[4];
                    Artifact artifact = new Artifact(ID, attributes[2], creatureID, name);
                    dataTree.addGameElement(artifact);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }
        }
        System.out.println(dataTree.toString());
        System.out.println(dataTree.sortTree());

    }

    private static void Test1AddComponents() {
        Cave dataTree = new Cave("Game Zone");

        for(int parties=1; parties<3; parties++){
                Party party = new Party("Red Hot Raiders "+parties);
            for(int cre=1; cre < 4; cre++) {
                Creature creature = new Creature("Freed"+cre, "Frank N. Stein", 15.5, 22.9, 44);

                for (int stuff = 1; stuff <= 5; stuff++) {

                    Treasure tr = new Treasure( "Gold "+stuff, 10.1, 3141.59);
                    Artifact art = new Artifact( "Picasso "+stuff, "very artsy");

                    creature.addTreasure(tr);
                    creature.addArtifact(art);
                }
                party.addGameElementTree(creature);
            }
            dataTree.addGameElement(party);
        }


        String output = dataTree.toString();
        System.out.println(output);

        output = dataTree.sortTree();
        System.out.println(output);
    }
}
