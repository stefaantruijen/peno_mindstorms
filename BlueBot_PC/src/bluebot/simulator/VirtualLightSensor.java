package bluebot.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
	private static final Color STANDARD_WHITE_LINE_COLOR = WHITE;
	/**
	 * Standard empty space color that will be used at construct.
	 */
	private static final Color STANDARD_EMPTY_SPACE_COLOR = LIGHT_BROWN_2;
	
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
	private static BufferedImage img; 
	/**
	 * The Sensors object of this VirtualLightSensor. Holds all informations regarding tiles.
	 */
	private Sensors sensors;
   
	/**
	 * Basic constructor where only a Sensors object is needed.
	 * 
	 * Tiles will be found from the Sensors object
	 * @param sensors
	 */
	public VirtualLightSensor(Sensors sensors) {
		this.sensors = sensors;
		img = new BufferedImage(sensors.getMaxX(),sensors.getMaxY(),BufferedImage.TYPE_INT_RGB);
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
			//TODO make drawing dependent on type of Tile.
			drawStandardTile(t.getX(), t.getY());
		}
	}
	
	/**
	 * Returns the light value at the given coordinate. 
	 * 
	 * TODO: is this conform the real nxt?
	 * 
	 * @param x
	 * @param y
	 * @return int
	 * 		An integer repersenting the light value.
	 */
	public static int getLightValue(int x, int y){
		  int clr=  img.getRGB(x,y); 
		  int lightValue = calculateLightValue(clr);
//		  System.out.println("Lightsensor white-value: "+lightValue);
		  
		  return lightValue;
	}

	/**
	 * Returns a certain lightValue calculated of the given 
	 * integer representing an RGB color.
	 * 
	 * TODO: is this conform the real nxt?
	 * 
	 * @param clr
	 * @return
	 */
	public static int calculateLightValue(int clr) {
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
		double sum = (double) (red+green+blue);
		double denominator = 255*3;
		double divide = sum/denominator;
		double percent = divide*100;
		int lightValue = (int) Math.round(percent);
//		System.out.println("Colors at ("+x+","+y+"): "+red+","+green+","+blue);
		return lightValue;
	}
	
	/**
	 * Returns the image corresponding with this VirtualLightSensor.
	 * 
	 * @return
	 */
	public BufferedImage getFullImage(){
		return img;
	}
	
	public Color getWhiteLineColor() {
		return whiteLineColor;
	}

	public void setWhiteLineColor(Color whiteLineColor) {
		this.whiteLineColor = whiteLineColor;
	}

	public Color getEmptySpaceColor() {
		return emptySpaceColor;
	}

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
	
	
	
	
}
