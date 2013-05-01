package bluebot.graph;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bluebot.maze.TileBuilder;
import bluebot.util.Barcode;



/**
 * 
 * @author Ruben Feyen
 */
public class Tile implements Comparable<Tile> {
	
	/**    The resolution of a tile (in pixels)    */
	public static final int RESOLUTION = 64;
	/**    The size of a tile (in mm)    */
	public static final float SIZE = 400F;
	
	private int barcode = -1;
	private byte borders;
	private int item;
	private int x, y;
	private boolean hasItem = false;
	private boolean isSeesaw = false;
	private boolean startPosition = false;
	private int startOrientation = -1;
	private int startPlayerId = -1;

	private double distanceFromStart;

	private Tile parent;

	private double heuristicDistanceFromGoal;

	private Direction orientationToReach;
	
	public Tile(final int x, final int y) {
		this.x = x;
		this.y = y;
		this.distanceFromStart = Double.MAX_VALUE;
		
	}

	
	
	public peno.htttp.Tile export() {
		return new peno.htttp.Tile(getX(), getY(),
				TileBuilder.fromTileToString(this));
	}
	
	public boolean hasItem() {
		return hasItem;
	}

	/**
	 * Set that this tile has an item on it.
	 * 
	 * @param hasItem
	 * @throws IllegalStateException if this tile is already a seesaw. A tile can't be located on a seesaw tile.
	 */
	public void setHasItem(boolean hasItem) {
		if(this.isSeesaw()){
			throw new IllegalStateException("Tile is already a seesaw. Can not have an item too.");
		}
		this.hasItem = hasItem;
	}

	/**
	 * Check if this tile is a seesaw tile.
	 * 
	 * @return
	 */
	public boolean isSeesaw() {
		return isSeesaw;
	}
	
	/**
	 * Check if this tile is a startposition.
	 * @return
	 */
	public boolean isStartPosition() {
		return startPosition;
	}
	/**
	 * Set this tile as a startPosition.
	 * 
	 * @param startPosition
	 */
	public void setStartPosition(boolean startPosition) {
		
		this.startPosition = startPosition;
	}
	/**
	 * Get the start orientation for a player on this tile.
	 * 
	 * @return NULL if this tile has not a valid starting orientation
	 * 			Orientation otherwise
	 * 
	 */
	public Orientation getStartOrientation() {
		if(this.startOrientation==-1){
			return null;
		}else{
			String orient = "N";
			if(startOrientation == 1){
				orient = "E";
			}else if(startOrientation == 2){
				orient = "S";
			}else if(startOrientation == 3){
				orient = "W";
			}
			try{
				return Orientation.getOrientation(orient);
			}catch(Exception e){
				return null;
			}
		}
		
	}

	public void setStartOrientation(int startOrientation) {
		this.startOrientation = startOrientation;
	}
	/**
	 * If this tile has not a valid playerId it returns -1. Else it will return a playerId between 1-4.
	 * @return
	 */
	public int getStartPlayerId() {
		return startPlayerId;
	}
	/**
	 * Set a valid playerId between 1-4.
	 * 
	 * @param startPlayerId
	 * @throws IllegalArgumentException if the given playerId is not a valid id.
	 */
	public void setStartPlayerId(int startPlayerId) {
		if(startPlayerId < 1 || startPlayerId > 4){
			throw new IllegalArgumentException("Wrong playerId, needs to be between 1-4");
		}else{
			this.startPlayerId = startPlayerId;
		}
		
	}
	
	/**
	 * Set this tile as seesaw.
	 * 
	 * @param isSeesaw
	 * @throws IllegalStateException if this tile already has an item
	 */
	public void setSeesaw(boolean isSeesaw) {
		if(this.hasItem()){
			throw new IllegalStateException("Tile already has an item. Can not be a seesaw too.");
		}
		this.isSeesaw = isSeesaw;
	}



