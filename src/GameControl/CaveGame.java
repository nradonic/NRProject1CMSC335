package GameControl;
/**
 * GameControl.java
 * Nov. 1, 2014
 * Nick Radonic
 * Program entry point
 */
import CaveGameGUI.GameControlWindow;
import DataTree.Cave;

/**
 * CaveGame.java
 * 11/1/14
 * Created by Nick Radonic
 * Top level Cave Game entry point
 */
public class CaveGame {

    public static void main(String[] args){

        Cave gameCave = new Cave("Big Hollow Mountain");

        GameControlWindow gcw = new GameControlWindow(gameCave);

    }
}
