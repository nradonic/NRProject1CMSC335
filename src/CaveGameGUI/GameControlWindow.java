package CaveGameGUI;
/**
 * CaveGameGUI.java
 * Oct. 30, 2014
 * Nick Radonic
 * GUI design file for Cave Gave, main window and search popup
 */
import DataFileInput.LoadGameData;
import DataTree.Cave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameControlWindow.java
 * Created by NickRadonic on 11/1/14.
 * with JFrame hints from Nicholas Duchon at http://sandsduchon.org/duchon/cs335/cave/strategy.html
 */
public class GameControlWindow extends JFrame{
    static final long serialVersionUID = 123L;
    JTextArea jta = new JTextArea ();
    Cave gameCave;

    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;
    String lastStr = "text to search for...";

    public GameControlWindow (Cave cave) {
        gameCave = cave;

        System.out.println ("In constructor");
        setTitle ("Sorcerer's Cave");
        setSize (500, 900);
        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible (true);

        JScrollPane jsp = new JScrollPane (jta);
        jta.setText("Empty Game:   "+cave.getName()+"\nUse READ button to get game file");
        add(jsp, BorderLayout.CENTER);

        setupButtons();

    } // end no-parameter constructor

    private void setupButtons() {
        JButton jbr = new JButton ("Read");
        JButton jbs = new JButton ("Search");
        JButton jba = new JButton ("Redraw");
        JButton jbm = new JButton("Move");

        JPanel jp = new JPanel ();
        jp.add (jbr);
        jp.add (jbs);
        jp.add (jba);
        jp.add (jbm);
        add(jp, BorderLayout.PAGE_START);
        validate ();

        jbr.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                loadFileData(gameCave);
                String treeStr = gameCave.toString();
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbs.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String searchGameResults = searchGame (gameCave);
                String treeStr = searchGameResults;
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jba.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String treeStr = gameCave.toString();
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbm.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String treeStr = gameCave.toString();
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class
    }

    private void loadFileData(Cave gameCave){
        LoadGameData.LoadData(gameCave);

        //System.out.println(gameCave.toString());
        //System.out.println(gameCave.sortTree());
    }

    private String searchGame(Cave cave){
        JCheckBox jcbParty = new JCheckBox("Party");
        jcbParty.setSelected(lastJCBP);

        JCheckBox jcbCreature = new JCheckBox("Creature");
        jcbCreature.setSelected(lastJCBC);

        JCheckBox jcbTreasure = new JCheckBox("Treasure");
        jcbTreasure.setSelected(lastJCBT);

        JCheckBox jcbArticle = new JCheckBox("Artifact");
        jcbArticle.setSelected(lastJCBA);

        JCheckBox[] jcbSearch = {jcbParty, jcbCreature, jcbTreasure, jcbArticle};


        String caveString = cave.toString();
        String[] gameLines = caveString.split("\n");
        String searchStr = JOptionPane.showInputDialog(jcbSearch, lastStr);
        lastStr = searchStr;
        lastJCBP = jcbParty.isSelected();
        lastJCBC = jcbCreature.isSelected();
        lastJCBT = jcbTreasure.isSelected();
        lastJCBA = jcbArticle.isSelected();

        String outputLines = "";
        for (String s: gameLines){
            if (s.toLowerCase().contains(searchStr) &&
                    ( (jcbArticle.isSelected() && s.trim().startsWith("a")) ||
                    (jcbParty.isSelected() && s.trim().startsWith("p")) ||
                    (jcbTreasure.isSelected() && s.trim().startsWith("t")) ||
                    (jcbCreature.isSelected() && s.trim().startsWith("c")) ) ) {
                outputLines += s+"\n";
            }
        }
        return outputLines;
    }

}

