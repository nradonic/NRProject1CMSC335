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
    public static FilterField currentFilterField = FilterField.ID;

    protected GameLayer gameLayer = GameLayer.NONE;
    protected Integer ID = 0;

    private FilterField filterField = FilterField.ID;
    private GameLayer filterDomain = GameLayer.CAVE;

    TreeMap<GameElement, Integer> tmID = new TreeMap<>();
    TreeMap<GameElement, String> tmName = new TreeMap<>();
    TreeMap<GameElement, String> tmType = new TreeMap<>();

    TreeMap<GameElement, Double> tmHeight = new TreeMap<>();
    TreeMap<GameElement, Integer> tmAge = new TreeMap<>();

    TreeMap<GameElement, Double> tmEmpathy = new TreeMap<>();
    TreeMap<GameElement, Double> tmFear = new TreeMap<>();
    TreeMap<GameElement, Double> tmCapacity = new TreeMap<>();

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
                    result = addGeUpdateMaps(gameElement);
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
                        result = addGeUpdateMaps(gameElement);
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
                        result = addGeUpdateMaps(gameElement);
                    }
                }
                if (gameElement.gameLayer == GameLayer.ARTIFACT) {
                    if (ID == ((Artifact) gameElement).creatureID) {
                        result = addGeUpdateMaps(gameElement);
                    }
                }
            }
            break;
            case NONE:
                break;
        }

        return result;
    }

    private boolean addGeUpdateMaps(GameElement gameElement) {
        boolean result;
        result = insertIfDuplicateGameElement(gameElement);
        if (!result) {
            this.add(gameElement);
        }
        result = true;
        updateMaps(gameElement.gameLayer);
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
        return "";
    }

    public void modifyDisplayJTree(GameLayer filterDomain, FilterField filterField) {
//        this.filterDomain = filterDomain;
//        this.filterField = filterField;

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
                geSet = getGameElementsSetForParty();
            } else if (gameLayer == GameLayer.CREATURE) {
                geSet = getGameElementsSetForCreature();
            }

            Iterator<GameElement> ge = geSet.iterator();
            while (ge.hasNext()) {
                GameElement geItem = ge.next();
                remove(geItem);
                add(geItem);
            }
        } else {
            recursivelyUpdateDisplayJTree(filterDomain, filterField);
        }
    }

    private void recursivelyUpdateDisplayJTree(GameLayer filterDomain, FilterField filterField) {
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

    private NavigableSet<GameElement> getGameElementsSetForParty() {
        NavigableSet<GameElement> geSet = tmID.navigableKeySet();
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
            case AGE: {
                treeMapFF = FilterField.AGE;
                geSet = tmAge.navigableKeySet();
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

    private NavigableSet<GameElement> getGameElementsSetForCreature() {
        NavigableSet<GameElement> geSet = tmID.navigableKeySet();
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
            default:break;
        }
        return geSet;
    }


    private void updateMaps(GameLayer gameLayer1) {
        Enumeration<GameElement> gameElementEnumeration;

        if(gameLayer==GameLayer.CAVE && gameLayer1 == GameLayer.PARTY ||
                gameLayer==GameLayer.PARTY && gameLayer1 == GameLayer.CREATURE ||
                gameLayer==GameLayer.CREATURE && gameLayer1 == GameLayer.TREASURE ||
                gameLayer==GameLayer.CREATURE && gameLayer1 == GameLayer.ARTIFACT
                ) {

            switch (gameLayer) {
                case CAVE: { // Party fields
                    tmID = new TreeMap<>();
                    tmName = new TreeMap<>();

                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {

                        GameElement ge = gameElementEnumeration.nextElement();
                        currentFilterField = FilterField.ID;
                        tmID.put((Party)ge, ((Party)ge).getID());

                        currentFilterField = FilterField.NAME;
                        tmName.put((Party)ge, ((Party) ge).getName());
                    }
                    break;
                }
                case PARTY: { // Creature fields
                    tmID = new TreeMap<>();
                    tmName = new TreeMap<>();
                    tmType = new TreeMap<>();
                    tmAge = new TreeMap<>();
                    tmEmpathy = new TreeMap<>();
                    tmFear = new TreeMap<>();
                    tmCapacity = new TreeMap<>();
                    tmHeight = new TreeMap<>();
                    tmWeight = new TreeMap<>();

                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {
                        GameElement ge = gameElementEnumeration.nextElement();
                        currentFilterField = FilterField.ID;
                        tmID.put((Creature)ge, ((Creature) ge).getID());

                        currentFilterField = FilterField.NAME;
                        tmName.put((Creature)ge, ((Creature) ge).name);

                        currentFilterField = FilterField.CREATURETYPE;
                        tmType.put((Creature)ge, ((Creature) ge).creatureType);

                        currentFilterField = FilterField.AGE;
                        tmAge.put((Creature)ge, ((Creature) ge).age);

                        currentFilterField = FilterField.EMPATHY;
                        tmEmpathy.put((Creature)ge, ((Creature) ge).empathy);

                        currentFilterField = FilterField.FEAR;
                        tmFear.put((Creature)ge, ((Creature) ge).fear);

                        currentFilterField = FilterField.CAPACITY;
                        tmCapacity.put((Creature)ge, ((Creature) ge).carryingCapacity);

                        currentFilterField = FilterField.HEIGHT;
                        tmHeight.put((Creature)ge, ((Creature) ge).height);

                        currentFilterField = FilterField.WEIGHT;
                        tmWeight.put((Creature)ge, ((Creature) ge).weight);

                    }
                    break;
                }
                case CREATURE: { // Treasure fields
                    tmName = new TreeMap<>();
                    tmType = new TreeMap<>();
                    tmWeight = new TreeMap<>();
                    tmID = new TreeMap<>();

                    gameElementEnumeration = this.children();
                    while (gameElementEnumeration.hasMoreElements()) {
                        GameElement ge = gameElementEnumeration.nextElement();
                        switch (ge.gameLayer) {
                            case TREASURE: {
                                currentFilterField = FilterField.TREASURETYPE;
                                tmType.put((Treasure)ge, ((Treasure) ge).treasureType);

                                currentFilterField = FilterField.ID;
                                tmID.put((Treasure)ge, ((Treasure) ge).getID());

                                currentFilterField = FilterField.VALUE;
                                tmValue.put((Treasure)ge, ((Treasure) ge).value);

                                currentFilterField = FilterField.WEIGHT;
                                tmWeight.put((Treasure)ge, ((Treasure) ge).weight);
                                break;
                            }
                            case ARTIFACT: {
                                currentFilterField = FilterField.NAME;
                                tmName.put((Artifact) ge, ((Artifact) ge).name);

                                currentFilterField = FilterField.ARTIFACTTYPE;
                                tmType.put((Artifact) ge, ((Artifact) ge).artifactType);

                                currentFilterField = FilterField.ID;
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
//        gameElementEnumeration = this.children();
//        while (gameElementEnumeration.hasMoreElements()) {
//            GameElement ge = gameElementEnumeration.nextElement();
//            ge.updateMaps(gameLayer1);
//        }
    }

    @Override
    public int compareTo(GameElement ge) {
        if (gameLayer == GameLayer.PARTY) {
            return compareAtPartyLevel((Party) ge);
        } else if (gameLayer == GameLayer.CREATURE){
            return compareAtCreatureLevel((Creature) ge);
        }else if (gameLayer == GameLayer.TREASURE){
            return compareAtTreasureLevel((Treasure) ge);
        }else if (gameLayer == GameLayer.ARTIFACT){
            return compareAtArtifactLevel((Artifact) ge);
        }
        return 0;
    }

    private int compareAtArtifactLevel(Artifact ge) {
        switch (currentFilterField) {
            case ID: {
                return ((Artifact)this).getID() - ((Artifact) ge).getID();
            }
            case NAME: {
                return ((Artifact) this).name.compareTo(((Artifact) ge).name);
            }
            case ARTIFACTTYPE: {
                return ((Artifact) this).artifactType.compareTo(((Artifact) ge).artifactType);
            }
            default:
                return 0;
        }
    }

    private int compareAtTreasureLevel(Treasure ge) {
        switch (currentFilterField) {
            case ID: {
                return ((Treasure)this).getID() - ((Treasure) ge).getID();
            }
            case WEIGHT: {
                return (int)(((Treasure) this).weight - ((Treasure) ge).weight);
            }
            case VALUE: {
                return (int)(((Treasure) this).value - ((Treasure) ge).value);
            }
            default:
                return 0;
        }
    }

    private int compareAtCreatureLevel(Creature ge) {
        switch (currentFilterField) {
            case ID: {
                return ((Creature)this).getID() - ((Creature) ge).getID();
            }
            case NAME: {
                return ((Creature) this).name.compareTo(((Creature) ge).name);
            }
            case CREATURETYPE: {
                return ((Creature) this).creatureType.compareTo(((Creature) ge).creatureType);
            }
            case EMPATHY: {
                return (int)(((Creature) this).empathy - ((Creature) ge).empathy);
            }
            case FEAR: {
                return (int)( ((Creature) this).fear - ((Creature) ge).fear);
            }
            case CAPACITY: {
                return (int)(((Creature) this).carryingCapacity - ((Creature) ge).carryingCapacity);
            }
            case AGE: {
                return ((Creature) this).age - (((Creature) ge).age);
            }
            case WEIGHT: {
                return (int)(((Creature) this).weight - ((Creature) ge).weight);
            }
            case HEIGHT: {
                return ((Creature) this).name.compareTo(((Creature) ge).name);
            }

            default:
                return 0;
        }
    }

    private int compareAtPartyLevel(Party ge) {
        switch (currentFilterField) {
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
}
