package bluebot.maze;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.MazeMerger;


public class MazeMergerTest {

	@Test
	public void testMerge() {
		Tile t100 = new Tile(0, 0);
		t100.setBorderEast(Border.OPEN);
		t100.setBorderWest(Border.CLOSED);
		t100.setBorderNorth(Border.CLOSED);
		t100.setBorderSouth(Border.CLOSED);
		
		Tile t101 = new Tile(0, 1);
		t101.setBorderEast(Border.OPEN);
		t101.setBorderWest(Border.CLOSED);
		t101.setBorderNorth(Border.CLOSED);
		t101.setBorderSouth(Border.CLOSED);
		
		Tile t102 = new Tile(0, 2);
		t102.setBorderEast(Border.CLOSED);
		t102.setBorderWest(Border.CLOSED);
		t102.setBorderNorth(Border.CLOSED);
		t102.setBorderSouth(Border.CLOSED);
		
		Tile t110 = new Tile(1, 0);
		t110.setBorderEast(Border.OPEN);
		t110.setBorderWest(Border.OPEN);
		t110.setBorderNorth(Border.CLOSED);
		t110.setBorderSouth(Border.CLOSED);
		t110.setBarCode(5);
		
		Tile t111 = new Tile(1, 1);
		t111.setBorderEast(Border.OPEN);
		t111.setBorderWest(Border.OPEN);
		t111.setBorderNorth(Border.OPEN);
		t111.setBorderSouth(Border.CLOSED);
		t111.setBarCode(10);
		
		Tile t112 = new Tile(1, 2);
		t112.setBorderEast(Border.CLOSED);
		t112.setBorderWest(Border.CLOSED);
		t112.setBorderNorth(Border.CLOSED);
		t112.setBorderSouth(Border.OPEN);
		
		Tile t120 = new Tile(2, 0);
		t120.setBorderEast(Border.CLOSED);
		t120.setBorderWest(Border.OPEN);
		t120.setBorderNorth(Border.OPEN);
		t120.setBorderSouth(Border.CLOSED);
		
		Tile t121 = new Tile(2, 1);
		t121.setBorderEast(Border.CLOSED);
		t121.setBorderWest(Border.OPEN);
		t121.setBorderNorth(Border.OPEN);
		t121.setBorderSouth(Border.OPEN);
		
		Tile t122 = new Tile(2, 2);
		t122.setBorderEast(Border.CLOSED);
		t122.setBorderWest(Border.CLOSED);
		t122.setBorderNorth(Border.CLOSED);
		t122.setBorderSouth(Border.OPEN);
		
		//-------------------------------
		
		Tile ta = new Tile(-1, 2);
		ta.setBorderEast(Border.CLOSED);
		ta.setBorderWest(Border.CLOSED);
		ta.setBorderNorth(Border.CLOSED);
		ta.setBorderSouth(Border.OPEN);
		
		Tile tb = new Tile(0, 2);
		tb.setBorderEast(Border.CLOSED);
		tb.setBorderWest(Border.CLOSED);
		tb.setBorderNorth(Border.CLOSED);
		tb.setBorderSouth(Border.OPEN);
		
		Tile tc = new Tile(1, 2);
		tc.setBorderEast(Border.CLOSED);
		tc.setBorderWest(Border.CLOSED);
		tc.setBorderNorth(Border.CLOSED);
		tc.setBorderSouth(Border.CLOSED);
		
		Tile td = new Tile(-1, 1);
		td.setBorderEast(Border.CLOSED);
		td.setBorderWest(Border.CLOSED);
		td.setBorderNorth(Border.OPEN);
		td.setBorderSouth(Border.OPEN);
		td.setBarCode(5);
		
		Tile te = new Tile(0, 1);
		te.setBorderEast(Border.OPEN);
		te.setBorderWest(Border.CLOSED);
		te.setBorderNorth(Border.OPEN);
		te.setBorderSouth(Border.OPEN);
		te.setBarCode(10);
		
		Tile tf = new Tile(1, 1);
		tf.setBorderEast(Border.CLOSED);
		tf.setBorderWest(Border.OPEN);
		tf.setBorderNorth(Border.CLOSED);
		tf.setBorderSouth(Border.CLOSED);
		
		Tile tg = new Tile(-1, 0);
		tg.setBorderEast(Border.OPEN);
		tg.setBorderWest(Border.CLOSED);
		tg.setBorderNorth(Border.OPEN);
		tg.setBorderSouth(Border.CLOSED);
		
		Tile th = new Tile(0, 0);
		th.setBorderEast(Border.OPEN);
		th.setBorderWest(Border.OPEN);
		th.setBorderNorth(Border.OPEN);
		th.setBorderSouth(Border.CLOSED);
		
		Tile ti = new Tile(1, 0);
		ti.setBorderEast(Border.CLOSED);
		ti.setBorderWest(Border.OPEN);
		ti.setBorderNorth(Border.CLOSED);
		ti.setBorderSouth(Border.CLOSED);
		
		MazeMerger mm = new MazeMerger();
		mm.addTileFromSelf(t100);
		mm.addTileFromSelf(t101);
		mm.addTileFromSelf(t102);
		mm.addTileFromSelf(t110);
		mm.addTileFromSelf(t111);
		mm.addTileFromSelf(t112);
		mm.addTileFromSelf(t120);
		mm.addTileFromSelf(t121);
		mm.addTileFromSelf(t122);
		mm.addTileFromTeammate(ta);
		mm.addTileFromTeammate(tb);
		mm.addTileFromTeammate(tc);
		mm.addTileFromTeammate(td);
		mm.addTileFromTeammate(te);
		mm.addTileFromTeammate(tf);
		mm.addTileFromTeammate(tg);
		mm.addTileFromTeammate(th);
		mm.addTileFromTeammate(ti);
		boolean result = mm.tryToMerge();
//		System.out.println("*** "+td);
		System.out.println(mm.getTilesFromSelf());
		System.out.println(mm.getTilesFromTeammate());
		assertEquals(true, result);
		assertEquals(1, td.getX());
		assertEquals(0, td.getY());
		//Their (-1,1) maps to our (1,0)
	}
	
