package bluebot.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import bluebot.graph.Border;
import bluebot.graph.Tile;

/**
 * Repersents an VirtualLightSensor object. 
 * 
 * It can make an image of the 'light map' if needed. 
 * 
 * 
 * @author Dieter, Michiel
 *
 */
public class VirtualLightSensor {
	
	//Public static Color objects. These can be used to experiment with the colors.
	public static final Color WHITE = Color.getHSBColor(1f, 0f, 1f);
	public static final Color VERY_LIGHT_BROWN = Color.getHSBColor(0.07f, 0.55f, 0.76f);
	public static final Color LIGHT_BROWN = Color.getHSBColor(0.07f, 0.55f, 0.73f);
	public static final Color LIGHT_BROWN_2 = Color.getHSBColor(0.07f, 0.5f, 0.71f);
	
	/**
	 * Standard white line color that will be used at construct.
	 */
	public static final Color STANDARD_WHITE_LINE_COLOR = WHITE;
	/**
	 * Standard white line light value.
	 */
	public static final int STANDARD_WHITE_LINE_LIGHTVALUE = calculateLightValuePercentage(STANDARD_WHITE_LINE_COLOR.getRGB());
	/**
	 * Standard empty space color that will be used at construct.
	 */
	public static final Color STANDARD_EMPTY_SPACE_COLOR = LIGHT_BROWN_2;
	/**
	 * Standard empty space light value.
	 */
	public static final int STANDARD_EMPTY_SPACE_LIGHTVALUE = calculateLightValuePercentage(LIGHT_BROWN_2.getRGB());
	/**
	 * The white line color currently used.
	 */
	private Color whiteLineColor;
	/**
	 * The empty space color currently used.
	 */
	private Color emptySpaceColor;
	/**
	 * Image that holds the generated image for this VirtualLightSensor.
	 */
	private BufferedImage img; 
	/**
	 * The Sensors object of this VirtualLightSensor. Holds all informations regarding tiles.
	 */
	private Sensors sensors;
	/**
	 * 
	 */
	private static final int barcodeLength = 16; //Pixels, lengte is in de richting dat de robot er over rijdt
	/**
	 * 
	 */
	private static final int barcodeWidth = Sensors.TILE_SIZE-4; // Pixels
	
	private static final int bitWidthResolution =  Sensors.TILE_SIZE/20;
	
	
	
	/**
	 * Basic constructor where only a Sensors object is needed.
	 * 
	 * Tiles will be found from the Sensors object
	 * @param sensors
	 */
	public VirtualLightSensor(Sensors sensors) {
		this.sensors = sensors;
		img = new BufferedImage(sensors.getMaxX()+1,sensors.getMaxY()+1,BufferedImage.TYPE_INT_RGB);
		setWhiteLineColor(STANDARD_WHITE_LINE_COLOR);
		setEmptySpaceColor(STANDARD_EMPTY_SPACE_COLOR);
		generateMap();
	}

	/**
	 * Generates the map (an image) with the current colors. 
	 * 
	 * 'protected' for testing purposes.
	 */
	protected void generateMap() {
		for(Tile t : sensors.getTilesList()){
			int barcode = t.getBarCode();
//			System.out.println(barcode);
			drawStandardTile(t.getX(), sensors.getMaxYOfGrid() - t.getY());
			if(barcode != -1){
//				System.out.println("not -1");
				Border northBorder = t.getBorderNorth();
				if(northBorder == Border.CLOSED){
//					System.out.println("North Border Closed");
					drawHorizontalBarcode(t.getX(), sensors.getMaxYOfGrid() - t.getY(), barcode);
				} else {
					drawVerticalBarcode(t.getX(), sensors.getMaxYOfGrid() - t.getY(), barcode);
				}
			}
		}
	}
	
	/**
	 * Returns the light value at the given coordinate. 
	 * 
	 * @param x
	 * @param y
	 * @return int
	 * 		An integer repersenting the light value.
	 * @throws IllegalArgumentException
	 * 		When the x or y coordinates are not valid |
	 * 		!sensors.isValid(x,y)
	 */
	public int getLightValuePercentage(int x, int y){
		if(sensors.isValid(x,y)){
		  int clr=  img.getRGB(x,y); 
		  int lightValue = calculateLightValuePercentage(clr);
//		  System.out.println("Lightsensor white-value: "+lightValue);
		  return lightValue;
		} else {
			throw new IllegalArgumentException("X or Y out of bounds");
		}
	}
	
	
	/**
	 * Returns the light value at the given coordinate. 
	 * 
	 * @param x
	 * @param y
	 * @return int
	 * 		An integer between 0 and 1023 representing the light value.
	 * @throws IllegalArgumentException
	 * 		When the x or y coordinates are not valid |
	 * 		!sensors.isValid(x,y)
	 */
	public int getLightValue(int x, int y){
		if(sensors.isValid(x,y)){
		  int clr=  img.getRGB(x,y); 
		  int lightValue = calculateLightValue(clr);
//		  System.out.println("Lightsensor white-value: "+lightValue);
		  return lightValue;
		} else {
			throw new IllegalArgumentException("X or Y out of bounds");
		}
	}

