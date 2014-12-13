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
    private static FilterField treeMapFF = FilterField.ID;
    private static FilterField currentFilterField = FilterField.ID;

    GameLayer gameLayer = GameLayer.NONE;
    Integer ID = 0;

    private FilterField filterField = FilterField.ID;
    private GameLayer filterDomain = GameLayer.CAVE;

    private TreeMap<GameElement, Integer> tmID = new TreeMap<GameElement, Integer>();

    private TreeMap<GameElement, String> tmName = new TreeMap<GameElement, String>();
    private TreeMap<GameElement, String> tmType = new TreeMap<GameElement, String>();

    private TreeMap<GameElement, Double> tmHeight = new TreeMap<GameElement, Double>();
    private TreeMap<GameElement, Double> tmAge = new TreeMap<GameElement, Double>();

    private TreeMap<GameElement, Double> tmEmpathy = new TreeMap<GameElement, Double>();
    private TreeMap<GameElement, Double> tmFear = new TreeMap<GameElement, Double>();
    private TreeMap<GameElement, Double> tmCapacity = new TreeMap<GameElement, Double>();

    private TreeMap<GameElement, Double> tmValue = new TreeMap<GameElement, Double>();
    private TreeMap<GameElement, Double> tmWeight = new TreeMap<GameElement, Double>();

    //private GameElement parent;

    GameElement() {
    }

    public GameElement(GameLayer layer, int ID) {
        gameLayer = layer;
        this.ID = ID;

    }

    public void setParentElement(GameElement ge){
        parent = ge;
    }

    public GameElement getParent(){
        return (GameElement)parent;
    }

    public GameLayer getGameLayer() {
        return gameLayer;
    }

    int getID() {
        return ID;
    }

    boolean addGameElementTree(GameElement gameElement) {
        boolean result = false;
        switch (gameLayer) {
            case CAVE: {
                if (gameElement.gameLayer == GameLayer.PARTY) {
                    result = addGeUpdateMaps(gameElement);
                }
                if (gameElement.gameLayer == GameLayer.CREATURE || gameElement.gameLayer == GameLayer.TREASURE ||
                        gameElement.gameLayer == GameLayer.ARTIFACT || gameElement.gameLayer == GameLayer.JOB ) {
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
                    if (gameElement.gameLayer == GameLayer.TREASURE || gameElement.gameLayer == GameLayer.ARTIFACT ||
                            gameElement.gameLayer == GameLayer.JOB) {
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
                if (gameElement.gameLayer == GameLayer.JOB) {
                    if (ID == ((Job) gameElement).creatureID) {
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
        gameElement.setParentElement(this);
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
//        for (GameElement g : (Vector<GameElement>) this.children) {
//            result = g.addGameElementTree(gameElement);
//            if (result) {
//                break;
//            }
//        }

        Vector<GameElement> ge = this.children;
        if (ge != null) {
            for (GameElement g : ge) {
                result = g.addGameElementTree(gameElement);
                if (result) {
                    break;
                }
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

    public String treeList(GameLayer searchLayer, String searchText){
        String outputString = "";

        if(gameLayer == searchLayer) {
            String tempStr = this.toString();
            outputString = tempStr.toLowerCase().contains(searchText.toLowerCase())? tempStr : "";
        }

        Enumeration<GameElement> geSource = this.children();
        while(geSource.hasMoreElements()){
            GameElement geS = geSource.nextElement();
            outputString += geS.treeList(searchLayer, searchText);
        }
        return outputString;
    }

    public void modifyDisplayJTree(GameLayer filterDomain, FilterField filterField) {

        if(gameLayer==GameLayer.CAVE && filterDomain == GameLayer.PARTY ||
                gameLayer==GameLayer.PARTY && filterDomain == GameLayer.CREATURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.TREASURE ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.ARTIFACT ||
                gameLayer==GameLayer.CREATURE && filterDomain == GameLayer.JOB
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
            case ARTIFACTTYPE:{
                treeMapFF = FilterField.ARTIFACTTYPE;
                geSet = tmType.navigableKeySet();
                break;
            }
            case JOBTYPE:{
                treeMapFF = FilterField.JOBTYPE;
                geSet = tmType.navigableKeySet();
                break;
            }
            case TREASURETYPE:{
                treeMapFF = FilterField.TREASURETYPE;
                geSet = tmType.navigableKeySet();
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
                gameLayer==GameLayer.CREATURE && gameLayer1 == GameLayer.ARTIFACT ||
                gameLayer==GameLayer.CREATURE && gameLayer1 == GameLayer.JOB
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
                            case JOB:{
                                currentFilterField = FilterField.JOBTYPE;
                                tmType.put((Job) ge, ((Job) ge).jobType);

                                currentFilterField = FilterField.ID;
                                tmID.put((Job) ge, ((Job) ge).getID());
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
        }else if (gameLayer == GameLayer.TREASURE && ge.gameLayer == GameLayer.TREASURE){
            return compareAtTreasureLevel((Treasure) ge);
        }else if (gameLayer == GameLayer.ARTIFACT && ge.gameLayer == GameLayer.ARTIFACT){
            return compareAtArtifactLevel((Artifact) ge);
        }else if (gameLayer == GameLayer.JOB && ge.gameLayer == GameLayer.JOB) {
            return compareAtJobLevel((Job) ge);
        } else { return gameLayer.ordinal() - ge.gameLayer.ordinal();}
    }

    private int compareAtArtifactLevel(Artifact ge) {
        int result = compareArtifactID(ge);
        switch (currentFilterField) {
            case ID: {
                break;
            }
            case NAME: {
                int tempCompare =   ((Artifact) this).name.compareToIgnoreCase(((Artifact) ge).name);
                if(tempCompare == 0 ){ tempCompare = compareArtifactID(ge);}
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case ARTIFACTTYPE: {
                int tempCompare = ((Artifact) this).artifactType.compareToIgnoreCase(((Artifact) ge).artifactType);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            default:
                break;
        }
        return result;
    }

    private int compareArtifactID(Artifact ge) {
        return ((Artifact)this).getID() - ((Artifact) ge).getID();
    }

    private int compareAtTreasureLevel(Treasure ge) {
        int result = compareTreasureID(ge);
        switch (currentFilterField) {
            case ID: {
                break;
            }
            case TREASURETYPE: {
                int tempCompare =  (int)(((Treasure) this).treasureType.compareToIgnoreCase(((Treasure) ge).treasureType));
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case WEIGHT: {
                int tempCompare =   (int)(((Treasure) this).weight - ((Treasure) ge).weight);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case VALUE: {
                int tempCompare = (int)(((Treasure) this).value - ((Treasure) ge).value);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            default:
                break;
        }
        return result;
    }

    private int compareTreasureID(Treasure ge) {
        return ((Treasure)this).getID() - ((Treasure) ge).getID();
    }

    private int compareAtJobLevel(Job ge) {
        int result = compareJobID(ge);
        switch (currentFilterField) {
            case ID: {
                break;
            }
            case JOBTYPE: {
                int tempCompare = (int)( ((Job) this).jobType.compareToIgnoreCase( ((Job) ge).jobType ) );
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            default:
                break;
        }
        return result;
    }

    private int compareJobID(Job ge) {
        return ((Job)this).getID() - ((Job) ge).getID();
    }


    private int compareAtCreatureLevel(Creature ge) {
        int result = compareCreatureID(ge);
        switch (currentFilterField) {
            case ID: {
                break;
            }
            case NAME: {
                int tempCompare = ((Creature) this).name.compareToIgnoreCase(((Creature) ge).name);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case CREATURETYPE: {
                int tempCompare = ((Creature) this).creatureType.compareToIgnoreCase(((Creature) ge).creatureType);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case EMPATHY: {
                int tempCompare =  (int)(((Creature) this).empathy - ((Creature) ge).empathy);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case FEAR: {
                int tempCompare = (int)( ((Creature) this).fear - ((Creature) ge).fear);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case CAPACITY: {
                int tempCompare = (int)(((Creature) this).carryingCapacity - ((Creature) ge).carryingCapacity);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case AGE: {
                int tempCompare = (int)(((Creature) this).age - (((Creature) ge).age));
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case WEIGHT: {
                int tempCompare = (int)(((Creature) this).weight - ((Creature) ge).weight);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            case HEIGHT: {
                int tempCompare = (int)(((Creature) this).height -((Creature) ge).height);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }

            default:
                break;
        }
        return result;
    }

    private int compareCreatureID(Creature ge) {
        return ((Creature)this).getID() - ((Creature) ge).getID();
    }

    private int compareAtPartyLevel(Party ge) {
        int result = comparePartyID(ge);
        switch (currentFilterField) {
            case ID: {
                break;
            }
            case NAME: {
                int tempCompare = ((Party) this).name.compareToIgnoreCase(((Party) ge).name);
                result = tempCompare != 0 ? tempCompare : result;
                break;
            }
            default:
                return 0;
        }
        return result;
    }

    private int comparePartyID(Party ge) {
        return ((Party)this).getID() - ((Party) ge).getID();
    }

    GameElement makeCopy(){
        GameElement ge = new GameElement(gameLayer, getID());
        return ge;
    }

    private static void Koppy(GameElement source, GameElement destination, GameLayer searchLayer, String searchText){
        Enumeration<GameElement> geSource = source.children();
        while(geSource.hasMoreElements()){
            GameElement geS = geSource.nextElement();

            if(geS.gameLayer != searchLayer || geS.gameLayer == searchLayer && geS.toString().toLowerCase().contains(searchText.toLowerCase())) {
                GameElement geTemp = geS.makeCopy();
                destination.add(geTemp);
                GameElement.Koppy(geS, geTemp, searchLayer, searchText);
            }
        }
    }

//    public Vector<GameElement> getTasks(){
//        Vector<GameElement> geV = new Vector<>();
//
//        Vector<GameElement> ge = this.children;
//        if (ge != null) {
//            for (GameElement g : ge) {
//                if (g.gameLayer == GameLayer.JOB){
//                    geV.add(g);
//                } if (g.gameLayer == GameLayer.CAVE || g.gameLayer == GameLayer.PARTY ||g.gameLayer == GameLayer.CREATURE ){
//                    for(GameElement geSub:  g.getTasks()){
//                        geV.add(geSub);
//                    }
//                }
//            }
//        }
//        return geV;
//    }

    public Vector<Job> getTasks(){
        Vector<Job> jobs = new Vector<>();
        if(gameLayer == GameLayer.CAVE || gameLayer==GameLayer.PARTY || gameLayer==GameLayer.CREATURE) {
            for(GameElement ge : getCreatureStuff(GameLayer.JOB)){
                jobs.add((Job) ge);
            };
        } else {return null;}
        return jobs;
    }

    public Vector<Artifact> getArtifacts(){
        Vector<Artifact> artifacts = new Vector<>();
        if(gameLayer == GameLayer.CAVE || gameLayer==GameLayer.PARTY || gameLayer==GameLayer.CREATURE) {
            for(GameElement ge : getCreatureStuff(GameLayer.ARTIFACT)){
                artifacts.add((Artifact) ge);
            };
        } else {return null;}
        return artifacts;
    }

    public Vector<Treasure> getTreasures(){
        Vector<Treasure> treasures = new Vector<>();
        if(gameLayer == GameLayer.CAVE || gameLayer==GameLayer.PARTY || gameLayer==GameLayer.CREATURE) {
            for(GameElement ge : getCreatureStuff(GameLayer.TREASURE)){
                treasures.add((Treasure) ge);
            };
        } else {return null;}
        return treasures;
    }


    public Vector<GameElement> getCreatureStuff(GameLayer gameLayerS){
        Vector<GameElement> geV = new Vector<>();

        Vector<GameElement> ge = this.children;
        if (ge != null) {
            for (GameElement g : ge) {
                if (g.gameLayer == gameLayerS){
                    geV.add(g);
                } if (g.gameLayer == GameLayer.CAVE || g.gameLayer == GameLayer.PARTY ||g.gameLayer == GameLayer.CREATURE ){
                    for(GameElement geSub:  g.getCreatureStuff(gameLayerS)){
                        geV.add(geSub);
                    }
                }
            }
        }
        return geV;
    }


//    public Creature findCreatureFromID(int targetCreatureID){
//        GameElement geV = new GameElement();
//        Creature tempCreature = null;
//        Vector<GameElement> ge = this.children;
//
//        if (ge != null) {
//            for (GameElement g : ge) {
//                if(tempCreature == null) {
//                    if (g.gameLayer == GameLayer.CREATURE) {
//                        tempCreature = (targetCreatureID == getID()) ? (Creature)this : null;
//                    } else if (g.gameLayer == GameLayer.CAVE ||
//                            g.gameLayer == GameLayer.PARTY) {
//                        tempCreature = g.findCreatureFromID(targetCreatureID);
//                    } else {
//                        tempCreature = null;
//                    }
//                }
//            }
//        }
//        return tempCreature;
//    }


    protected void clearTreeMaps(){
        tmID = new TreeMap<GameElement, Integer>();

        tmName = new TreeMap<GameElement, String>();
        tmType = new TreeMap<GameElement, String>();

        tmHeight = new TreeMap<GameElement, Double>();
        tmAge = new TreeMap<GameElement, Double>();

        tmEmpathy = new TreeMap<GameElement, Double>();
        tmFear = new TreeMap<GameElement, Double>();
        tmCapacity = new TreeMap<GameElement, Double>();

        tmValue = new TreeMap<GameElement, Double>();
        tmWeight = new TreeMap<GameElement, Double>();
    }
}
