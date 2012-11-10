package bluebot.simulator;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import bluebot.graph.Tile;

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
	private static final String destinationPath = "src\\newTest."+ fileType;
	
	/**
	 * Draws the image to a file for easy debugging.
	 */
	@Test
	public void drawAndPrintImage(){
//		s.getLightSensor().setEmptySpaceColor(VirtualLightSensor.VERY_LIGHT_BROWN);
//		s.getLightSensor().generateMap();
		BufferedImage myImg = s.getLightSensor().getFullImage();
		try {
			ImageIO.write(myImg, fileType, new File(destinationPath));
		} catch (IOException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void getLightValue() {
		int white = VirtualLightSensor.WHITE.getRGB();
		int whiteLineLightValue = s.getLightSensor().calculateLightValue(white);
		int other = VirtualLightSensor.LIGHT_BROWN.getRGB();
		int emptySpaceLightValue = s.getLightSensor().calculateLightValue(other);

		//Check some white lines
		assertEquals(whiteLineLightValue,s.getLightValue(0, 0));
		assertEquals(whiteLineLightValue,s.getLightValue(0, 1));
		assertEquals(whiteLineLightValue,s.getLightValue(1, 0));
		assertEquals(whiteLineLightValue,s.getLightValue(1, 1));
		assertEquals(whiteLineLightValue,s.getLightValue(1, 2));

		//Check some Non-white lines
		assertEquals(emptySpaceLightValue,s.getLightValue(37, 37));
		assertEquals(emptySpaceLightValue,s.getLightValue(30, 20));
		assertEquals(emptySpaceLightValue,s.getLightValue(10, 35));
		assertEquals(emptySpaceLightValue,s.getLightValue(5, 5));
		assertEquals(emptySpaceLightValue,s.getLightValue(20, 20));
	}
}
