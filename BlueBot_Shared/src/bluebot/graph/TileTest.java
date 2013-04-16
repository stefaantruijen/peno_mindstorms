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
		Tile source = new Tile(0,2);
		Tile other = new Tile(0,1);
		assertTrue(source.isNorthFrom(other));
	}
	
	@Test
	public void testSouthFrom(){
		Tile source = new Tile(0,1);
		Tile destination = new Tile(0,2);
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
	
	//TODO: THIS HAS BEEN CHANGED, MAY CAUSE BUGS?
//	@Test
//	public void testOnceUnknownTest(){
//		Tile t1 = new Tile(0,0);
//		t1.setBorderNorth(Border.OPEN);
//		t1.setBorderNorth(Border.UNKNOWN);
//		assertFalse(t1.getBorderNorth() == Border.UNKNOWN);
//	}
	
	@Test
    public void testRotate(){
        Tile t1 = new Tile(4,1);
        t1.setBorderNorth(Border.OPEN);
        t1.setBorderEast(Border.UNKNOWN);
        t1.setBorderSouth(Border.CLOSED);
        t1.setBorderWest(Border.CLOSED);
        Tile goal = new Tile(-1,4);
        goal.setBorderNorth(Border.UNKNOWN);
        goal.setBorderEast(Border.CLOSED);
        goal.setBorderSouth(Border.CLOSED);
        goal.setBorderWest(Border.OPEN);
       
       
        t1.rotate(Direction.LEFT);
       
        assertEquals(t1.getX(), goal.getX());
        assertEquals(t1.getY(), goal.getY());
        assertEquals(goal.getBorderNorth(), t1.getBorderNorth());
        assertEquals(goal.getBorderEast(), t1.getBorderEast());
        assertEquals(goal.getBorderWest(), t1.getBorderWest());
        assertEquals(goal.getBorderSouth(), t1.getBorderSouth());
    }
   
    @Test
    public void testRotate2(){
        Tile t1 = new Tile(5,-4);
        t1.setBorderNorth(Border.OPEN);
        t1.setBorderEast(Border.UNKNOWN);
        t1.setBorderSouth(Border.CLOSED);
        t1.setBorderWest(Border.CLOSED);
       
        Tile t2 = new Tile(5,-4);
        t2.setBorderNorth(Border.OPEN);
        t2.setBorderEast(Border.UNKNOWN);
        t2.setBorderSouth(Border.CLOSED);
        t2.setBorderWest(Border.CLOSED);
       
        t1.rotate(Direction.LEFT);
       
        t2.rotate(Direction.DOWN);
        t2.rotate(Direction.RIGHT);
       
        assertEquals(t1.getX(), t2.getX());
        assertEquals(t1.getY(), t2.getY());
        assertEquals(t1.getBorderNorth(), t2.getBorderNorth());
        assertEquals(t1.getBorderEast(), t2.getBorderEast());
        assertEquals(t1.getBorderWest(), t2.getBorderWest());
        assertEquals(t1.getBorderSouth(), t2.getBorderSouth());
    }
   
    @Test
    public void testRotate3(){
        Tile t1 = new Tile(4,1);
        t1.setBorderNorth(Border.OPEN);
        t1.setBorderEast(Border.UNKNOWN);
        t1.setBorderSouth(Border.CLOSED);
        t1.setBorderWest(Border.CLOSED);
        Tile goal = new Tile(-4,-1);
        goal.setBorderNorth(Border.CLOSED);
        goal.setBorderEast(Border.CLOSED);
        goal.setBorderSouth(Border.OPEN);
        goal.setBorderWest(Border.UNKNOWN);
       
       
        t1.rotate(Direction.DOWN);
       
        assertEquals(t1.getX(), goal.getX());
        assertEquals(t1.getY(), goal.getY());
        assertEquals(t1.getBorderNorth(), goal.getBorderNorth());
        assertEquals(t1.getBorderEast(), goal.getBorderEast());
        assertEquals(t1.getBorderWest(), goal.getBorderWest());
        assertEquals(t1.getBorderSouth(), goal.getBorderSouth());
    }
   
   
    @Test
    public void testRotate4(){
        Tile t1 = new Tile(-1,4);
        t1.setBorderNorth(Border.OPEN);
        t1.setBorderEast(Border.UNKNOWN);
        t1.setBorderSouth(Border.CLOSED);
        t1.setBorderWest(Border.CLOSED);
        Tile goal = new Tile(4,1);
        goal.setBorderNorth(Border.CLOSED);
        goal.setBorderEast(Border.OPEN);
        goal.setBorderSouth(Border.UNKNOWN);
        goal.setBorderWest(Border.CLOSED);
       
       
        t1.rotate(Direction.RIGHT);
       
        assertEquals(t1.getX(), goal.getX());
        assertEquals(t1.getY(), goal.getY());
        assertEquals(t1.getBorderNorth(), goal.getBorderNorth());
        assertEquals(t1.getBorderEast(), goal.getBorderEast());
        assertEquals(t1.getBorderWest(), goal.getBorderWest());
        assertEquals(t1.getBorderSouth(), goal.getBorderSouth());
    }
	

}
