package CaveGameGUI;
/**
 * CaveGameGUI.java
 * Oct. 30, 2014
 * Nick Radonic
 * GUI design file for Cave Gave, main window and search popup
 */
import DataFileInput.LoadGameData;
import DataTree.Cave;
import DataTree.GameLayer;
import DataTree.Treasure;
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
    JPanel sortPanel = new JPanel();
    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;
    GameLayer layerOfInterest = GameLayer.NONE;
    GameLayer filterDomain = GameLayer.NONE;

    String lastStr = "text to search for...";

    public GameControlWindow (Cave cave) {
        this.cave = cave;
        this.tree = new JTree(cave);

        System.out.println("In constructor");
        setTitle("Sorcerer's Cave");
        setSize(900, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel framePanel = new JPanel();
        framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.Y_AXIS));


        JPanel buttonsScrollPanel = new JPanel ();
        buttonsScrollPanel.add(setupButtons());
        framePanel.add(buttonsScrollPanel);

        JScrollPane treeScrollPane = new JScrollPane (tree);
        framePanel.add(treeScrollPane);

        jta.setText(cave.getName()+"\nUse READ button to get game file");
        jta.setRows(10);
        JScrollPane messagesScrollPane = new JScrollPane (jta);

        framePanel.add(messagesScrollPane);

        JScrollPane unassignedCreaturesPane = new JScrollPane(new JTree(cave.unassignedCaveCreatures));
        JScrollPane unassignedTreasuresPane = new JScrollPane(new JTree(cave.unassignedCaveTreasures));
        JScrollPane unassignedArtifactsPane = new JScrollPane(new JTree(cave.unassignedCaveArtifacts));

        framePanel.add(unassignedCreaturesPane);
        framePanel.add(unassignedTreasuresPane);
        framePanel.add(unassignedArtifactsPane);
        framePanel.add(sortGame());

        getContentPane().add(framePanel);
        setVisible (true);



    } // end no-parameter constructor

    public JPanel setupButtons() {
        JButton jbr = new JButton ("Read");
        JButton jbs = new JButton ("Search");
        JButton jba = new JButton ("Sort");
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
                //JPanel jp = sortGame(cave);


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

    JPanel sortGame(){
        ButtonGroup bg = new ButtonGroup();

        JRadioButton jcbParty = new JRadioButton("Party");
        bg.add(jcbParty);

        JRadioButton jcbCreature = new JRadioButton("Creature");
        bg.add(jcbCreature);

        JRadioButton jcbTreasure = new JRadioButton("Treasure");
        bg.add(jcbTreasure);

        JRadioButton jcbArticle = new JRadioButton("Artifact");
        bg.add(jcbArticle);

        JRadioButton jcbNone = new JRadioButton("None");
        jcbNone.setSelected(true);
        bg.add(jcbNone);

        JPanel jcbFilter = new JPanel();
        jcbFilter.add(new JLabel("Search domain:    "));
        jcbFilter.add(jcbParty);
        jcbFilter.add(jcbCreature);
        jcbFilter.add(jcbTreasure);
        jcbFilter.add(jcbArticle);
        jcbFilter.add(jcbNone);


        jcbParty.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.PARTY;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //

        jcbCreature.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.CREATURE;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //
        jcbTreasure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterDomain = GameLayer.TREASURE;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //
        jcbArticle.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.ARTIFACT;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //
        jcbNone.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.NONE;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //


        return jcbFilter;
    }


    void updateFiltering(){
        cave.createMaps(filterDomain);

    }
}
