package Test;

import DataTree.GameElement;
import DataTree.GameLayer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.*;

import sun.jvm.hotspot.utilities.Assert;

import static org.testng.Assert.*;

public class GameElementTest {
    GameElement sut = new GameElement(GameLayer.CAVE, 100);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetGameLayer() throws Exception {
        Assert.that(sut.getGameLayer()==GameLayer.CAVE, "gamelayer == Cave");
    }

    @Test
    public void testGetID() throws Exception {

    }

    @Test
    public void testAddGameElementTree() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testModifyDisplayJTree() throws Exception {

    }

    @Test
    public void testCompareTo() throws Exception {

    }
}