	@Test
	public void testRotate() {
		Tile ti = new Tile(0,1);
		ti.setBorderEast(Border.CLOSED);
		ti.setBorderWest(Border.OPEN);
		ti.setBorderNorth(Border.CLOSED);
		ti.setBorderSouth(Border.CLOSED);
		ti.rotate(Direction.LEFT);
		assertEquals(-1, ti.getX());
		assertEquals(0, ti.getY());
	}
	
	@Test
	public void testMergeSmall(){
		Tile tc = new Tile(0, 0);
		tc.setBorderEast(Border.OPEN);
		tc.setBorderWest(Border.CLOSED);
		tc.setBorderNorth(Border.OPEN);
		tc.setBorderSouth(Border.CLOSED);
		tc.setBarCode(5);
		
		Tile td = new Tile(0, 1);
		td.setBorderEast(Border.OPEN);
		td.setBorderWest(Border.CLOSED);
		td.setBorderNorth(Border.CLOSED);
		td.setBorderSouth(Border.OPEN);
		
		Tile te = new Tile(1, 0);
		te.setBorderEast(Border.CLOSED);
		te.setBorderWest(Border.OPEN);
		te.setBorderNorth(Border.OPEN);
		te.setBorderSouth(Border.CLOSED);
		
		Tile tf = new Tile(1, 1);
		tf.setBorderEast(Border.CLOSED);
		tf.setBorderWest(Border.OPEN);
		tf.setBorderNorth(Border.CLOSED);
		tf.setBorderSouth(Border.OPEN);
		tf.setBarCode(10);
		
		//---------------------------
		
		Tile t1 = new Tile(0, 0);
		t1.setBorderEast(Border.OPEN);
		t1.setBorderWest(Border.CLOSED);
		t1.setBorderNorth(Border.OPEN);
		t1.setBorderSouth(Border.CLOSED);
		
		
		Tile t2 = new Tile(0, 1);
		t2.setBorderEast(Border.OPEN);
		t2.setBorderWest(Border.CLOSED);
		t2.setBorderNorth(Border.CLOSED);
		t2.setBorderSouth(Border.OPEN);
		t2.setBarCode(5);
		
		Tile t3 = new Tile(1, 0);
		t3.setBorderEast(Border.CLOSED);
		t3.setBorderWest(Border.OPEN);
		t3.setBorderNorth(Border.OPEN);
		t3.setBorderSouth(Border.CLOSED);
		t3.setBarCode(10);
		
		Tile t4 = new Tile(1, 1);
		t4.setBorderEast(Border.CLOSED);
		t4.setBorderWest(Border.OPEN);
		t4.setBorderNorth(Border.CLOSED);
		t4.setBorderSouth(Border.OPEN);
		
		MazeMerger mm = new MazeMerger();
		mm.addTileFromSelf(td);
		mm.addTileFromSelf(tc);
		mm.addTileFromSelf(te);
		mm.addTileFromSelf(tf);
		mm.addTileFromTeammate(t1);
		mm.addTileFromTeammate(t2);
		mm.addTileFromTeammate(t3);
		mm.addTileFromTeammate(t4);
		mm.tryToMerge();
//		System.out.println(mm.getMergeRotationDirection());
//		t1.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		t2.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		t3.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		t4.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
		assertEquals(te.getX(), t1.getX());
		assertEquals(te.getY(), t1.getY());
		assertEquals(tc.getX(), t2.getX());
		assertEquals(tc.getY(), t2.getY());
		assertEquals(tf.getX(), t3.getX());
		assertEquals(tf.getY(), t3.getY());
		assertEquals(td.getX(), t4.getX());
		assertEquals(td.getY(), t4.getY());
	}
	
