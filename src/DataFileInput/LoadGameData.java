package DataFileInput;
/**
 *  LoadGameData.java
 * Created by Nick Radonic on 10/31/14.
 */

import DataTree.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadGameData {
    public static void LoadData(Cave cave){
        final String PCOLON = "p:";
        final String CCOLON = "c:";
        final String ACOLON = "a:";
        final String TCOLON = "t:";

        Cave dataTree = cave;

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Cave Game Data Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
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

        for (String ss: gameData){
            String s = ss.trim();
            String[] attributes = s.split(":");
            String temp = "";
            int regex = attributes.length;
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
                    String name = (attributes.length <5) ? " " : attributes[4]+" ";
                    Artifact artifact = new Artifact(ID, attributes[2], creatureID, name);
                    dataTree.addGameElement(artifact);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }
        }
     }
}
