package bluebot.simulator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Tile;

/**
 * 
 * @author Dieter
 *
 */
public class TestVirtualSonar {
	private static double delta = 0;
	//4x4 Tile list.
	private static Tile[] tileList= new Tile[]{
		new Tile(0,0),
		new Tile(0,1),
		new Tile(0,2),
		new Tile(0,3),
		new Tile(1,0),
		new Tile(1,1),
		new Tile(1,2),
		new Tile(1,3),
		new Tile(2,0),
		new Tile(2,1),
		new Tile(2,2),
		new Tile(2,3),
		new Tile(3,0),
		new Tile(3,1),
		new Tile(3,2),
		new Tile(3,3)
		};
	private static Sensors s = new Sensors(tileList);
	
	
	@Test
	public void getDistanceFromTo() {
		assertEquals(50, VirtualSonar.getDistanceFromTo(0, 0, 50, 0),delta);
		assertEquals(100, VirtualSonar.getDistanceFromTo(0, 0, 0, 100),delta);
		assertEquals(50, VirtualSonar.getDistanceFromTo(50, 0, 0, 0),delta);
		assertEquals(100, VirtualSonar.getDistanceFromTo(0, 100, 0, 0),delta);
		assertEquals(Math.sqrt(200), VirtualSonar.getDistanceFromTo(0, 0, 10, 10),delta);
		assertEquals(Math.sqrt(200), VirtualSonar.getDistanceFromTo(10, 10, 0, 0),delta);
	}
	
	@Test
	public void compareWithRespectTo(){
		int X = 0;
		int Y = 0;
		int[] c1 = new int[]{2,2};
		int[] c2 = new int[]{4,4};
		int[] c3 = new int[]{5,5};
		int[] c4 = new int[]{1,2};
		int[] c5 = new int[]{20,20};
		int[] c6 = new int[]{20,20};
		int[] c7 = new int[]{1,4};
		int[] c8 = new int[]{4,1};
		assertEquals(-1, VirtualSonar.compareWithRespectTo(c1, c2,  X, Y));
		assertEquals(1, VirtualSonar.compareWithRespectTo(c3, c4,  X, Y));
		assertEquals(0, VirtualSonar.compareWithRespectTo(c5, c6,  X, Y));
		assertEquals(0, VirtualSonar.compareWithRespectTo(c7, c8,  X, Y));
	}
	
	@Test
	public void getMinimumWithRespectTo(){
		ArrayList<int[]> testArr = new ArrayList<int[]>();
		int[] c1 = new int[]{2,2};
		int[] c2 = new int[]{4,4};
		int[] c3 = new int[]{5,5};
		int[] c4 = new int[]{1,2}; //<< should be minimum element
		int[] c5 = new int[]{20,20};
		int[] c6 = new int[]{20,20};
		int[] c7 = new int[]{1,4};
		int[] c8 = new int[]{4,1};
		int[] c9 = new int[]{-800,-4};
		int[] c10 = new int[]{-5,4};
		testArr.add(c1);
		testArr.add(c2);
		testArr.add(c3);
		testArr.add(c4);
		testArr.add(c5);
		testArr.add(c6);
		testArr.add(c7);
		testArr.add(c8);
		testArr.add(c9);
		testArr.add(c10);
		int X = 0;
		int Y = 0;
		int[] min = VirtualSonar.getMinimumWithRespectTo(testArr, X, Y);
		assertEquals(c4[0], min[0]);
		assertEquals(c4[1], min[1]);
	}
	
	
	@Test
	public void sortByDistanceTo(){
		ArrayList<int[]> testArr = new ArrayList<int[]>();
		int[] c1 = new int[]{2,2};
		int[] c2 = new int[]{4,4};
		int[] c3 = new int[]{5,5};
		int[] c4 = new int[]{1,2};
		int[] c5 = new int[]{20,20};
		int[] c6 = new int[]{20,20};
		int[] c7 = new int[]{1,4};
		int[] c8 = new int[]{4,1};
		int[] c9 = new int[]{-800,-4};
		int[] c10 = new int[]{-5,4};
		testArr.add(c1);
		testArr.add(c2);
		testArr.add(c3);
		testArr.add(c4);
		testArr.add(c5);
		testArr.add(c6);
		testArr.add(c7);
		testArr.add(c8);
		testArr.add(c9);
		testArr.add(c10);
		int testArrSize = testArr.size();
		int X = 0;
		int Y = 0;
		ArrayList<int[]> result = VirtualSonar.sortByDistanceTo(testArr, X, Y);
		int resultArrSize = result.size();
		assertEquals(testArrSize, resultArrSize);
		for(int i=1; i<result.size(); i++){
			if(VirtualSonar.compareWithRespectTo(result.get(i-1), result.get(i), X, Y) > 0){
				assertFalse(true);
			}
		}
	}
	
