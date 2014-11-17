package DataTree;
/**
 * GameElement.java
 * Oct. 30, 2014
 * Nick Radonic
 * Base game element, used for game element inheritance in Project 1, Cave Game
 */
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

/**
 * GameElement.java
 * Base class to allow tree recursion on game element layers
 * Created by NickRadonic on 11/2/14.
 */

public class GameElement extends DefaultMutableTreeNode {
    GameLayer gameLayer = GameLayer.NONE;
    int ID = 0;
    //ArrayList<GameElement> gameElementArrayList = new ArrayList<GameElement>();

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
}
