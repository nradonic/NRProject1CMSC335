package GameControl;

import CaveGameGUI.GameControlWindow;
import DataTree.Cave;

/**
 * CaveGame.java
 * Created by Nick Radonic on 11/1/14.
 */
public class CaveGame {

    public static void main(String[] args){

        Cave gameCave = new Cave("Big Hollow Mountain");

        GameControlWindow gcw = new GameControlWindow(gameCave);

    }
}