	public void copyBorders(final Tile other) {
		this.borders = other.borders;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Tile) {
			final Tile tile = (Tile)obj;
			return ((tile.x == x) && (tile.y == y));
		}
		return false;
	}
	
	public String getBarcodeAsString() {
		final int barcode = getBarCode();
		if (barcode <= 0) {
			return "none";
		}
		return Barcode.format(barcode);
	}
	
	private final Border getBorder(final int shift) {
		switch ((borders & (0x3 << shift)) >>> shift) {
			case 0:
				return Border.UNKNOWN;
			case 1:
				return Border.CLOSED;
			case 2:
				return Border.OPEN;
			default:
				// It should not be possible to reach this
				throw new RuntimeException("The universe has collapsed!");
		}
	}
	
	public Border getBorder(final Orientation dir) {
		switch (dir) {
			case NORTH:
				return getBorderNorth();
			case EAST:
				return getBorderEast();
			case SOUTH:
				return getBorderSouth();
			case WEST:
				return getBorderWest();
			default:
				throw new RuntimeException("Invalid direction:  " + dir);
		}
	}
	
	public Border getBorderEast() {
		return getBorder(2);
	}
	
	public Border getBorderNorth() {
		return getBorder(0);
	}
	
	public Border getBorderSouth() {
		return getBorder(4);
	}
	
	public Border getBorderWest() {
		return getBorder(6);
	}
	
	public int getItemId() {
		return item;
	}
	
	public int getX() {
		return x;
	}
	
	private void setX(int x){
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	private void setY(int y){
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return hashCode(x, y);
	}
	
	public static int hashCode(final int x, final int y) {
		return (((y & 0xFFFF) << 16) | (x & 0xFFFF));
	}
	
	public boolean isNeighborFrom(Tile other){
		if(this.isEastFrom(other) && this.getBorderWest() == Border.OPEN){
			return true;
		}
		
		if(this.isWestFrom(other) && this.getBorderEast() == Border.OPEN){
			return true;
		}
		
		if(this.isNorthFrom(other)&& this.getBorderSouth() == Border.OPEN){
			return true;
		}
		
		if(this.isSouthFrom(other)&& this.getBorderNorth()== Border.OPEN){
			return true;
		}
		
		return false;
	}
	
	public boolean isAbsoluteNeighborFrom(Tile other){
		if(this.isEastFrom(other)){
			return true;
		}
		
		if(this.isWestFrom(other)){
			return true;
		}
		
		if(this.isNorthFrom(other)){
			return true;
		}
		
		if(this.isSouthFrom(other)){
			return true;
		}
		
		return false;
	}
	
	public boolean matches(final int x, final int y) {
		return ((this.x == x) && (this.y == y));
	}
	
	public static final double ratioPixelsPerDistance() {
		return ((double)RESOLUTION / (double)SIZE);
	}
	
	private final void setBorder(final int shift, final Border border) {
//		if (getBorder(shift) == border) {
//			return;
//		}

		final int mask = (~(0x3 << shift) & 0xFF);

		final int value;
		switch (border) {
			case CLOSED:
				value = 1;
				break;
			case OPEN:
				value = 2;
				break;
			case UNKNOWN:
				
				value = 0;
				break;
			default:
				throw new IllegalArgumentException("Invalid border:  " + border);
		}

		borders = (byte)((borders & mask) | (value << shift));
	}
	
	public void setBorderEast(final Border border) {
		//TODO: MAY CAUSE BUGS
//		if(this.getBorderEast()!= Border.UNKNOWN && border == Border.UNKNOWN){
//			//do nothing, once explored remains.
//			return;
//		}else{
			setBorder(2, border);
//		}
	}
	
	public void setBorderNorth(final Border border) {
		//TODO: MAY CAUSE BUGS
//		if(this.getBorderNorth()!= Border.UNKNOWN && border == Border.UNKNOWN){
//			//do nothing, once explored remains.
//			return;
//		}else{
			setBorder(0, border);
//		}
	}
	
	public void setBorderSouth(final Border border) {
		//TODO: MAY CAUSE BUGS
//		if(this.getBorderSouth()!= Border.UNKNOWN && border == Border.UNKNOWN){
//			//do nothing, once explored remains.
//			return;
//		}else{
			setBorder(4, border);
//		}
	}
	
	public void setBorderWest(final Border border) {
		//TODO: MAY CAUSE BUGS
//		if(this.getBorderWest()!= Border.UNKNOWN && border == Border.UNKNOWN){
//			//do nothing, once explored remains.
//			return;
//		}else{
			setBorder(6, border);
//		}
	}
	
	public void setItemId(final int id) {
		this.item = id;
	}
	
	public boolean isEastFrom(Tile other){
		return(other.getX()==this.getX()-1 && this.getY()==other.getY());
	}
	
	public boolean isWestFrom(Tile other){
		return(other.getX()==this.getX()+1 && this.getY()==other.getY());
	}
	
	public boolean isSouthFrom(Tile other){
		return(other.getY()==this.getY()+1 && this.getX()==other.getX());
	}
	
	public boolean isNorthFrom(Tile other){
		return(other.getY()==this.getY()-1 && this.getX()==other.getX());
	}
	
	public static Tile read(final DataInput input) throws IOException {
		final Tile tile = new Tile(input.readByte(), input.readByte());
		tile.setBarCode(input.readByte());
		tile.setSeesaw(input.readBoolean());
		
		tile.setStartPosition(input.readBoolean());
		tile.setStartPlayerId(input.readInt());
		tile.setStartOrientation(input.readInt());
		tile.setHasItem(input.readBoolean());
		tile.borders = input.readByte();
		tile.item = (input.readBoolean() ? input.readInt() : 0);
		return tile;
	}
	
	@Override
	public String toString(){
		return new StringBuilder()
		.append('[')
		.append('(').append(getX()).append(", ").append(getY())
		.append("), ").append(getBarcodeAsString())
		.append(']')
		.toString();
	}
	
	public void write(final DataOutput output) throws IOException {
		output.writeByte(getX());
		output.writeByte(getY());
		output.writeByte(getBarCode());
		output.writeBoolean(isSeesaw());
		output.writeBoolean(this.startPosition);
		output.writeInt(this.startPlayerId);
		output.writeInt(this.startOrientation);
		output.writeBoolean(hasItem());
		
		output.writeByte(borders);
		if (item > 0) {
			output.writeBoolean(true);
			output.writeInt(item);
		} else {
			output.writeBoolean(false);
		}
	}
	
	/**
	 * Set all borders open (flag == true) or closed (flag == false).
	 * @param flag
	 */
	public void setAllBordersOpen(boolean flag){
		Border b;
		if(flag){
			b = Border.OPEN;
		}else{
			b = Border.CLOSED;
		}
		
		this.setBorderEast(b);
		this.setBorderNorth(b);
		this.setBorderSouth(b);
		this.setBorderWest(b);
		
	}
	/**
	 * Set a certain border open (=true) or closed (=false) given a certain ori���ntation.
	 * @param o
	 * @param flag
	 */
	public void setBorder(Orientation o, boolean flag){
		Border b;
		if(flag){
			b = Border.OPEN;
		}else{
			b = Border.CLOSED;
		}
		
		switch(o){
			case EAST:
				this.setBorderEast(b);
				break;
			case NORTH:
				this.setBorderNorth(b);
				break;
			case SOUTH:
				this.setBorderSouth(b);
				break;
			case WEST:
				this.setBorderWest(b);
				break;
			default:
				break;
		
		}
	}
	
	public boolean isExplored(){
		if(this.isSeesaw){
			return true;
		}
		if(this.getBarCode()>=0 || this.getBarCode()==-2){
			return true;
		}
		if(this.getBorderEast() == Border.UNKNOWN){
			return false;
		}else if(this.getBorderNorth()==Border.UNKNOWN){
			return false;
		}else if(this.getBorderSouth()==Border.UNKNOWN){
			return false;
		}else if(this.getBorderWest() == Border.UNKNOWN){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Get the neighbors of this tile. The tile needs to be explored in order to determine the neighbors.
	 * 
	 * IllegalStateException is thrown : if(!isExplored)
	 * @return
	 */
	public List<Tile> getNeighbors(){
		
			List<Tile> neighbors = new ArrayList<Tile>();
			if(this.getBorderEast()==Border.OPEN){
				neighbors.add(new Tile(x+1,y));
			}
			if(this.getBorderNorth()==Border.OPEN){
				neighbors.add(new Tile(x,y+1));
			}
			
			if(this.getBorderSouth()==Border.OPEN){
				neighbors.add(new Tile(x,y-1));
			}
			
			if(this.getBorderWest()==Border.OPEN){
				neighbors.add(new Tile(x-1,y));
			}
			
			return neighbors;
		
		
	}
	
	public List<Tile> getAbsoluteNeighbors(){
		List<Tile> neighbors = new ArrayList<Tile>();
		
			neighbors.add(new Tile(x+1,y));
			neighbors.add(new Tile(x,y+1));
			neighbors.add(new Tile(x,y-1));
			neighbors.add(new Tile(x-1,y));
		
		return neighbors;
	}


	/**
	 * Returns the barcode of this tile.
	 * 
	 * @return -1 if no barcode is available. 
	 */
	public int getBarCode() {
		return barcode;
	}



	public void setBarCode(int barcode) {
		this.barcode = barcode;
	}


	/**
	 * Returns whether this tile is allowed to have a barcode.
	 * 
	 * Current specification is that only in straight tiles there can be barcodes.
	 * 
	 * @return
	 */
	public boolean canHaveBarcode() {
		boolean vertical = this.getBorderEast() == Border.CLOSED && this.getBorderWest() == Border.CLOSED && this.getBorderNorth() == Border.OPEN && this.getBorderSouth() == Border.OPEN;
		boolean horizontal = this.getBorderNorth() == Border.CLOSED && this.getBorderSouth() == Border.CLOSED && this.getBorderEast() == Border.OPEN && this.getBorderWest() == Border.OPEN;
		return vertical || horizontal;
	}



	public void setDistanceFromStart(double neighborDistanceFromStart) {
		this.distanceFromStart = neighborDistanceFromStart;
		
	}



	public double getDistanceFromStart() {
		return this.distanceFromStart;
	}



	public void setPreviousNode(Tile current) {
		this.parent = current;
		
	}



	public void setHeuristicDistanceFromGoal(double determineHeuristicValue) {
		this.heuristicDistanceFromGoal = determineHeuristicValue;
		
	}



	public Tile getPreviousNode() {
		
		return this.parent;
	}
	
	public int compareTo(final Tile otherNode) {
		double thisTotalDistanceFromGoal = heuristicDistanceFromGoal + distanceFromStart;
        double otherTotalDistanceFromGoal = otherNode.getHeuristicDistanceFromGoal() + otherNode.getDistanceFromStart();
        
        if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal) {
                return -1;
        } else if (thisTotalDistanceFromGoal > otherTotalDistanceFromGoal) {
                return 1;
        } else {
                return 0;
        }
	}



	private double getHeuristicDistanceFromGoal() {
		return this.heuristicDistanceFromGoal;
	}



	public void setOrientationToReach(Direction bestDir) {
		this.orientationToReach = bestDir;
		
	}



	public Direction getOrientationToReach() {
		return this.orientationToReach;
	}


    /**
     * Turns the borders and coordinates of the Tile according to the given direction.
     *
     * Direction.LEFT means a -90 degrees turn.
     * Direction.RIGHT means a +90 degrees turn.
     * Direction.DOWN means a -+180 degrees turn.
     * Direction.UP means a -+0 degrees turn. (and is ignored)
     *
     * @param direction
     */
    public void rotate(Direction direction){
        Border oldNorth = getBorderNorth();
        Border oldEast = getBorderEast();
        Border oldSouth = getBorderSouth();
        Border oldWest = getBorderWest();
        int oldX = getX();
        int oldY = getY();
        switch (direction) {
        case LEFT:
            //-90 degrees
            setBorderNorth(oldEast);
            setBorderEast(oldSouth);
            setBorderSouth(oldWest);
            setBorderWest(oldNorth);
            setX(-oldY);
            setY(oldX);
            break;
        case RIGHT:
            //+90 degrees
            setBorderNorth(oldWest);
            setBorderEast(oldNorth);
            setBorderSouth(oldEast);
            setBorderWest(oldSouth);
            setX(oldY);
            setY(-oldX);
            break;
        case DOWN:
            //-+180 degrees
            setBorderNorth(oldSouth);
            setBorderEast(oldWest);
            setBorderSouth(oldNorth);
            setBorderWest(oldEast);
            setX(-oldX);
            setY(-oldY);
            break;
        default:
            break;
        }
    }
    
    /**
     * Use this method to transform tiles according to the calculated values of the MazeMerger.
     * @param angle		The angle difference between the 2 maps.
     * @param mergeTranslationVector	The translational difference of the 2 maps after correcting for rotation.
     */
    public void transform(double angle, Vector<Integer> mergeTranslationVector){
    	Direction direction = Direction.UP;
    	if(angle>0){
			if(angle==90){
				direction = Direction.RIGHT;
			}
			else if(angle==180){
				direction = Direction.DOWN;
			}
			else if(angle==270){
				direction = Direction.LEFT;
			}
    	}
    	this.transform(direction, mergeTranslationVector);
    }
    	
    /**
    * Use this method to transform tiles according to the calculated values of the MazeMerger.
    * @param angle		The angle difference between the 2 maps.
    * @param mergeTranslationVector	The translational difference of the 2 maps after correcting for rotation.
    */
    public void transform(Direction direction, Vector<Integer> mergeTranslationVector){
        this.rotate(direction);
        int dx = mergeTranslationVector.get(0);
        int dy = mergeTranslationVector.get(1);
        this.setX(this.getX()+dx);
        this.setY(this.getY()+dy);
     }

	public Byte getByteRepresentation(){
		return this.borders;
	}
}
