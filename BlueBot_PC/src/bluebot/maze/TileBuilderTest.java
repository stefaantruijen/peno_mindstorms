/**
 * 
 */
package bluebot.maze;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Tile;

/**
 * @author Incalza Dario
 *
 */
public class TileBuilderTest {

	@Test
	public void test() {
		Tile t = new Tile(0,0);
		t.setAllBordersOpen(true);
		t.setBorderEast(Border.CLOSED);
		t.setBorderWest(Border.CLOSED);
		t.setBarCode(2);
		
		Tile t2 = new Tile(1,1);
		t2.setAllBordersOpen(true);
		t2.setBorderEast(Border.CLOSED);
		t2.setBorderWest(Border.CLOSED);
		t2.setBarCode(2);
		t2.setSeesaw(true);
		assertTrue(TileBuilder.fromTileToString(t).contains("Straight"));
		assertTrue(TileBuilder.fromTileToString(t).contains("2"));
		assertTrue(TileBuilder.fromTileToString(t2).contains("2"));
		assertTrue(TileBuilder.fromTileToString(t2).contains("Seesaw"));

	}

}