	@Test
	public void testMerge3(){
		Tile t1 = new Tile(-1, 0);
		Tile t2 = new Tile(0, 0);
		t2.setBarCode(3);
		Tile t3 = new Tile(1, 0);
		Tile t4 = new Tile(-1, 1);
		Tile t5 = new Tile(0, 1);
		t5.setBarCode(4);
		Tile t6 = new Tile(1, 1);
		
		Tile ta = new Tile(0, 0);
		Tile tz = new Tile(0, 1);
		tz.setBarCode(3);
		Tile te = new Tile(0, 2);
		Tile tr = new Tile(-1, 0);
		Tile tt = new Tile(-1, 1);
		tt.setBarCode(4);
		Tile ty = new Tile(-1, 2);
		
		MazeMerger mm = new MazeMerger();
		mm.addTileFromSelf(t1);
		mm.addTileFromSelf(t2);
		mm.addTileFromSelf(t3);
		mm.addTileFromSelf(t4);
		mm.addTileFromSelf(t5);
		mm.addTileFromSelf(t6);
		mm.addTileFromTeammate(ta);
		mm.addTileFromTeammate(tz);
		mm.addTileFromTeammate(te);
		mm.addTileFromTeammate(tr);
		mm.addTileFromTeammate(tt);
		mm.addTileFromTeammate(ty);
		mm.tryToMerge();
//		ta.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		tz.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		te.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		tr.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		tt.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
//		ty.transform(mm.getMergeRotationAngle(), mm.getMergeTranslationVector());
		assertEquals(ta.getX(), t1.getX());
		assertEquals(ta.getY(), t1.getY());
		assertEquals(tz.getX(), t2.getX());
		assertEquals(tz.getY(), t2.getY());
		assertEquals(te.getX(), t3.getX());
		assertEquals(te.getY(), t3.getY());
		assertEquals(tr.getX(), t4.getX());
		assertEquals(tr.getY(), t4.getY());
		assertEquals(tt.getX(), t5.getX());
		assertEquals(tt.getY(), t5.getY());
		assertEquals(ty.getX(), t6.getX());
		assertEquals(ty.getY(), t6.getY());
	}
	
	@Test
	public void testMerge4(){
		MazeReader reader = new MazeReader();
		Graph g1 = reader.parseMaze("testMaze26.txt");
		List<Tile> tiles1 = g1.getVerticies();
		Graph g2 = reader.parseMaze("testMaze26gedraaid.txt");
		List<Tile> tiles2 = g2.getVerticies();
		MazeMerger mm = new MazeMerger();
		for(Tile t : tiles1){
			mm.addTileFromSelf(t);
		}
		for(Tile t : tiles2){
			mm.addTileFromTeammate(t);
		}
		
		mm.tryToMerge();
		
		for(Tile t1 : tiles1){
			for(Tile t2 : tiles2){
				assertEquals(t1.getX(), t2.getX());
				assertEquals(t1.getY(), t2.getY());
			}
		}
		
	}
	
}

