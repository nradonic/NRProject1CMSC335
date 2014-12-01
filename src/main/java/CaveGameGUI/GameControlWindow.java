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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameControlWindow.java
 * Created by Nick Radonic on 11/1/14.
 * with JFrame hints from Nicholas Duchon at http://sandsduchon.org/duchon/cs335/cave/strategy.html
 */
public class GameControlWindow extends JFrame{
    static final long serialVersionUID = 123L;
    JPanel framepanelHolder = new JPanel();

    GameLayer filterDomain = GameLayer.PARTY;
    FilterField filterField = FilterField.ID;

    JTextArea jta = new JTextArea ();
    JScrollPane messagesScrollPane = new JScrollPane();

    Cave cave;
    JTree tree;
    JPanel sortPanel = new JPanel();

    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;
    boolean lastJCBJ = true;

    final Dimension WINDOWDIMENSIONS = new Dimension(860, 1000);

    String lastStr = "text to search for...";

    JPanel filterFieldJPanel = new JPanel();
    FilterField selectedButtonParty = FilterField.ID;
    FilterField selectedButtonCreature = FilterField.ID;
    FilterField selectedButtonTreasure = FilterField.ID;
    FilterField selectedButtonArtifact = FilterField.ID;
    FilterField selectedButtonJob = FilterField.ID;

    GameLayer searchLayer = GameLayer.NONE;
    String searchText = "";

    JScrollPane treeScrollPane = new JScrollPane();
    JFrame taskDisplay ;

    public GameControlWindow (Cave cave) {
        this.cave = cave;
        this.tree = new JTree(cave);

       // System.out.println("In constructor");
        setTitle("Sorcerer's Cave");
        setSize(WINDOWDIMENSIONS);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel framePanel = setupDisplayPanels(cave);

        getContentPane().add(framePanel);
        setVisible (true);



    } // end no-parameter constructor

    private JPanel setupDisplayPanels(Cave cave) {
        JPanel framePanel = new JPanel();
        framepanelHolder = framePanel;
        framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.Y_AXIS));


        JPanel buttonsScrollPanel = new JPanel ();
        buttonsScrollPanel.add(setupButtons());
        framePanel.add(buttonsScrollPanel);

        treeScrollPane = new JScrollPane (tree);
        treeScrollPane.setMinimumSize(new Dimension(500, 500));
        framePanel.add(treeScrollPane);

        jta.setText(cave.getName()+"\nUse READ button to get game file");
        jta.setRows(10);
        messagesScrollPane = new JScrollPane (jta);
        messagesScrollPane.setMinimumSize(new Dimension(100, 100));
        messagesScrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        framePanel.add(messagesScrollPane);

        JScrollPane unassignedCreaturesPane = new JScrollPane(new JTree(cave.unassignedCaveCreatures));
        JScrollPane unassignedTreasuresPane = new JScrollPane(new JTree(cave.unassignedCaveTreasuresArtifactsJobs));
//        JScrollPane unassignedArtifactsPane = new JScrollPane(new JTree(cave.unassignedCaveArtifacts));

        framePanel.add(unassignedCreaturesPane);
        framePanel.add(unassignedTreasuresPane);
