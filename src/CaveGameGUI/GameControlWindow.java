package CaveGameGUI;
/**
 * CaveGameGUI.java
 * Oct. 30, 2014
 * Nick Radonic
 * GUI design file for Cave Gave, main window and search popup
 */
import DataFileInput.LoadGameData;
import DataTree.Cave;
import DataTree.FilterField;
import DataTree.GameLayer;
import javafx.scene.control.RadioButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GameControlWindow.java
 * Created by NickRadonic on 11/1/14.
 * with JFrame hints from Nicholas Duchon at http://sandsduchon.org/duchon/cs335/cave/strategy.html
 */
public class GameControlWindow extends JFrame{
    static final long serialVersionUID = 123L;
    JPanel framepanelHolder = new JPanel();

    GameLayer filterDomain = GameLayer.PARTY;
    FilterField filterField = FilterField.ID;

    JTextArea jta = new JTextArea ();
    Cave cave;
    JTree tree;
    JPanel sortPanel = new JPanel();
    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;


    String lastStr = "text to search for...";

    ArrayList<JRadioButton> partyFilterFields = new ArrayList<>();
    ArrayList<JRadioButton> creatureFilterFields = new ArrayList<>();
    ArrayList<JRadioButton> treasureFilterFields = new ArrayList<>();
    ArrayList<JRadioButton> artifactFilterFields = new ArrayList<>();
    JPanel filterFieldJPanel = new JPanel();

    public GameControlWindow (Cave cave) {
        this.cave = cave;
        this.tree = new JTree(cave);

        System.out.println("In constructor");
        setTitle("Sorcerer's Cave");
        setSize(900, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel framePanel = new JPanel();
        framepanelHolder = framePanel;
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

        framePanel.add(sortGameLayerButtons());
        filterFieldJPanel = loadSortFieldButtons();
        framePanel.add(filterFieldJPanel);

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
                String searchGameResults = searchGame(cave);
                String treeStr = searchGameResults;
                jta.setText(treeStr);
                jta.setCaretPosition(0);
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jba.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //JPanel jp = sortGameLayerButtons(cave);


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

    JPanel sortGameLayerButtons(){
        ButtonGroup bg = new ButtonGroup();

        JRadioButton jcbParty = new JRadioButton("Party");
        bg.add(jcbParty);

        JRadioButton jcbCreature = new JRadioButton("Creature");
        bg.add(jcbCreature);

        JRadioButton jcbTreasure = new JRadioButton("Treasure");
        bg.add(jcbTreasure);

        JRadioButton jcbArticle = new JRadioButton("Artifact");
        bg.add(jcbArticle);

        JPanel jcbFilter = new JPanel();
        jcbFilter.add(new JLabel("Search domain:    "));
        jcbFilter.add(jcbParty);
        jcbFilter.add(jcbCreature);
        jcbFilter.add(jcbTreasure);
        jcbFilter.add(jcbArticle);


        jcbParty.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.PARTY;
                framepanelHolder.remove(filterFieldJPanel);
                JPanel jp = loadSortFieldButtons();
                filterFieldJPanel = jp;
                framepanelHolder.add(jp);
                revalidate();
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //

        jcbCreature.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.CREATURE;
                framepanelHolder.remove(filterFieldJPanel);
                JPanel jp = loadSortFieldButtons();
                filterFieldJPanel = jp;
                framepanelHolder.add(jp);
                revalidate();
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //
        jcbTreasure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterDomain = GameLayer.TREASURE;
                framepanelHolder.remove(filterFieldJPanel);
                JPanel jp = loadSortFieldButtons();
                filterFieldJPanel = jp;
                framepanelHolder.add(jp);
                revalidate();
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //
        jcbArticle.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.ARTIFACT;
                framepanelHolder.remove(filterFieldJPanel);
                JPanel jp = loadSortFieldButtons();
                filterFieldJPanel = jp;
                framepanelHolder.add(jp);
                revalidate();
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        ); //

        switch(filterDomain){
            case PARTY:{
                jcbParty.setSelected(true);
                break;
            }
            case CREATURE:{
                jcbCreature.setSelected(true);
                break;
            }
            case TREASURE:{
                jcbTreasure.setSelected(true);
                break;
            }
            case ARTIFACT:{
                jcbArticle.setSelected(true);
                break;
            }
        }

        return jcbFilter;
    }


    JPanel loadSortFieldButtons(){
        ButtonGroup bg = new ButtonGroup();
        JPanel jp = new JPanel();
        jp.add(new Label("Filters:  "));

        if(filterDomain == GameLayer.PARTY){
            generateRadioButtonFF(FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(FilterField.NAME, "Name", jp, bg);
        } else if (filterDomain == GameLayer.CREATURE){
            generateRadioButtonFF(FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(FilterField.CREATURETYPE, "Type", jp, bg);
            generateRadioButtonFF(FilterField.NAME, "Name", jp, bg);
            generateRadioButtonFF(FilterField.PARTYID, "Party", jp, bg);
            generateRadioButtonFF(FilterField.EMPATHY, "Empathy", jp, bg);
            generateRadioButtonFF(FilterField.FEAR, "Fear", jp, bg);
            generateRadioButtonFF(FilterField.CAPACITY, "Carrying capacity", jp, bg);
            generateRadioButtonFF(FilterField.AGE, "Age", jp, bg);
            generateRadioButtonFF(FilterField.HEIGHT, "Height", jp, bg);
            generateRadioButtonFF(FilterField.WEIGHT, "Weight", jp, bg);
        } else if(filterDomain == GameLayer.TREASURE){
            generateRadioButtonFF(FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(FilterField.TREASURETYPE, "Type", jp, bg);
            generateRadioButtonFF(FilterField.CREATUREID, "Creature", jp, bg);
            generateRadioButtonFF(FilterField.WEIGHT, "Weight", jp, bg);
            generateRadioButtonFF(FilterField.VALUE, "Value", jp, bg);
        } else if (filterDomain == GameLayer.ARTIFACT){
            generateRadioButtonFF(FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(FilterField.ARTIFACTTYPE, "Type", jp, bg);
            generateRadioButtonFF(FilterField.CREATUREID, "Creature", jp, bg);
            generateRadioButtonFF(FilterField.NAME, "Name", jp, bg);
        }
        return jp;
    }


    private JPanel generateRadioButtonFF(FilterField ff, String buttonLabel, JPanel jp, ButtonGroup bg){
        JRadioButton jcb = new JRadioButton(buttonLabel);
        bg.add(jcb);

        jp.add(jcb);

        jcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterField = ff;
                updateFiltering();
            } // end required method
        } // end local definition of inner class
        );
        return jp;
    }

    void updateFiltering(){
        cave.modifyDisplayJTree(filterDomain, filterField);
        tree.updateUI();
    }
}