	@Test
	public void calculateIntercepts(){
		VirtualSonar sonar = s.getSonar();
		
		//Test looking directly north (0°)
		ArrayList<int[]> intercepts1 = sonar.calculateIntersections(2, 3, 0);
		assertEquals(4,intercepts1.size());
		assertEquals(39,intercepts1.get(0)[1]);
		assertEquals(79,intercepts1.get(1)[1]);
		assertEquals(119,intercepts1.get(2)[1]);
		assertEquals(159,intercepts1.get(3)[1]);
		
		//Test looking directly south (180°)
		ArrayList<int[]> intercepts2 = sonar.calculateIntersections(50, 150, 180);
		assertEquals(4,intercepts2.size());
		assertEquals(120,intercepts2.get(0)[1]);
		assertEquals(80,intercepts2.get(1)[1]);
		assertEquals(40,intercepts2.get(2)[1]);
		assertEquals(0,intercepts2.get(3)[1]);
		
		//Test looking directly east (90°)
		ArrayList<int[]> intercepts3 = sonar.calculateIntersections(1, 1, 90);
		assertEquals(4,intercepts3.size());
		assertEquals(39,intercepts3.get(0)[0]);
		assertEquals(79,intercepts3.get(1)[0]);
		assertEquals(119,intercepts3.get(2)[0]);
		assertEquals(159,intercepts3.get(3)[0]);
		
		
		//Test looking directly west (270°)
		ArrayList<int[]> intercepts4 = sonar.calculateIntersections(159, 159, 270);
		assertEquals(4,intercepts4.size());
		assertEquals(120,intercepts4.get(0)[0]);
		assertEquals(80,intercepts4.get(1)[0]);
		assertEquals(40,intercepts4.get(2)[0]);
		assertEquals(0,intercepts4.get(3)[0]);
		
	}
	
	@Test
	public void calculateIntercepts2(){
		VirtualSonar sonar = s.getSonar();
		
		//Test looking directly north-east (45°)
		ArrayList<int[]> intercepts1 = sonar.calculateIntersections(90, 100, 45);
		assertEquals(3,intercepts1.size());
		// intersects should be : (X) (119, 129) and (Y) (109, 119), (149, 159) in that order
		assertEquals(119,intercepts1.get(0)[0]);
		assertEquals(129,intercepts1.get(0)[1]);
		assertEquals(109,intercepts1.get(1)[0]);
		assertEquals(119,intercepts1.get(1)[1]);
		assertEquals(149,intercepts1.get(2)[0]);
		assertEquals(159,intercepts1.get(2)[1]);
		
		//Test looking directly almost north (10°)
		ArrayList<int[]> intercepts2 = sonar.calculateIntersections(0, 2, 7);
		assertEquals(4,intercepts2.size());
		// exact solution in order:
		/*
		 * 4.5430288, 39
		 * 9,4544112, 79
		 * 14,365794, 119
		 * 19,277176, 159
		 */
		assertEquals(5,intercepts2.get(0)[0]);
		assertEquals(39,intercepts2.get(0)[1]);
		assertEquals(9,intercepts2.get(1)[0]);
		assertEquals(79,intercepts2.get(1)[1]);
		assertEquals(14,intercepts2.get(2)[0]);
		assertEquals(119,intercepts2.get(2)[1]);
		assertEquals(19,intercepts2.get(3)[0]);
		assertEquals(159,intercepts2.get(3)[1]);

		//TODO: some more testing in all directions...
	}
	
