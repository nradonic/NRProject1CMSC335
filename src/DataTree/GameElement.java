package DataTree;
/**
 * GameElement.java
 * @since Oct. 30, 2014
 * @author Nick Radonic
 * Base game element, used for game element inheritance in Project 1, Cave Game
 */
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * GameElement.java
 * Base class to allow tree recursion on game element layers
 * @author NickRadonic
 * @since 11/2/14.
 */

public class GameElement extends DefaultMutableTreeNode implements Comparable<GameElement> {
    public static FilterField treeMapFF = FilterField.ID;
    protected GameLayer gameLayer = GameLayer.NONE;
    protected Integer ID = 0;

    private FilterField filterField = FilterField.ID;
    private GameLayer filterDomain = GameLayer.PARTY;
    private FilterField currentFilterField = FilterField.ID;

    TreeMap<GameElement, Integer> tmID = new TreeMap<>();
    TreeMap<GameElement, String> tmName = new TreeMap<>();

    TreeMap<GameElement, String> tmType = new TreeMap<>();
    TreeMap<GameElement, Integer> tmPartyID = new TreeMap<>();

    TreeMap<GameElement, Double> tmHeight = new TreeMap<>();
    TreeMap<GameElement, Integer> tmAge = new TreeMap<>();

    TreeMap<GameElement, Double> tmEmpathy = new TreeMap<>();
    TreeMap<GameElement, Double> tmFear = new TreeMap<>();
    TreeMap<GameElement, Double> tmCapacity = new TreeMap<>();

    TreeMap<GameElement, Integer> tmCreatureID = new TreeMap<>();
    TreeMap<GameElement, Double> tmValue = new TreeMap<>();
    TreeMap<GameElement, Double> tmWeight = new TreeMap<>();

    public GameElement() {
    }

    public GameElement(GameLayer layer, int ID) {
        gameLayer = layer;
        this.ID = ID;

    }

    public GameLayer getGameLayer() {
        return gameLayer;
    }

    public int getID() {
        return ID;
    }

    public boolean addGameElementTree(GameElement gameElement) {
        boolean result = false;
        switch (gameLayer) {
            case CAVE: {
                if (gameElement.gameLayer == GameLayer.PARTY) {
                    result = insertIfDuplicateGameElement(gameElement);
                    if (!result) {
                        //gameElementArrayList.add(gameElement);
                        this.add(gameElement);
                    }
                    result = true;
                }
                if (gameElement.gameLayer == GameLayer.CREATURE || gameElement.gameLayer == GameLayer.TREASURE ||
                        gameElement.gameLayer == GameLayer.ARTIFACT) {
                    result = recursiveInsertion(gameElement);
                }
            }
            break;
            case PARTY: {
                if (gameElement.gameLayer == GameLayer.CREATURE) {
                    if (ID == ((Creature) gameElement).partyID) {
                        result = insertIfDuplicateGameElement(gameElement);
                        if (!result) {
                            //gameElementArrayList.add(gameElement);
                            this.add(gameElement);
                            result = true;
                        }
                    }
                } else {
                    if (gameElement.gameLayer == GameLayer.TREASURE || gameElement.gameLayer == GameLayer.ARTIFACT) {
                        result = recursiveInsertion(gameElement);
                    }
                }
            }
            break;
            case CREATURE: {
                if (gameElement.gameLayer == GameLayer.TREASURE) {
                    if (ID == ((Treasure) gameElement).creatureID) {
                        result = insertIfDuplicateGameElement(gameElement);
                        if (!result) {
                            //gameElementArrayList.add(gameElement);
                            this.add(gameElement);
                            result = true;
                        }
                    }
                }
                if (gameElement.gameLayer == GameLayer.ARTIFACT) {
                    if (ID == ((Artifact) gameElement).creatureID) {
                        result = insertIfDuplicateGameElement(gameElement);
                        if (!result) {
                            //gameElementArrayList.add(gameElement);
                            this.add(gameElement);
                            result = true;
                        }
                    }
                }
            }
            break;
            case NONE:
                break;
        }
        return result;
    }

