/**
 * Artifact.java
 * Created by Nick Radonic on 10/30/14.
 * Artifact class
 */
package DataTree;

public class Artifact {
        private static int artifactsCreated = 0;
        final int ID;
        String name;
        int creatureID;
        String artifactType;

        public Artifact(String name, String artifactType){
            artifactsCreated++;
            ID = artifactsCreated;
            this.name = name;
            this.artifactType = artifactType;
        }

        public Artifact(int ID, String artifactType, int creatureID, String name){
        this.ID = ID;
        this.name = name;
        this.artifactType = artifactType;
        this.creatureID = creatureID;
    }

    public String toString(){
            String artifactOutput = "          a : "+ID+" : "+artifactType+" : "+creatureID+" : "+name+"\n";
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
            return ID;
        }

        public void setCreatureID(int creatureID){
            this.creatureID = creatureID;
        }

        public String getTreasureType(){
            return artifactType;
        }

    }