	@Test
	public void getSonarValue(){
		VirtualSonar sonar = s.getSonar();
		
		//Test looking directly north (0°)
		s.getTileOnGridAt(0, 0).setBorderNorth(Border.CLOSED);
		assertEquals(39-3,sonar.getSonarValue(2, 3, 0));
		s.getTileOnGridAt(0, 0).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 1).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 1).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 2).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 2).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 3).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 3).setBorderNorth(Border.CLOSED);
		assertEquals(159-3,sonar.getSonarValue(2, 3, 0));
//		
//		//Test looking directly south (180°)
//		ArrayList<int[]> intercepts2 = sonar.calculateIntercepts(50, 150, 180);
//		assertEquals(4,intercepts2.size());
//		assertEquals(120,intercepts2.get(0)[1]);
//		assertEquals(80,intercepts2.get(1)[1]);
//		assertEquals(40,intercepts2.get(2)[1]);
//		assertEquals(0,intercepts2.get(3)[1]);
//		
//		//Test looking directly east (90°)
//		ArrayList<int[]> intercepts3 = sonar.calculateIntercepts(1, 1, 90);
//		assertEquals(4,intercepts3.size());
//		assertEquals(39,intercepts3.get(0)[0]);
//		assertEquals(79,intercepts3.get(1)[0]);
//		assertEquals(119,intercepts3.get(2)[0]);
//		assertEquals(159,intercepts3.get(3)[0]);
//		
//		
//		//Test looking directly west (270°)
//		ArrayList<int[]> intercepts4 = sonar.calculateIntercepts(159, 159, 270);
//		assertEquals(4,intercepts4.size());
//		assertEquals(120,intercepts4.get(0)[0]);
//		assertEquals(80,intercepts4.get(1)[0]);
//		assertEquals(40,intercepts4.get(2)[0]);
//		assertEquals(0,intercepts4.get(3)[0]);
	}
	

	@Test
	public void getSonarValue_NoWall(){
		VirtualSonar sonar = s.getSonar();
		
		//Test looking directly north (0°)
		s.getTileOnGridAt(0, 0).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 1).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 1).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 2).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 2).setBorderNorth(Border.OPEN);
		s.getTileOnGridAt(0, 3).setBorderSouth(Border.OPEN);
		s.getTileOnGridAt(0, 3).setBorderNorth(Border.OPEN);
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(2, 3, 0));
		
		//Test looking directly east (90°)
		s.getTileOnGridAt(0, 3).setBorderEast(Border.OPEN);
		s.getTileOnGridAt(1, 3).setBorderWest(Border.OPEN);
		s.getTileOnGridAt(1, 3).setBorderEast(Border.OPEN);
		s.getTileOnGridAt(2, 3).setBorderWest(Border.OPEN);
		s.getTileOnGridAt(2, 3).setBorderEast(Border.OPEN);
		s.getTileOnGridAt(3, 3).setBorderWest(Border.OPEN);
		s.getTileOnGridAt(3, 3).setBorderEast(Border.OPEN);
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(2, 150, 90));
		
		//Test with random values between the borders and random angles
		for(Tile t : s.TileMap.values()){
			t.setAllBordersOpen(true);
		}
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(2, 150, 25));
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(2, 150, 30));
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(12, 75, 99));
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(159, 0, 359));
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(100, 11, 280));
		assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(159, 0, 315));
		
		Random rand = new Random();
		int amountOfRandomTests = 100;
		for(int i = 0; i < amountOfRandomTests; i++){
			int randX = rand.nextInt(s.getMaxX()+1);
			int randY = rand.nextInt(s.getMaxY()+1);
			float randAngle =(float)rand.nextInt(360);
			assertEquals(Integer.MAX_VALUE,sonar.getSonarValue(randX, randY, randAngle));
		}		
	}
}
