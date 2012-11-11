package bluebot.simulator;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Tile;

/**
 * 
 * @author Dieter
 *
 */
public class TestSensors {
	private static double delta = 0;
	//4x4 Tile list.
	private static Tile Tile00 = new Tile(0,0);
	private static Tile Tile01 = new Tile(0,1);
	private static Tile Tile02 = new Tile(0,2);
	private static Tile Tile03 = new Tile(0,3);
	private static Tile Tile10 = new Tile(1,0);
	private static Tile Tile11 = new Tile(1,1);
	private static Tile Tile12 = new Tile(1,2);
	private static Tile Tile13 = new Tile(1,3);
	private static Tile Tile20 = new Tile(2,0);
	private static Tile Tile21 = new Tile(2,1);
	private static Tile Tile22 = new Tile(2,2);
	private static Tile Tile23 = new Tile(2,3);
	private static Tile Tile30 = new Tile(3,0);
	private static Tile Tile31 = new Tile(3,1);
	private static Tile Tile32 = new Tile(3,2);
	private static Tile Tile33 = new Tile(3,3);
	private static Tile[] tileList= new Tile[]{	
		Tile00, 
		Tile01,
		Tile02,
		Tile03,
		Tile10,
		Tile11,
		Tile12,
		Tile13,
		Tile20,
		Tile21,
		Tile22,
		Tile23,
		Tile30,
		Tile31,
		Tile32,
		Tile33};
	private static Sensors s = new Sensors(tileList);
	
	
	@Test
	public void Hash() {
		assertEquals(Math.pow(2, 0)*Math.pow(3, 0), s.hash(0, 0),delta);
		assertEquals(Math.pow(2, 0)*Math.pow(3, 1), s.hash(0, 1),delta);
		assertEquals(Math.pow(2, 0)*Math.pow(3, 2), s.hash(0, 2),delta);
		assertEquals(Math.pow(2, 0)*Math.pow(3, 3), s.hash(0, 3),delta);
		assertEquals(Math.pow(2, 1)*Math.pow(3, 0), s.hash(1, 0),delta);
		assertEquals(Math.pow(2, 1)*Math.pow(3, 1), s.hash(1, 1),delta);
		assertEquals(Math.pow(2, 1)*Math.pow(3, 2), s.hash(1, 2),delta);
		assertEquals(Math.pow(2, 1)*Math.pow(3, 3), s.hash(1, 3),delta);
		assertEquals(Math.pow(2, 2)*Math.pow(3, 0), s.hash(2, 0),delta);
		assertEquals(Math.pow(2, 2)*Math.pow(3, 1), s.hash(2, 1),delta);
		assertEquals(Math.pow(2, 2)*Math.pow(3, 2), s.hash(2, 2),delta);
		assertEquals(Math.pow(2, 2)*Math.pow(3, 3), s.hash(2, 3),delta);
		assertEquals(Math.pow(2, 3)*Math.pow(3, 0), s.hash(3, 0),delta);
		assertEquals(Math.pow(2, 3)*Math.pow(3, 1), s.hash(3, 1),delta);
		assertEquals(Math.pow(2, 3)*Math.pow(3, 2), s.hash(3, 2),delta);
		assertEquals(Math.pow(2, 3)*Math.pow(3, 3), s.hash(3, 3),delta);
	}
	
	@Test
	public void Test() {
		assertEquals(16,s.TileMap.size());
	}
	
	@Test
	public void getTileAt() {
		assertEquals(Tile00,s.getTileOnGridAt(0, 0));
		assertEquals(Tile01,s.getTileOnGridAt(0, 1));
		assertEquals(Tile02,s.getTileOnGridAt(0, 2));
		assertEquals(Tile10,s.getTileOnGridAt(1, 0));
		assertEquals(Tile11,s.getTileOnGridAt(1, 1));
		assertEquals(Tile12,s.getTileOnGridAt(1, 2));
		assertEquals(Tile13,s.getTileOnGridAt(1, 3));
		assertEquals(Tile20,s.getTileOnGridAt(2, 0));
		assertEquals(Tile21,s.getTileOnGridAt(2, 1));
		assertEquals(Tile22,s.getTileOnGridAt(2, 2));
		assertEquals(Tile23,s.getTileOnGridAt(2, 3));
		assertEquals(Tile30,s.getTileOnGridAt(3, 0));
		assertEquals(Tile31,s.getTileOnGridAt(3, 1));
		assertEquals(Tile32,s.getTileOnGridAt(3, 2));
		assertEquals(Tile33,s.getTileOnGridAt(3, 3));		
	}

}
