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

public class GameElement extends DefaultMutableTreeNode {
    GameLayer gameLayer = GameLayer.NONE;
    Integer ID = 0;
    //ArrayList<GameElement> gameElementArrayList = new ArrayList<GameElement>();

    Map<Map,String> fieldMap = new TreeMap<>();

    public GameElement() {
    }

    public GameElement(GameLayer layer, int ID) {
        gameLayer = layer;
        this.ID = ID;
        createMaps();
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
                    if (ID == ((Creature)gameElement).partyID) {
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
                    if (ID == ((Treasure)gameElement).creatureID) {
                        result = insertIfDuplicateGameElement(gameElement);
                        if (!result) {
                            //gameElementArrayList.add(gameElement);
                            this.add(gameElement);
                            result = true;
                        }
                    }
                }
                if (gameElement.gameLayer == GameLayer.ARTIFACT) {
                    if (ID == ((Artifact)gameElement).creatureID) {
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
        if(ge != null) {
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
        for (GameElement g : (Vector<GameElement>)this.children) {
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
        for (GameElement gameElement : (Vector<GameElement>)this.children) {
            output += gameElement.toString();
        }
        //return output;
        return "";
    }

    private void createMaps(){
        fieldMap.put(new TreeMap<GameElement, Integer>(new Comparator<GameElement>()
        {
            public int compare(GameElement o1, GameElement o2)
            {
                //comparison logic goes here
                return o1.ID - o2.ID;
            }
        }), "ID");

        switch (gameLayer) {
            case CAVE: { // Party fields
                fieldMap.put(new TreeMap<GameElement, String>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Party)o1).name.compareTo(((Party)o2).name);
                    }
                }), "Name");
                break;
            }
            case PARTY:  { // Creature fields
                fieldMap.put(new TreeMap<GameElement, String>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Creature)o1).name.compareTo(((Creature)o2).name);
                    }
                }), "Name");
                fieldMap.put(new TreeMap<GameElement, String>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Creature)o1).creatureType.compareTo(((Creature)o2).creatureType);
                    }
                }), "Type");
                fieldMap.put(new TreeMap<GameElement, Integer>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Creature)o1).partyID - ((Creature)o2).partyID;
                    }
                }), "Party ID");
                fieldMap.put(new TreeMap<GameElement, Integer>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Creature)o1).age - ((Creature)o2).age;
                    }
                }), "Age");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Creature)o1).empathy - ((Creature)o2).empathy;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Empathy");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Creature)o1).fear - ((Creature)o2).fear;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Fear");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Creature)o1).carryingCapacity - ((Creature)o2).carryingCapacity;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Capacity");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Creature)o1).height - ((Creature)o2).height;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Height");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Creature)o1).weight - ((Creature)o2).weight;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Weight");
                break;
            }
            case CREATURE:{ // Treasure fields
                fieldMap.put(new TreeMap<GameElement, Integer>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        if (o1.gameLayer == GameLayer.TREASURE){
                            return ((Treasure)o1).creatureID - ((Treasure)o2).creatureID;
                        } else {return ((Artifact)o1).creatureID - ((Artifact)o2).creatureID;}
                    }
                }), "ID");
                fieldMap.put(new TreeMap<GameElement, String>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        if (o1.gameLayer == GameLayer.TREASURE){
                            return ((Treasure)o1).treasureType.compareTo(((Treasure)o2).treasureType);
                        } else {return ((Artifact)o1).artifactType.compareTo(((Artifact)o2).artifactType);}
                    }
                }), "Type");
                fieldMap.put(new TreeMap<GameElement, Integer>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Creature)o1).partyID - ((Creature)o2).partyID;
                    }
                }), "Creature ID");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Treasure)o1).value - ((Treasure)o2).value;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Value");
                fieldMap.put(new TreeMap<GameElement, Double>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        double k = ((Treasure)o1).weight - ((Treasure)o2).weight;
                        return k > 0 ? 1 : (k<0 ? -1 : 0);
                    }
                }), "Weight");

                // Artifact fields not in Treasure
                fieldMap.put(new TreeMap<GameElement, String>(new Comparator<GameElement>()
                {
                    public int compare(GameElement o1, GameElement o2)
                    {
                        //comparison logic goes here
                        return ((Artifact)o1).name.compareTo(((Artifact)o2).name);
                    }
                }), "Name");
                break;
            }
            default: {break;}
        }
    }
}