	/**
	 * Returns the lightValue calculated of the given 
	 * integer representing an RGB color.
	 * 
	 * @param clr
	 * @return An Integer ranging from 0 to 100 representing the light intensity (percentage).
	 */
	public static int calculateLightValuePercentage(int clr) {
		double divide = calculateRGBValue(clr);
		double percent = divide*100;
		int lightValue = (int) Math.round(percent);
//		System.out.println("Colors at ("+x+","+y+"): "+red+","+green+","+blue);
		return lightValue;
	}
	
	/**
	 * Returns the lightValue calculated of the given 
	 * integer representing an RGB color.
	 * 
	 * @param clr
	 * @return An Integer ranging from 0 to 1023 representing the light intensity.
	 */
	public static int calculateLightValue(int clr) {
		double divide = calculateRGBValue(clr);
		double value = divide*1023;
		int lightValue = (int) Math.round(value);
//		System.out.println("Colors at ("+x+","+y+"): "+red+","+green+","+blue);
		return lightValue;
	}

	/**
	 * The common calculations for making the light sensor values. 
	 * 
	 * @param clr
	 * @return
	 */
	private static double calculateRGBValue(int clr) {
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
		double sum = (double) (red+green+blue);
		double denominator = 255*3;
		double divide = sum/denominator;
		return divide;
	}
	
	/**
	 * Returns the image corresponding with this VirtualLightSensor.
	 * 
	 * @return
	 */
	public BufferedImage getFullImage(){
		return img;
	}
	
	/**
	 * Returns the color being used for drawing white lines.
	 * @return
	 */
	public Color getWhiteLineColor() {
		return whiteLineColor;
	}

	/**
	 * Sets the color to be used for drawing white lines.
	 * @param whiteLineColor
	 */
	public void setWhiteLineColor(Color whiteLineColor) {
		this.whiteLineColor = whiteLineColor;
	}

	/**
	 * Returns the color used for drawing empty spaces.
	 * @return
	 */
	public Color getEmptySpaceColor() {
		return emptySpaceColor;
	}

	/**
	 * Sets the color to be used for drawing empty spaces.
	 * @param emptySpaceColor
	 */
	public void setEmptySpaceColor(Color emptySpaceColor) {
		this.emptySpaceColor = emptySpaceColor;
	}

	//PRIVATE DRAWING METHODS
	private void drawFilledSquare(BufferedImage img, int x, int y, int width, Color color){
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		graphics.drawRect(x, y, width-1, width-1);
		//Added this to fill it completley with brown
		graphics.fillRect(x, y, width-1, width-1);
	}
	
	
	private void drawSquare(BufferedImage img, int x, int y, int width, Color color){
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		graphics.drawRect(x, y, width-1, width-1);
	}
	
	private void drawLine(BufferedImage img, int x1, int y1, int x2, int y2, Color color){
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		graphics.drawLine(x1, y1, x2, y2);
	}
	
	private void drawHorizontalLineWithFaders(BufferedImage img, int x, int y, int length){
		drawLine(img, x, y+1, x+length-1, y+1, WHITE);
		drawLine(img, x, y+2, x+length-1, y+2, WHITE);
		drawLine(img, x, y, x+length-1, y, LIGHT_BROWN_2);
		drawLine(img, x, y+3, x+length-1, y+3, LIGHT_BROWN_2);
	}
	
