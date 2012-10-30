package bluebot.graph;


import static org.junit.Assert.*;

import org.junit.Test;



/**
 * Unit tests for the {@link Tile} class
 * 
 * @author Ruben Feyen
 */
public class TestTile {
	
	@Test
	public void testBorders() {
		final Tile tile = new Tile(0, 0);
		
		assertEquals(Border.UNKNOWN, tile.getBorderNorth());
		assertEquals(Border.UNKNOWN, tile.getBorderEast());
		assertEquals(Border.UNKNOWN, tile.getBorderSouth());
		assertEquals(Border.UNKNOWN, tile.getBorderWest());
		
		tile.setBorderNorth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile.getBorderNorth());
		tile.setBorderNorth(Border.OPEN);
		assertEquals(Border.OPEN, tile.getBorderNorth());
		tile.setBorderNorth(Border.UNKNOWN);
		
		assertEquals(Border.UNKNOWN, tile.getBorderNorth());
		assertEquals(Border.UNKNOWN, tile.getBorderEast());
		assertEquals(Border.UNKNOWN, tile.getBorderSouth());
		assertEquals(Border.UNKNOWN, tile.getBorderWest());
		
		tile.setBorderEast(Border.CLOSED);
		assertEquals(Border.CLOSED, tile.getBorderEast());
		tile.setBorderEast(Border.OPEN);
		assertEquals(Border.OPEN, tile.getBorderEast());
		tile.setBorderEast(Border.UNKNOWN);
		
		assertEquals(Border.UNKNOWN, tile.getBorderNorth());
		assertEquals(Border.UNKNOWN, tile.getBorderEast());
		assertEquals(Border.UNKNOWN, tile.getBorderSouth());
		assertEquals(Border.UNKNOWN, tile.getBorderWest());
		
		tile.setBorderSouth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile.getBorderSouth());
		tile.setBorderSouth(Border.OPEN);
		assertEquals(Border.OPEN, tile.getBorderSouth());
		tile.setBorderSouth(Border.UNKNOWN);
		
		assertEquals(Border.UNKNOWN, tile.getBorderNorth());
		assertEquals(Border.UNKNOWN, tile.getBorderEast());
		assertEquals(Border.UNKNOWN, tile.getBorderSouth());
		assertEquals(Border.UNKNOWN, tile.getBorderWest());
		
		tile.setBorderWest(Border.CLOSED);
		assertEquals(Border.CLOSED, tile.getBorderWest());
		tile.setBorderWest(Border.OPEN);
		assertEquals(Border.OPEN, tile.getBorderWest());
		tile.setBorderWest(Border.UNKNOWN);
		
		assertEquals(Border.UNKNOWN, tile.getBorderNorth());
		assertEquals(Border.UNKNOWN, tile.getBorderEast());
		assertEquals(Border.UNKNOWN, tile.getBorderSouth());
		assertEquals(Border.UNKNOWN, tile.getBorderWest());
	}
	
}