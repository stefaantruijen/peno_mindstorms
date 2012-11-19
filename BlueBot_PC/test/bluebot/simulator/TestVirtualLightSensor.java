package bluebot.simulator;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import bluebot.graph.Border;
import bluebot.graph.Tile;

/**
 * 
 * @author Dieter
 *
 */
public class TestVirtualLightSensor {
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
	private static final String fileType = "png";
//	private static final String originalPath = "src\\brown-bg-original."+ fileType;
	private static final String destinationPath = "src\\newTestWithBarcode."+ fileType;
	
	/**
	 * Draws the image to a file for easy debugging.
	 * 
	 * You can edit destinationPath or fileType to your liking
	 * With this you can very easily experiment with different colors for the image.
	 */
	@Test
	public void drawAndPrintImage(){
		Tile t11 = new Tile(1,1);
		t11.setAllBordersOpen(true);
//		t11.setBorderNorth(Border.CLOSED);
//		t11.setBorderSouth(Border.CLOSED);
		t11.setBorderEast(Border.CLOSED);
		t11.setBorderWest(Border.CLOSED);
//		t11.setBarCode(19);
		t11.setBarCode(5);
		Tile[] tileList2= new Tile[]{
			new Tile(0,0),
			new Tile(0,1),
			new Tile(0,2),
			new Tile(0,3),
			new Tile(1,0),
			t11,
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
		
		Sensors s2 = new Sensors(tileList2);

//		s.getLightSensor().setEmptySpaceColor(VirtualLightSensor.VERY_LIGHT_BROWN);
//		s.getLightSensor().generateMap();
		BufferedImage myImg = s2.getLightSensor().getFullImage();
		try {
			ImageIO.write(myImg, fileType, new File(destinationPath));
		} catch (IOException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	/**
	 * Tests getting the light values at different positions of a standard image.
	 */
	@Test
	public void getLightValue() {
		int white = VirtualLightSensor.WHITE.getRGB();
		int whiteLineLightValue = s.getLightSensor().calculateLightValuePercentage(white);
		int other = VirtualLightSensor.LIGHT_BROWN.getRGB();
		int emptySpaceLightValue = s.getLightSensor().calculateLightValuePercentage(other);

		//Check some white lines
		assertEquals(whiteLineLightValue,s.getLightValuePercentage(0, 0));
		assertEquals(whiteLineLightValue,s.getLightValuePercentage(0, 1));
		assertEquals(whiteLineLightValue,s.getLightValuePercentage(1, 0));
		assertEquals(whiteLineLightValue,s.getLightValuePercentage(1, 1));
		assertEquals(whiteLineLightValue,s.getLightValuePercentage(1, 2));

		//Check some Non-white lines
		assertEquals(emptySpaceLightValue,s.getLightValuePercentage(37, 37));
		assertEquals(emptySpaceLightValue,s.getLightValuePercentage(30, 20));
		assertEquals(emptySpaceLightValue,s.getLightValuePercentage(10, 35));
		assertEquals(emptySpaceLightValue,s.getLightValuePercentage(5, 5));
		assertEquals(emptySpaceLightValue,s.getLightValuePercentage(20, 20));
	}
	
	/**
	 * Tests if all values of a standard image respect 
	 * 	the constraint of being a value between 0 and 100.
	 */
	@Test
	public void getLightValue_percent() {
		for(int x=0; x <= s.getMaxX(); x++){
			for(int y=0; y <= s.getMaxX(); y++){
				assertTrue(s.getLightValuePercentage(x,y) <=100 && s.getLightValuePercentage(x,y) >=0);
			}
		}
	}
}
