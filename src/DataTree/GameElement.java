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
    GameLayer gameLayer = GameLayer.NONE;
    Integer ID = 0;
    private FilterField filterField = FilterField.NONE;
    private GameLayer filterDomain = GameLayer.NONE;
    private FilterField currentFilterField = FilterField.NONE;
    //ArrayList<GameElement> gameElementArrayList = new ArrayList<GameElement>();

//    class TreeMapMine extends TreeMap implements Comparable<TreeMapMine>{
//        int compareTo(Object t){
//            return 1;
//        }
//    }
//
//    Map<TreeMap,String> fieldMap = new TreeMap<>();

    TreeMap<GameElement, Integer> tmID = new TreeMap<>();
    TreeMap<GameElement, String> tmName = new TreeMap<>();

    TreeMap<GameElement, String> tmType = new TreeMap<>();
    TreeMap<GameElement, Integer> tmPartyID = new TreeMap<>();
    TreeMap<GameElement, Integer> tmCreatureID = new TreeMap<>();

    TreeMap<GameElement, Double> tmValue = new TreeMap<>();
    TreeMap<GameElement, Double> tmWeight = new TreeMap<>();
    TreeMap<GameElement, Double> tmHeight = new TreeMap<>();
    TreeMap<GameElement, Integer> tmAge = new TreeMap<>();

    TreeMap<GameElement, Double> tmEmpathy = new TreeMap<>();
    TreeMap<GameElement, Double> tmFear = new TreeMap<>();
    TreeMap<GameElement, Double> tmCapacity = new TreeMap<>();


    public GameElement() {
    }

    public GameElement(GameLayer layer, int ID) {
        gameLayer = layer;
        this.ID = ID;
        //createOrUpdateMaps();
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
            Optional<GameElement> geExisting = ge.stream().filter(
                    p -> p.ID == gameElement.ID &&
                            p.gameLayer == gameElement.gameLayer).findFirst();
            if (geExisting.isPresent()) {
                GameElement tempGE = geExisting.get();
                this.remove(tempGE);
                this.add(gameElement);
                //tempGE = gameElement;
                result = true;
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

    public void createOrUpdateMaps(GameLayer filterDomain, FilterField filterField) {
        this.filterDomain = filterDomain;
        this.filterField = filterField;

        tmID = new TreeMap<>();

        Enumeration<GameElement> gameElementEnumeration = this.children();
        while (gameElementEnumeration.hasMoreElements()) {
            GameElement ge = gameElementEnumeration.nextElement();
            tmID.put(ge, ge.ID);
        }

        switch (gameLayer) {
            case CAVE: { // Party fields
                if(filterDomain == GameLayer.PARTY){
                    currentFilterField = filterField;
                }
                tmName = new TreeMap<>();
                gameElementEnumeration = this.children();
                while (gameElementEnumeration.hasMoreElements()) {
                    GameElement ge = gameElementEnumeration.nextElement();
                    tmName.put(ge, ((Party) ge).getName());
                }
                break;
            }
            case PARTY: { // Creature fields
                if(filterDomain == GameLayer.CREATURE){
                    currentFilterField = filterField;
                }
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
                    tmName.put(ge, ((Creature) ge).name);
                    tmType.put(ge, ((Creature) ge).creatureType);
                    tmPartyID.put(ge, ((Creature) ge).partyID);
                    tmAge.put(ge, ((Creature) ge).age);
                    tmEmpathy.put(ge, ((Creature) ge).empathy);
                    tmFear.put(ge, ((Creature) ge).fear);
                    tmCapacity.put(ge, ((Creature) ge).carryingCapacity);
                    tmHeight.put(ge, ((Creature) ge).height);
                    tmWeight.put(ge, ((Creature) ge).weight);

                }
                break;
            }
            case CREATURE: { // Treasure fields
                if(filterDomain == GameLayer.TREASURE || filterDomain == GameLayer.ARTIFACT){
                    currentFilterField = filterField;
                }
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
                            tmType.put(ge, ((Treasure) ge).treasureType);
                            tmPartyID.put(ge, ((Treasure) ge).creatureID);
                            tmID.put(ge, ((Treasure) ge).ID);
                            tmValue.put(ge, ((Treasure) ge).value);
                            tmWeight.put(ge, ((Treasure) ge).weight);
                            break;
                        }
                        case ARTIFACT: {
                            tmName.put(ge, ((Artifact) ge).name);
                            tmType.put(ge, ((Artifact) ge).artifactType);
                            tmCreatureID.put(ge, ((Artifact) ge).creatureID);
                            tmID.put(ge, ((Artifact) ge).ID);
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

        gameElementEnumeration = this.children();
        while (gameElementEnumeration.hasMoreElements()) {
            GameElement ge = gameElementEnumeration.nextElement();
            ge.createOrUpdateMaps(filterDomain, filterField);
        }


    }

    public int compareTo(GameElement ge) {
        if (filterDomain == gameLayer) {
            if(gameLayer == GameLayer.PARTY) {
                switch (filterField) {
                    case ID: {
                        return this.ID - ((Party) ge).ID;
                    }
                    case NAME: {
                        return ((Party) this).name.compareTo(((Party) ge).name);
                    }
                    default:
                        return 0;
                }
            }
        }
        return 0;
    }
}
