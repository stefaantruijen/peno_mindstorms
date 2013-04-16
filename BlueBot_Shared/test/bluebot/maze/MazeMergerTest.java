package bluebot.maze;

import static org.junit.Assert.*;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Tile;
import bluebot.maze.MazeMerger;


public class MazeMergerTest {

	@Test
	public void testAddTileFromTeammate() {
		/**
		 * OWN TILES
		 */
		//Corner at 0,0
		Tile t00 = new Tile(0,0);
		t00.setAllBordersOpen(true);
		t00.setBorderWest(Border.CLOSED);
		t00.setBorderSouth(Border.CLOSED);
		//Straight with barcode at 0,1
		Tile t01 = new Tile(0,1);
		t01.setAllBordersOpen(true);
		t01.setBorderEast(Border.CLOSED);
		t01.setBorderWest(Border.CLOSED);
		t01.setBarCode(10);
		//Straight with barcode at 0,2
		Tile t02 = new Tile(0,2);
		t02.setAllBordersOpen(true);
		t02.setBorderEast(Border.CLOSED);
		t02.setBorderWest(Border.CLOSED);
		t02.setBarCode(6);
		//Corner at 0,3
		Tile t03 = new Tile(0,3);
		t03.setAllBordersOpen(true);
		t03.setBorderWest(Border.CLOSED);
		t03.setBorderNorth(Border.CLOSED);
		//Corner at 1,3
		Tile t13 = new Tile(1,3);
		t13.setAllBordersOpen(true);
		t13.setBorderEast(Border.CLOSED);
		t13.setBorderNorth(Border.CLOSED);
		//Straight at 1,2
		Tile t12 = new Tile(1,2);
		t12.setAllBordersOpen(true);
		t12.setBorderEast(Border.CLOSED);
		t12.setBorderWest(Border.CLOSED);
		//Straight with barcode at 1,1
		Tile t11 = new Tile(1,1);
		t11.setAllBordersOpen(true);
		t11.setBorderEast(Border.CLOSED);
		t11.setBorderWest(Border.CLOSED);
		t11.setBarCode(8);
		//Corner at 1,0
		Tile t10 = new Tile(1,3);
		t10.setAllBordersOpen(true);
		t10.setBorderEast(Border.CLOSED);
		t10.setBorderSouth(Border.CLOSED);
		/**
		 * TILES FROM TEAMMATE
		 */
		//Corner at 0,0
		Tile tt00 = new Tile(0,0);
		tt00.setAllBordersOpen(true);
		tt00.setBorderWest(Border.CLOSED);
		tt00.setBorderSouth(Border.CLOSED);
		//Straight with barcode at 0,1
		Tile tt01 = new Tile(0,1);
		tt01.setAllBordersOpen(true);
		tt01.setBorderEast(Border.CLOSED);
		tt01.setBorderWest(Border.CLOSED);
		//Straight with barcode at 0,2
		Tile tt02 = new Tile(0,2);
		tt02.setAllBordersOpen(true);
		tt02.setBorderEast(Border.CLOSED);
		tt02.setBorderWest(Border.CLOSED);
		tt02.setBarCode(8);
		//Corner at 0,3
		Tile tt03 = new Tile(0,3);
		tt03.setAllBordersOpen(true);
		tt03.setBorderWest(Border.CLOSED);
		tt03.setBorderNorth(Border.CLOSED);
		//Corner at 1,3
		Tile tt13 = new Tile(1,3);
		tt13.setAllBordersOpen(true);
		tt13.setBorderEast(Border.CLOSED);
		tt13.setBorderNorth(Border.CLOSED);
		//Straight at 1,2
		Tile tt12 = new Tile(1,2);
		tt12.setAllBordersOpen(true);
		tt12.setBorderEast(Border.CLOSED);
		tt12.setBorderWest(Border.CLOSED);
		tt12.setBarCode(10);
		//Straight with barcode at 1,1
		Tile tt11 = new Tile(1,1);
		tt11.setAllBordersOpen(true);
		tt11.setBorderEast(Border.CLOSED);
		tt11.setBorderWest(Border.CLOSED);
		tt11.setBarCode(6);
		//Corner at 1,0
		Tile tt10 = new Tile(1,3);
		tt10.setAllBordersOpen(true);
		tt10.setBorderEast(Border.CLOSED);
		tt10.setBorderSouth(Border.CLOSED);
		MazeMerger MM = new MazeMerger();
		MM.addTileFromSelf(t01);
		MM.addTileFromSelf(t02);
		MM.addTileFromSelf(t11);
		MM.addTileFromTeammate(tt02);
		MM.addTileFromTeammate(tt12);
		MM.addTileFromTeammate(tt11);
		assertEquals(3, MM.getTilesFromSelf().size());
		assertEquals(3, MM.getTilesFromTeammate().size());
		
		MM.searchForMatches();
		assertEquals(2, MM.getNbOfMatchesFound());
		
		MM.calculateRotation();
		
		
		
	}

}
