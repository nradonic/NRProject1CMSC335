package DataFileInput;
/**
 *  LoadGameData.java
 *  10/31/14
 * Created by Nick Radonic
 * File chooser popup, data import filtering
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

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Cave Game Data Files", "txt");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("."));
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
                int x = 0;
                temp = st;
                x = st.length();
            }
            if (s.trim().startsWith("p")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    Party party = new Party( ID, attributes[2]);
                    cave.addGameElement(party);
                } catch (Exception ex) {System.out.println("Party failed to parse data: "+s);}
            }

            if (s.trim().startsWith("c")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    int PID = Integer.parseInt(attributes[4].trim());
                    double empathy = Double.parseDouble(attributes[5].trim());
                    double fear = Double.parseDouble(attributes[6].trim());
                    double carryingCapacity = Double.parseDouble(attributes[7].trim());

                    int age = attributes.length>8 ? Integer.parseInt(attributes[8].trim()) : 0;
                    double height = attributes.length>9 ? Double.parseDouble(attributes[9].trim()) : 0;
                    double weight = attributes.length>10 ? Double.parseDouble(attributes[10].trim()) : 0;

                    Creature creature = new Creature(ID, attributes[2], attributes[3], PID, empathy, fear, carryingCapacity, age, height, weight);
                    cave.addGameElement(creature);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }

            if (s.trim().startsWith("t")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    String treasureType = attributes[2].trim();
                    int creatureID = Integer.parseInt(attributes[3].trim());
                    double weight = Double.parseDouble(attributes[4].trim());
                    double value = Double.parseDouble(attributes[5].trim());
                    Treasure treasure = new Treasure(ID, treasureType, creatureID, weight, value);
                    cave.addGameElement(treasure);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }

            if (s.trim().startsWith("a")) {
                try {
                    int ID = Integer.parseInt(attributes[1].trim());
                    String artifactType = attributes[2].trim();
                    int creatureID = Integer.parseInt(attributes[3].trim());
                    String name = (attributes.length <5) ? " " : attributes[4]+" ";
                    Artifact artifact = new Artifact(ID, artifactType, creatureID, name);
                    cave.addGameElement(artifact);
                } catch (Exception ex) {System.out.println("Creature failed to parse data: "+s);}
            }
        }
     }
}
