/**
 * Artifact.java
 * 10/30/14
 * Author Nick Radonic
 * Artifact class game element in CAve Game
 */
package DataTree;

public class Artifact extends GameElement{
    private static int artifactsCreated = 0;
    final String name;
    int creatureID;
    final String artifactType;

    public Artifact(String name, String artifactType){
        super(GameLayer.ARTIFACT, artifactsCreated+1);
        artifactsCreated++;

        this.ID = artifactsCreated;
        this.name = name;
        this.artifactType = artifactType;
        gameLayer = GameLayer.ARTIFACT;
    }

    public Artifact(int ID, String artifactType, int creatureID, String name){
        super(GameLayer.ARTIFACT, ID);
        this.name = name;
        this.artifactType = artifactType;
        this.creatureID = creatureID;
    }

    public String toString(){
        String artifactOutput = String.format("          a : %6d : %8s : %6d : %8s\n",
                ID, artifactType, creatureID, name);
        return artifactOutput;
    }

    public String artifactOnlyToString(){
        return "a : "+ID+" : "+name+"\n";
    }

    public int getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public int getCreatureID(){
        return creatureID;
    }

    public void setCreatureID(int creatureID){
        this.creatureID = creatureID;
    }

    public String getArtifactType(){
        return artifactType;
    }


    public Artifact makeCopy(){
        return new Artifact(ID, artifactType, creatureID, name);
    }

}