	private void drawVerticalLineWithFaders(BufferedImage img, int x, int y, int length){
		drawLine(img, x+1, y, x+1, y+length-1, WHITE);
		drawLine(img, x+2, y, x+2, y+length-1, WHITE);
		drawLine(img, x, y, x, y+length-1, LIGHT_BROWN_2);
		drawLine(img, x+3, y, x+3, y+length-1, LIGHT_BROWN_2);
	}
	
	
	//PUBLIC ABSOLUTE DRAWING METHODS (USING THE GLOBAL COORDINATE SYSTEM)
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile edges have white stripes. 
	 * 
	 * The colors of the resulting images depent on getWhiteLineColor() and getEmptySpaceColor();
	 */
	public void drawStandardTileAbsolute(int x, int y){
		//draw white square over the full distance
		drawSquare(img, x, y, sensors.getTileSize(), getWhiteLineColor());
		drawSquare(img, x+1, y+1, sensors.getTileSize()-2, getWhiteLineColor());
		//draws an 'inner' lightBrown2 square.
		drawFilledSquare(img, x+2, y+2, sensors.getTileSize()-4, getEmptySpaceColor());
	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has a white stripe vertically crossing it.
	 */
	public void drawVerticallyCrossedTileAbsolute(int x, int y){
		drawVerticalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y, sensors.getTileSize());
	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has a white stripe horizontally crossing it.
	 */
	public void drawHorizontallyCrossedTileAbsolute(int x, int y){
		drawHorizontalLineWithFaders(img, x, y+(sensors.getTileSize()/2)-2, sensors.getTileSize());

	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has L shaped white stripes, with the corner of the L to the top right.
	 */
	public void drawTopRightLTileAbsolute(int x, int y){
		drawHorizontalLineWithFaders(img, x, y+(sensors.getTileSize()/2)-2, (sensors.getTileSize()/2)+2);
		drawVerticalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y+(sensors.getTileSize()/2)-1, sensors.getTileSize()/2+1);
	
		//Correction
		drawLine(img, x+(sensors.getTileSize()/2)-2, y+(sensors.getTileSize()/2)-1, x+(sensors.getTileSize()/2)-2, y+(sensors.getTileSize()/2), WHITE);
	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has L shaped white stripes, with the corner of the L to the top left.
	 */
	public void drawTopLeftLTileAbsolute(int x, int y){
		drawHorizontalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y+(sensors.getTileSize()/2)-2, (sensors.getTileSize()/2)+2);
		drawVerticalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y+(sensors.getTileSize()/2)-1, sensors.getTileSize()/2+1);
	
		//Correction
		drawLine(img, x+(sensors.getTileSize()/2)+1, y+(sensors.getTileSize()/2)-1, x+(sensors.getTileSize()/2)+1, y+(sensors.getTileSize()/2), WHITE);
	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has L shaped white stripes, with the corner of the L to the bottom right.
	 */
	public void drawBottomRightLTileAbsolute(int x, int y){
		drawVerticalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y, sensors.getTileSize()/2+2);
		drawHorizontalLineWithFaders(img, x, y+(sensors.getTileSize()/2)-2, (sensors.getTileSize()/2)+1);
	
		//Correction
		drawLine(img, x+(sensors.getTileSize()/2)-1, y+(sensors.getTileSize()/2)-2, x+(sensors.getTileSize()/2), y+(sensors.getTileSize()/2)-2, WHITE);
	}
	