    private boolean insertIfDuplicateGameElement(GameElement gameElement) {
        boolean result = false;
        Vector<GameElement> ge = this.children;
        if (ge != null) {
            for (GameElement g : ge) {
                if (g.getID() == gameElement.getID()) {
                    if (g.gameLayer.ordinal() == gameElement.gameLayer.ordinal()) {
                        int e = g.ID;
                        this.remove(g);
                        this.add(gameElement);
                        //tempGE = gameElement;
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private boolean recursiveInsertion(GameElement gameElement) {
        boolean result = false;
        for (GameElement g : (Vector<GameElement>) this.children) {
            result = g.addGameElementTree(gameElement);
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String output = "";
        for (GameElement gameElement : (Vector<GameElement>) this.children) {
            output += gameElement.toString();
        }
        //return output;
        return "";
    }

    public void modifyDisplayJTree(GameLayer filterDomain, FilterField filterField) {
        this.filterDomain = filterDomain;
        this.filterField = filterField;

        if(gameLayer==GameLayer.CAVE && filterDomain == GameLayer.PARTY ||
                gameLayer==GameLayer.PARTY && filterDomain == GameLayer.CREATURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.TREASURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.ARTIFACT
                ) {
            currentFilterField = filterField;
            NavigableSet<GameElement> geSet = tmID.navigableKeySet();

            if (gameLayer == GameLayer.CAVE) {
                geSet = getGameElementsSetForCave();
            } else if (gameLayer == GameLayer.PARTY) {
                geSet = getGameElementsSetForParty(geSet);
            } else if (gameLayer == GameLayer.CREATURE) {
                geSet = getGameElementsSetForCreature(geSet);
            }

            Iterator<GameElement> ge = geSet.iterator();
            while (ge.hasNext()) {
                GameElement geItem = ge.next();
                remove(geItem);
                add(geItem);
            }

            recursivelyUpdateDisplayJTree();
        }

    }

    private void recursivelyUpdateDisplayJTree() {
        Enumeration<GameElement> gameElementEnumeration = this.children();
        while (gameElementEnumeration.hasMoreElements()) {
            GameElement geDisplay = gameElementEnumeration.nextElement();
            geDisplay.modifyDisplayJTree(filterDomain, filterField);
        }
    }

    private NavigableSet<GameElement> getGameElementsSetForCave() {
        NavigableSet<GameElement> geSet;
        if (currentFilterField == FilterField.ID) {
            treeMapFF = FilterField.ID;
            geSet = tmID.navigableKeySet();
        } else {
            treeMapFF = FilterField.NAME;
            geSet = tmName.navigableKeySet();
        }
        return geSet;
    }

    private NavigableSet<GameElement> getGameElementsSetForCreature(NavigableSet<GameElement> geSet) {
        switch (currentFilterField) {
            case ID: {
                treeMapFF = FilterField.ID;
                geSet = tmID.navigableKeySet();
                break;
            }
            case NAME: {
                treeMapFF = FilterField.NAME;
                geSet = tmName.navigableKeySet();
                break;
            }
            case TREASURETYPE: {
                treeMapFF = FilterField.TREASURETYPE;
                geSet = tmType.navigableKeySet();
                break;
            }
            case ARTIFACTTYPE: {
                treeMapFF = FilterField.ARTIFACTTYPE;
                geSet = tmType.navigableKeySet();
                break;
            }
            case CREATUREID: {
                treeMapFF = FilterField.CREATUREID;
                geSet = tmCreatureID.navigableKeySet();
                break;
            }
            case WEIGHT: {
                treeMapFF = FilterField.WEIGHT;
                geSet = tmWeight.navigableKeySet();
                break;
            }
            case VALUE: {
                treeMapFF = FilterField.VALUE;
                geSet = tmValue.navigableKeySet();
                break;
            }
        }
        return geSet;
    }

    private NavigableSet<GameElement> getGameElementsSetForParty(NavigableSet<GameElement> geSet) {
        switch (currentFilterField) {
            case ID: {
                treeMapFF = FilterField.ID;
                geSet = tmID.navigableKeySet();
                break;
            }
            case NAME: {
                treeMapFF = FilterField.NAME;
                geSet = tmName.navigableKeySet();
                break;
            }
            case CREATURETYPE: {
                treeMapFF = FilterField.CREATURETYPE;
                geSet = tmType.navigableKeySet();
                break;
            }
            case PARTYID: {
                treeMapFF = FilterField.PARTYID;
                geSet = tmPartyID.navigableKeySet();
                break;
            }
            case AGE: {
                treeMapFF = FilterField.AGE;
                geSet = tmAge.navigableKeySet();
                break;
            }
            case EMPATHY: {
                treeMapFF = FilterField.EMPATHY;
                geSet = tmEmpathy.navigableKeySet();
                break;
            }
            case FEAR: {
                treeMapFF = FilterField.FEAR;
                geSet = tmFear.navigableKeySet();
                break;
            }
            case CAPACITY: {
                treeMapFF = FilterField.CAPACITY;
                geSet = tmCapacity.navigableKeySet();
                break;
            }
            case HEIGHT: {
                treeMapFF = FilterField.HEIGHT;
                geSet = tmHeight.navigableKeySet();
                break;
            }
            case WEIGHT: {
                treeMapFF = FilterField.WEIGHT;
                geSet = tmWeight.navigableKeySet();
                break;
            }
        }
        return geSet;
    }

    public void updateMaps() {
        Enumeration<GameElement> gameElementEnumeration;

        if(gameLayer==GameLayer.CAVE && filterDomain == GameLayer.PARTY ||
                gameLayer==GameLayer.PARTY && filterDomain == GameLayer.CREATURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.TREASURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.ARTIFACT
                ) {
            tmID = new TreeMap<>();
            treeMapFF = FilterField.ID;
            gameElementEnumeration = this.children();
            while (gameElementEnumeration.hasMoreElements()) {
                GameElement ge = gameElementEnumeration.nextElement();
                tmID.put((Party)ge, ((Party)ge).getID());
            }

            switch (gameLayer) {
                case CAVE: { // Party fields
                    tmName = new TreeMap<>();
                    treeMapFF = FilterField.NAME;
                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {
                        GameElement ge = gameElementEnumeration.nextElement();
                        tmName.put((Party)ge, ((Party) ge).getName());
                    }
                    break;
                }
                case PARTY: { // Creature fields
                    tmID = new TreeMap<>();
                    tmName = new TreeMap<>();
                    tmType = new TreeMap<>();
                    tmPartyID = new TreeMap<>();
                    tmAge = new TreeMap<>();
                    tmEmpathy = new TreeMap<>();
                    tmFear = new TreeMap<>();
                    tmCapacity = new TreeMap<>();
                    tmHeight = new TreeMap<>();
                    tmWeight = new TreeMap<>();

                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {
                        GameElement ge = gameElementEnumeration.nextElement();
                        treeMapFF = FilterField.ID;
                        tmID.put((Creature)ge, ((Creature) ge).getID());

                        treeMapFF = FilterField.NAME;
                        tmName.put((Creature)ge, ((Creature) ge).name);

                        treeMapFF = FilterField.CREATURETYPE;
                        tmType.put((Creature)ge, ((Creature) ge).creatureType);

                        treeMapFF = FilterField.PARTYID;
                        tmPartyID.put((Creature)ge, ((Creature) ge).partyID);

                        treeMapFF = FilterField.AGE;
                        tmAge.put((Creature)ge, ((Creature) ge).age);

                        treeMapFF = FilterField.EMPATHY;
                        tmEmpathy.put((Creature)ge, ((Creature) ge).empathy);

                        treeMapFF = FilterField.FEAR;
                        tmFear.put((Creature)ge, ((Creature) ge).fear);

                        treeMapFF = FilterField.CAPACITY;
                        tmCapacity.put((Creature)ge, ((Creature) ge).carryingCapacity);

                        treeMapFF = FilterField.HEIGHT;
                        tmHeight.put((Creature)ge, ((Creature) ge).height);

                        treeMapFF = FilterField.WEIGHT;
                        tmWeight.put((Creature)ge, ((Creature) ge).weight);

                    }
                    break;
                }
                case CREATURE: { // Treasure fields
                    tmName = new TreeMap<>();
                    tmType = new TreeMap<>();
                    tmPartyID = new TreeMap<>();
                    tmWeight = new TreeMap<>();
                    tmCreatureID = new TreeMap<>();
                    tmID = new TreeMap<>();

                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {
                        GameElement ge = gameElementEnumeration.nextElement();
                        switch (ge.gameLayer) {
                            case TREASURE: {
                                treeMapFF = FilterField.TREASURETYPE;
                                tmType.put((Treasure)ge, ((Treasure) ge).treasureType);

                                treeMapFF = FilterField.CREATUREID;
                                tmPartyID.put((Treasure)ge, ((Treasure) ge).creatureID);

                                treeMapFF = FilterField.ID;
                                tmID.put((Treasure)ge, ((Treasure) ge).getID());

                                treeMapFF = FilterField.VALUE;
                                tmValue.put((Treasure)ge, ((Treasure) ge).value);

                                treeMapFF = FilterField.WEIGHT;
                                tmWeight.put((Treasure)ge, ((Treasure) ge).weight);
                                break;
                            }
                            case ARTIFACT: {
                                treeMapFF = FilterField.NAME;
                                tmName.put((Artifact) ge, ((Artifact) ge).name);

                                treeMapFF = FilterField.ARTIFACTTYPE;
                                tmType.put((Artifact) ge, ((Artifact) ge).artifactType);

                                treeMapFF = FilterField.CREATUREID;
                                tmCreatureID.put((Artifact) ge, ((Artifact) ge).creatureID);

                                treeMapFF = FilterField.ID;
                                tmID.put((Artifact) ge, ((Artifact) ge).getID());
                                break;
                            }
                        }

                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
        gameElementEnumeration = this.children();
        while (gameElementEnumeration.hasMoreElements()) {
            GameElement ge = gameElementEnumeration.nextElement();
            ge.updateMaps();
        }
    }

    @Override
    public int compareTo(GameElement ge) {
        if (filterDomain == GameLayer.PARTY) {
            switch (treeMapFF) {
                case ID: {
                    return ((Party)this).getID() - ((Party) ge).getID();
                }
                case NAME: {
                    return ((Party) this).name.compareTo(((Party) ge).name);
                }
                default:
                    return 0;
            }
        }
        return 0;
    }
}
