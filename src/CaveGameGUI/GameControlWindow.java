package CaveGameGUI;
/**
 * CaveGameGUI.java
 * Oct. 30, 2014
 * Nick Radonic
 * GUI design file for Cave Gave, main window and search popup
 */
import DataFileInput.LoadGameData;
import DataTree.Cave;
import javafx.geometry.VerticalDirection;

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
    Cave cave;
    JTree tree;

    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;
    String lastStr = "text to search for...";

    public GameControlWindow (Cave cave) {
        this.cave = cave;
        this.tree = new JTree(cave);

        System.out.println("In constructor");
        setTitle("Sorcerer's Cave");
        setSize(900, 900);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel jp0 = new JPanel();
        jp0.setLayout(new BoxLayout(jp0, BoxLayout.Y_AXIS));


        JPanel jp1 = new JPanel ();
        jp1.add(setupButtons());
        jp0.add(jp1);

        JScrollPane jsp2 = new JScrollPane (tree);
        jp0.add(jsp2);

        jta.setText(cave.getName()+"\nUse READ button to get game file");
        JScrollPane jsp3 = new JScrollPane (jta);

        jp0.add(jsp3);

        //add(cave.unassignedCreatures, BorderLayout.CENTER);
        //add(cave.unassignedTreasures, BorderLayout.CENTER);
        //add(cave.unassignedArtifacts, BorderLayout.CENTER);
        getContentPane().add(jp0);
        setVisible (true);



    } // end no-parameter constructor

    public JPanel setupButtons() {
        JButton jbr = new JButton ("Read");
        JButton jbs = new JButton ("Search");
        JButton jba = new JButton ("Redraw");
        JButton jbm = new JButton("Move");

        JPanel jp = new JPanel ();
        jp.setLayout(new FlowLayout());

        jp.add (jbr);
        jp.add (jbs);
        jp.add (jba);
        jp.add (jbm);

        jbr.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                loadFileData();
//                String treeStr = cave.toString();
//                jta.setText(treeStr);
//                jta.setCaretPosition(0);
                tree.treeDidChange();
                tree.updateUI();

            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbs.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String searchGameResults = searchGame (cave);
                String treeStr = searchGameResults;
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jba.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String treeStr = cave.toString();
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbm.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String treeStr = cave.toString();
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class
        return jp;
    }

    private void loadFileData(){
        LoadGameData.LoadData(cave);
        //tree = new JTree(cave);

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