	/**
	 * Draws a tile with its top left corner at the given coordinates.
	 * The tile has L shaped white stripes, with the corner of the L to the bottom left.
	 */
	public void drawBottomLeftLTileAbsolute(int x, int y){
		drawVerticalLineWithFaders(img, x+(sensors.getTileSize()/2)-2, y, sensors.getTileSize()/2+2);
		drawHorizontalLineWithFaders(img, x+(sensors.getTileSize()/2)-1, y+(sensors.getTileSize()/2)-2, (sensors.getTileSize()/2)+1);
	
		//Correction
		drawLine(img, x+(sensors.getTileSize()/2)-1, y+(sensors.getTileSize()/2)-2, x+(sensors.getTileSize()/2), y+(sensors.getTileSize()/2)-2, WHITE);
	}
	
	
	
	
	//PUBLIC RELATIVE DRAWING METHODS (USING THE TILE COORDINATE SYSTEM)
	/**
	 * Draws a standard tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has white stripes at its borders.
	 */
	public void drawStandardTile(int x, int y){
		drawStandardTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has a white stripe vertically crossing it.
	 */
	public void drawVerticallyCrossedTile(int x, int y){
		drawVerticallyCrossedTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has a white stripe horizontally crossing it.
	 */
	public void drawHorizontallyCrossedTile(int x, int y){
		drawHorizontallyCrossedTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has L shaped white stripes, with the corner of the L to the top right.
	 */
	public void drawTopRightLTile(int x, int y){
		drawTopRightLTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has L shaped white stripes, with the corner of the L to the top right.
	 */
	public void drawTopLeftLTile(int x, int y){
		drawTopLeftLTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has L shaped white stripes, with the corner of the L to the bottom right.
	 */
	public void drawBottomRightLTile(int x, int y){
		drawBottomRightLTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	/**
	 * Draws a tile at the given coordinate in the system where each tile represents 1 unit on both x and y axis.
	 * This tile has L shaped white stripes, with the corner of the L to the bottom left.
	 */
	public void drawBottomLeftLTile(int x, int y){
		drawBottomLeftLTileAbsolute(x*sensors.getTileSize(), y*sensors.getTileSize());
	}
	
	//UTILITY METHODS
	
	/**
	 * Draws the standard panel with 4 tiles, all with crossing white lines.
	 */
	public void drawStandardPanel(int x, int y){
		for(int i=x; i<x+2; i++){
			for(int j=y; j<y+2; j++){
				drawStandardTile(i, j);
			}
		}
	}
	
	/**
	 * Draws a panel that has 1 white line having a corner to the top left.
	 */
	public void drawPanelCornerTopLeft(int x, int y){
		drawTopLeftLTile(x,y);
		drawVerticallyCrossedTile(x, y+1);
		drawHorizontallyCrossedTile(x+1, y);
	}
	
	/**
	 * Draws a panel that has 1 white line having a corner to the top right.
	 */
	public void drawPanelCornerTopRight(int x, int y){
		drawTopRightLTile(x+1,y);
		drawVerticallyCrossedTile(x+1, y+1);
		drawHorizontallyCrossedTile(x, y);
	}
	
	/**
	 * Draws a panel that has 1 white line having a corner to the bottom left.
	 */
	public void drawPanelCornerBottomLeft(int x, int y){
		drawBottomLeftLTile(x,y+1);
		drawVerticallyCrossedTile(x, y);
		drawHorizontallyCrossedTile(x+1, y+1);
	}
	
	/**
	 * Draws a panel that has 1 white line having a corner to the bottom right.
	 */
	public void drawPanelCornerBottomRight(int x, int y){
		drawBottomRightLTile(x+1,y+1);
		drawVerticallyCrossedTile(x+1, y);
		drawHorizontallyCrossedTile(x, y+1);
	}
	
	/* BARCODES */
	/**
	 * Basic drawer
	 */
	public void drawHorizontalBarAbsolute(int x, int y, Color color){
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		System.out.println("Drawing Horiz: "+x+" "+y+" "+color);
		graphics.fillRect(x, y, bitWidthResolution, barcodeWidth);
	}
	
	/**
	 * Basic drawer
	 */
	public void drawVerticalBarAbsolute(int x, int y, Color color){
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		System.out.println("Drawing Vertic: "+x+" "+y+" "+color);
		graphics.fillRect(x, y, barcodeWidth, bitWidthResolution);
	}
	
	/**
	 * Obvious
	 */
	public void drawHorizontalWhiteBarAbsolute(int x, int y){
		drawHorizontalBarAbsolute(x,y,WHITE);
	}
	
	/**
	 * Obvious
	 */
	public void drawVerticalWhiteBarAbsolute(int x, int y){
		drawVerticalBarAbsolute(x,y,WHITE);
	}
	
	/**
	 * Obvious
	 */
	public void drawHorizontalBlackBarAbsolute(int x, int y){
		drawHorizontalBarAbsolute(x,y,Color.black);
	}
	
	/**
	 * Obvious
	 */
	public void drawVerticalBlackBarAbsolute(int x, int y){
		drawVerticalBarAbsolute(x,y,Color.black);
	}
	
	/**
	 * x and y are the relative coordinates of the Tile that is drawn on.
	 * Orientation = 1 => Horizontal tile
	 * 			   = 0 => Vertical tile 
	 * This is the orientation of the tile that the barcode is located on. A horizontal tile means the robot can drive through it horizontally.
	 * 
	 * A barcode is built as follows: white-black-X-X-X-X-X-X-black-white, where XXXXXX is the functional code. This method draws those outer bars.
	 */
	public void drawEdgesOfBarcodeOnTile(int x, int y, int orientation){
		if(orientation==1){//HORIZONTAL
			int xOffset = (Sensors.TILE_SIZE-barcodeLength)/2;
			int yOffset = (Sensors.TILE_SIZE-barcodeWidth)/2;
			drawHorizontalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset, y*Sensors.TILE_SIZE+yOffset);
			//Leave space for 6 functional bars
			drawHorizontalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset+7*bitWidthResolution, y*Sensors.TILE_SIZE+yOffset);
			}
		else{//VERTICAL
			int xOffset = (Sensors.TILE_SIZE-barcodeWidth)/2;
			int yOffset = (Sensors.TILE_SIZE-barcodeLength)/2;
			drawVerticalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset, y*Sensors.TILE_SIZE+yOffset);
			//Leave space for 6 functional bars
			drawVerticalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset, y*Sensors.TILE_SIZE+yOffset+7*bitWidthResolution);
		}
	}
	
	/**
	 * 
	 * @param x, y			The relative coordinates of the tile.
	 * @param orientation	1 for horizontal and 0 for vertical tiles.
	 * @param bit			1 for a white bar and 0 for a black bar
	 * @param position		a number [0..5] indicating the place of the bit in the code. (Barcode-positions: /edge/-0-1-2-3-4-5-/edge/ )
	 */
	public void drawSingleBitOnTile(int x, int y, int orientation, char bit, int position){
		position *= bitWidthResolution;
		if(orientation==1){//HORIZONTAL
			int xOffset = (Sensors.TILE_SIZE-barcodeLength)/2;
			int yOffset = (Sensors.TILE_SIZE-barcodeWidth)/2;
			if(bit=='0'){
				drawHorizontalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset+2+position, y*Sensors.TILE_SIZE+yOffset);
			}
			else{//bit=='1'
				drawHorizontalWhiteBarAbsolute(x*Sensors.TILE_SIZE+xOffset+2+position, y*Sensors.TILE_SIZE+yOffset);
			}
		}
		else{//VERTICAL
			int xOffset = (Sensors.TILE_SIZE-barcodeWidth)/2;
			int yOffset = (Sensors.TILE_SIZE-barcodeLength)/2;
			if(bit=='0'){
				drawVerticalBlackBarAbsolute(x*Sensors.TILE_SIZE+xOffset, y*Sensors.TILE_SIZE+yOffset+2+position);
			}
			else{//bit=='1'
				drawVerticalWhiteBarAbsolute(x*Sensors.TILE_SIZE+xOffset, y*Sensors.TILE_SIZE+yOffset+2+position);
			}
			
		}
	}
	
	/**
	 * Obvious
	 */
	public int convertBinaryToInt(String binaryString){
		return Integer.parseInt(binaryString, 2);
	}
	
	/**
	 * Example: convertIntToBinary(19) returns "10011".
	 * Notice that all our barcodes consist of 6 bits and thus we need to add leading zeros after this conversion, where needed!
	 * This happens in the {@link drawBarcode()} method.
	 */
	public String convertIntToBinary(int number){
		return Integer.toBinaryString(number);
	}
	
	/**
	 * Draws the given binaryCode (for example: "1011") on the tile with relative coordinates x and y
	 * and orientation 1 (Horizontal) or 0(Vertical).
	 * Also adds leading zeros to the binary code, if needed, to make it 6 bits long.
	 */
	public void drawBarcode(int x, int y, String binaryCode, int orientation){
//		System.out.println("pre: "+binaryCode);
		int length = binaryCode.length();
		if(length<6){
			int nbLeadingZeros = 6-length;
			while(nbLeadingZeros>0){
				binaryCode = "0"+binaryCode;
				nbLeadingZeros--;
			}
		}
		length = binaryCode.length();
//		System.out.println("post: "+binaryCode);
		int index = length-1;
		drawEdgesOfBarcodeOnTile(x, y, orientation);
		while(index>=0){//index should always start at 5 and descend down to, including, 0 because we only have 6-bit codes.
			drawSingleBitOnTile(x, y, orientation, binaryCode.charAt(index), index);
			index--;
		}
	}
	
	/**
	 * Utility method to use an integer as code instead of a binary string.
	 */
	public void drawBarcode(int x, int y, int numberOfBarcode, int orientation){
		String binaryString = convertIntToBinary(numberOfBarcode);
		drawBarcode(x, y, binaryString, orientation);
	}
	
	/**
	 * Utility method to not worry about orientations being 0 or 1.
	 */
	public void drawHorizontalBarcode(int x, int y, String binaryCode){
		drawBarcode(x, y, binaryCode , 1);
	}
	
	/**
	 * Utility method to not worry about orientations being 0 or 1.
	 */
	public void drawVerticalBarcode(int x, int y, String binaryCode){
		drawBarcode(x, y, binaryCode , 0);
	}
	
	/**
	 * Utility method to not worry about orientations being 0 or 1.
	 */
	public void drawHorizontalBarcode(int x, int y, int numberOfBarcode){
		drawBarcode(x, y, numberOfBarcode , 1);
	}
	
	/**
	 * Utility method to not worry about orientations being 0 or 1.
	 */
	public void drawVerticalBarcode(int x, int y, int numberOfBarcode){
		drawBarcode(x, y, numberOfBarcode , 0);
	}

	
	
	
}
