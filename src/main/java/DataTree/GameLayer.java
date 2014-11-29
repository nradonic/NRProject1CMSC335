package DataTree;

/**
 * GameLayer.java
 * 11/2/14
 * Created by Nick Radonic
 * ENUM definitions for the game components, inherited from the base GameElement
 */


public enum GameLayer {CAVE, PARTY, CREATURE, TREASURE, ARTIFACT, JOB, NONE;

    GameLayer Next(){
        if(this == CAVE){return PARTY;}
        if(this == PARTY){return CREATURE;}
        return NONE;
    }
}
