package bluebot.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class TileTest {

	@Test
	public void testEastFrom() {
		Tile source = new Tile(1,0);
		Tile destination = new Tile(0,0);
		assertTrue(source.isEastFrom(destination));
	}
	
	@Test
	public void testWesttFrom(){
		Tile source = new Tile(0,0);
		Tile destination = new Tile(1,0);
		assertTrue(source.isWestFrom(destination));
	}
	
	@Test
	public void testNorthFrom(){
		Tile source = new Tile(0,1);
		Tile other = new Tile(0,0);
		assertTrue(source.isNorthFrom(other));
	}
	
	@Test
	public void testSouthFrom(){
		Tile source = new Tile(0,0);
		Tile destination = new Tile(0,1);
		assertTrue(source.isSouthFrom(destination));
	}
//	@Test
//	public void testIsNeighbor(){
//		Tile tile1 = new Tile(0,0);
//		Tile tile2 = new Tile(1,0);
//		tile1.setBorderEast(Border.CLOSED);
//		assertFalse(tile1.isNeighborFrom(tile2));
//		Tile tile3 = new Tile(0,1);
//		tile3.setBorderSouth(Border.OPEN);
//		tile1.setBorderNorth(Border.OPEN);
//		assertTrue(tile1.isNeighborFrom(tile3));
//	}
	
	@Test
	public void setBorderNorth(){
		Tile tile1 = new Tile(0,0);
		tile1.setBorderNorth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderNorth());
		tile1.setBorderNorth(Border.OPEN);
		assertEquals(Border.OPEN, tile1.getBorderNorth());
		//TODO: this is strange ...
//		tile1.setBorderNorth(Border.UNKNOWN);
//		assertEquals(Border.UNKNOWN, tile1.getBorderNorth());
		tile1.setBorderNorth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderNorth());
	}
	
	@Test
	public void setBorderEast(){
		Tile tile1 = new Tile(0,0);
		tile1.setBorderEast(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderEast());
		tile1.setBorderEast(Border.OPEN);
		assertEquals(Border.OPEN, tile1.getBorderEast());
		//TODO: this is strange ...
//		tile1.setBorderEast(Border.UNKNOWN);
//		assertEquals(Border.UNKNOWN, tile1.getBorderEast());
		tile1.setBorderEast(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderEast());
	}
	
	@Test
	public void setBorderSouth(){
		Tile tile1 = new Tile(0,0);
		tile1.setBorderSouth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderSouth());
		tile1.setBorderSouth(Border.OPEN);
		assertEquals(Border.OPEN, tile1.getBorderSouth());
		//TODO: this is strange ...
//		tile1.setBorderSouth(Border.UNKNOWN);
//		assertEquals(Border.UNKNOWN, tile1.getBorderSouth());
		tile1.setBorderSouth(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderSouth());
	}

	@Test
	public void setBorderWest(){
		Tile tile1 = new Tile(0,0);
		tile1.setBorderWest(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderWest());
		tile1.setBorderWest(Border.OPEN);
		assertEquals(Border.OPEN, tile1.getBorderWest());
		//TODO: this is strange ...
//		tile1.setBorderWest(Border.UNKNOWN);
//		assertEquals(Border.UNKNOWN, tile1.getBorderWest());
		tile1.setBorderWest(Border.CLOSED);
		assertEquals(Border.CLOSED, tile1.getBorderWest());
	}

}
