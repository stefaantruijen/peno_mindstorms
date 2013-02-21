package bluebot.graph;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bluebot.util.Barcode;



/**
 * 
 * @author Ruben Feyen
 */
public class Tile implements Comparable<Tile> {
	
	/**    The size of a tile (in mm)    */
	public static final float SIZE = 400F;
	
	private int barcode = -1;
	private byte borders;
	private int item;
	private int x, y;

	private double distanceFromStart;

	private Tile parent;

	private double heuristicDistanceFromGoal;

	private Direction orientationToReach;
	
	public Tile(final int x, final int y) {
		this.x = x;
		this.y = y;
		this.distanceFromStart = Double.MAX_VALUE;
		
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
	
	public int getY() {
		return y;
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
		if(this.getBorderEast()!= Border.UNKNOWN && border == Border.UNKNOWN){
			//do nothing, once explored remains.
			return;
		}else{
			setBorder(2, border);
		}
	}
	
	public void setBorderNorth(final Border border) {
		if(this.getBorderNorth()!= Border.UNKNOWN && border == Border.UNKNOWN){
			//do nothing, once explored remains.
			return;
		}else{
			setBorder(0, border);
		}
	}
	
	public void setBorderSouth(final Border border) {
		if(this.getBorderSouth()!= Border.UNKNOWN && border == Border.UNKNOWN){
			//do nothing, once explored remains.
			return;
		}else{
			setBorder(4, border);
		}
	}
	
	public void setBorderWest(final Border border) {
		if(this.getBorderWest()!= Border.UNKNOWN && border == Border.UNKNOWN){
			//do nothing, once explored remains.
			return;
		}else{
			setBorder(6, border);
		}
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
	 * Set a certain border open (=true) or closed (=false) given a certain ori�ntation.
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



	
}