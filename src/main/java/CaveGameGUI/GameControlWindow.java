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
import DataTree.GameElement;
import DataTree.GameLayer;

import javax.swing.*;
import javax.swing.border.Border;
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
    Cave cave;
    JTree tree;
    JPanel sortPanel = new JPanel();
    // store previous search terms
    boolean lastJCBP = true;
    boolean lastJCBC = true;
    boolean lastJCBT = true;
    boolean lastJCBA = true;
    final Dimension WINDOWDIMENSIONS = new Dimension(800, 1000);

    String lastStr = "text to search for...";

    JPanel filterFieldJPanel = new JPanel();
    FilterField selectedButtonParty = FilterField.ID;
    FilterField selectedButtonCreature = FilterField.ID;
    FilterField selectedButtonTreasure = FilterField.ID;
    FilterField selectedButtonArtifact = FilterField.ID;

    GameLayer searchLayer = GameLayer.NONE;
    String searchText = "";

    JScrollPane treeScrollPane = new JScrollPane();

    public GameControlWindow (Cave cave) {
        this.cave = cave;
        this.tree = new JTree(cave);

        System.out.println("In constructor");
        setTitle("Sorcerer's Cave");
        setSize(WINDOWDIMENSIONS);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        JButton jbm = new JButton("Redraw");

        JPanel jp = new JPanel ();
        jp.setLayout(new FlowLayout());

        jp.add (jbr);
        jp.add (jbs);
        jp.add (jbm);

        jbr.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
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
            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class

       jbm.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                searchLayer = GameLayer.NONE;
                searchText = "";

            } // end required method
        } // end local definition of inner class
        ); // the anonymous inner class
        return jp;
    }

    private void loadFileData(){
        LoadGameData.LoadData(cave);
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

        JRadioButton[] jcbSearch = {jcbParty, jcbCreature, jcbTreasure, jcbArticle};


        String caveString = cave.toString();
        String[] gameLines = caveString.split("\n");
        String searchStr = JOptionPane.showInputDialog(jcbSearch, lastStr);

        lastStr = searchStr;
        lastJCBP = jcbParty.isSelected();
        lastJCBC = jcbCreature.isSelected();
        lastJCBT = jcbTreasure.isSelected();
        lastJCBA = jcbArticle.isSelected();

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

        JRadioButton jcbArticle = new JRadioButton("Artifact");
        bg.add(jcbArticle);

        JPanel jcbFilter = new JPanel();
        jcbFilter.add(new JLabel("Sort Level:    "));
        jcbFilter.add(jcbParty);
        jcbFilter.add(jcbCreature);
        jcbFilter.add(jcbTreasure);
        jcbFilter.add(jcbArticle);


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
        jcbArticle.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                filterDomain = GameLayer.ARTIFACT;
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
                jcbArticle.setSelected(true);
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
        }
        return jp;
    }


    private JPanel generateRadioButtonFF(GameLayer filterDomain, FilterField ff, String buttonLabel, JPanel jp, ButtonGroup bg){
        JRadioButton jcb = new JRadioButton(buttonLabel);
        bg.add(jcb);

        if ( filterDomain==GameLayer.PARTY && ff == selectedButtonParty ||
                filterDomain==GameLayer.CREATURE && ff == selectedButtonCreature ||
                filterDomain==GameLayer.TREASURE && ff == selectedButtonTreasure ||
                filterDomain==GameLayer.ARTIFACT && ff == selectedButtonArtifact )
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