//        framePanel.add(unassignedArtifactsPane);

        framePanel.add(sortGameLayerButtons());
        filterFieldJPanel = loadSortFieldButtons();
        framePanel.add(filterFieldJPanel);
        return framePanel;
    }

    public JPanel setupButtons() {
        JButton jbr = new JButton ("Read");
        JButton jbs = new JButton ("Search");
        JButton jbc = new JButton("Clear Data");

        JPanel jp = new JPanel ();
        jp.setLayout(new FlowLayout());

        jp.add (jbr);
        jp.add (jbs);
        jp.add (jbc);

        jbr.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               if( taskDisplay != null) {taskDisplay.dispose();}
                loadFileData();
                tree.treeDidChange();
                tree.updateUI();
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbs.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String tempStr = searchGame();
                jta.setText("Search: "+searchLayer.toString() + " : " + searchText+"\n"+tempStr);
                jta.setCaretPosition(0);
                messagesScrollPane.setBorder(BorderFactory.createTitledBorder("Search: "+searchLayer.toString() + " : " + searchText));

            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

        jbc.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(null,
                        "Clear Cave Data?","", JOptionPane.YES_NO_OPTION
                );
                if (n == 0 ){
                    cave.removeAllChildren();
                    cave.unassignedCaveCreatures.removeAllChildren();
                    cave.unassignedCaveTreasuresArtifactsJobs.removeAllChildren();
                    updateFiltering();
                }
            } // end required method
        });

        return jp;
    }

    private void loadFileData(){
        LoadGameData.LoadData(cave);
        taskDisplay = new JobTaskDisplay(cave);
    }

    private String searchGame(){
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createTitledBorder("Search"));
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        ButtonGroup bg = new ButtonGroup();

        JRadioButton jcbParty = new JRadioButton("Party");
        jcbParty.setSelected(lastJCBP);
        bg.add(jcbParty);

        JRadioButton jcbCreature = new JRadioButton("Creature");
        jcbCreature.setSelected(lastJCBC);
        bg.add(jcbCreature);

        JRadioButton jcbTreasure = new JRadioButton("Treasure");
        jcbTreasure.setSelected(lastJCBT);
        bg.add(jcbTreasure);

        JRadioButton jcbArticle = new JRadioButton("Artifact");
        jcbArticle.setSelected(lastJCBA);
        bg.add(jcbArticle);

        JRadioButton jcbJob = new JRadioButton("Job");
        jcbJob.setSelected(lastJCBJ);
        bg.add(jcbJob);

        JRadioButton[] jcbSearch = {jcbParty, jcbCreature, jcbTreasure, jcbArticle, jcbJob};


        String caveString = cave.toString();
        String[] gameLines = caveString.split("\n");
        String searchStr = JOptionPane.showInputDialog(jcbSearch, lastStr);

        lastStr = searchStr;
        lastJCBP = jcbParty.isSelected();
        lastJCBC = jcbCreature.isSelected();
        lastJCBT = jcbTreasure.isSelected();
        lastJCBA = jcbArticle.isSelected();
        lastJCBJ = jcbJob.isSelected();

        searchText = searchStr;
        if ( lastJCBP) {
            searchLayer = GameLayer.PARTY;
        }
        if ( lastJCBC) {
            searchLayer = GameLayer.CREATURE;
        }
        if ( lastJCBT) {
            searchLayer = GameLayer.TREASURE;
        }
        if ( lastJCBA) {
            searchLayer = GameLayer.ARTIFACT;
        }
        if ( lastJCBJ) {
            searchLayer = GameLayer.JOB;
        }
        return cave.treeList(searchLayer, searchText);
    }

    JPanel sortGameLayerButtons(){
        ButtonGroup bg = new ButtonGroup();

        JRadioButton jcbParty = new JRadioButton("Party");
        bg.add(jcbParty);

        JRadioButton jcbCreature = new JRadioButton("Creature");
        bg.add(jcbCreature);

        JRadioButton jcbTreasure = new JRadioButton("Treasure");
        bg.add(jcbTreasure);

        JRadioButton jcbArtifact = new JRadioButton("Artifact");
        bg.add(jcbArtifact);

        JRadioButton jcbJob = new JRadioButton("Job");
        bg.add(jcbJob);

        JPanel jcbFilter = new JPanel();
        jcbFilter.add(new JLabel("Sort Level:    "));
        jcbFilter.add(jcbParty);
        jcbFilter.add(jcbCreature);
        jcbFilter.add(jcbTreasure);
        jcbFilter.add(jcbArtifact);
        jcbFilter.add(jcbJob);



        jcbParty.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.PARTY;
                selectGameLayer(filterDomain);
            } // end required method
        } // end local definition of inner class
        ); //

        jcbCreature.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.CREATURE;
                selectGameLayer(filterDomain);
            } // end required method
        } // end local definition of inner class
        ); //
        jcbTreasure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterDomain = GameLayer.TREASURE;
                selectGameLayer(filterDomain);
            } // end required method
        } // end local definition of inner class
        ); //
        jcbArtifact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterDomain = GameLayer.ARTIFACT;
                selectGameLayer(filterDomain);
            } // end required method
        } // end local definition of inner class
        ); //
        jcbJob.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterDomain = GameLayer.JOB;
                selectGameLayer(filterDomain);
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
                jcbArtifact.setSelected(true);
                break;
            }
            case JOB:{
                jcbJob.setSelected(true);
                break;
            }
        }

        return jcbFilter;
    }

    private void selectGameLayer(GameLayer filterDomain) {
        this.filterDomain = filterDomain;
        framepanelHolder.remove(filterFieldJPanel);
        JPanel jp = loadSortFieldButtons();
        filterFieldJPanel = jp;
        framepanelHolder.add(jp);
        revalidate();
        updateFiltering();
    }


    JPanel loadSortFieldButtons(){
        ButtonGroup bg = new ButtonGroup();
        JPanel jp = new JPanel();
        jp.add(new Label("Sort Filters:  "));

        if(filterDomain == GameLayer.PARTY){
            generateRadioButtonFF(filterDomain, FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.NAME, "Name", jp, bg);

        } else if (filterDomain == GameLayer.CREATURE){
            generateRadioButtonFF(filterDomain, FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.CREATURETYPE, "Type", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.NAME, "Name", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.EMPATHY, "Empathy", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.FEAR, "Fear", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.CAPACITY, "Carrying capacity", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.AGE, "Age", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.HEIGHT, "Height", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.WEIGHT, "Weight", jp, bg);

        } else if(filterDomain == GameLayer.TREASURE){
            generateRadioButtonFF(filterDomain, FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.TREASURETYPE, "Type", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.WEIGHT, "Weight", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.VALUE, "Value", jp, bg);

        } else if (filterDomain == GameLayer.ARTIFACT){
            generateRadioButtonFF(filterDomain, FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.ARTIFACTTYPE, "Type", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.NAME, "Name", jp, bg);

        } else if (filterDomain == GameLayer.JOB){
            generateRadioButtonFF(filterDomain, FilterField.ID, "ID", jp, bg);
            generateRadioButtonFF(filterDomain, FilterField.JOBTYPE, "Type", jp, bg);
        }
        return jp;
    }


    private JPanel generateRadioButtonFF(GameLayer filterDomain, FilterField ff, String buttonLabel, JPanel jp, ButtonGroup bg){
        JRadioButton jcb = new JRadioButton(buttonLabel);
        bg.add(jcb);

        if ( filterDomain==GameLayer.PARTY && ff == selectedButtonParty ||
                filterDomain==GameLayer.CREATURE && ff == selectedButtonCreature ||
                filterDomain==GameLayer.TREASURE && ff == selectedButtonTreasure ||
                filterDomain==GameLayer.ARTIFACT && ff == selectedButtonArtifact ||
                filterDomain==GameLayer.JOB && ff == selectedButtonJob )
        {
            jcb.setSelected(true);
        }

        jp.add(jcb);

        jcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterField = ff;
                if(filterDomain == GameLayer.PARTY){
                    selectedButtonParty = ff;
                } else if (filterDomain == GameLayer.CREATURE){
                    selectedButtonCreature = ff;
                } else if (filterDomain == GameLayer.TREASURE){
                    selectedButtonTreasure = ff;
                } else if (filterDomain == GameLayer.ARTIFACT){
                    selectedButtonArtifact = ff;
                } else if (filterDomain == GameLayer.JOB){
                    selectedButtonJob = ff;
                }
                ((JRadioButton)e.getSource()).setSelected(true);
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
