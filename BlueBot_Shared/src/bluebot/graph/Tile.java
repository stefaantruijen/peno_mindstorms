package bluebot.graph;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




/**
 * 
 * @author Ruben Feyen
 */
public class Tile {
	
	/**    The size of a tile (in mm)    */
	public static final float SIZE = 400F;
	
	private byte borders;
	private int x, y;
	private boolean explored;
	
	public Tile(final int x, final int y) {
		this.x = x;
		this.y = y;
		this.explored = false;
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
	
	private final Border getBorder(final int shift) {
		switch ((borders & (0x3 << shift)) >>> shift) {
			case 0:
				return Border.OPEN;
			case 1:
				return Border.CLOSED;
			default:
				// It should not be possible to reach this
				throw new RuntimeException("The universe has collapsed!");
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
		if(this.isEastFrom(other) && other.getBorderEast() == Border.OPEN){
			return true;
		}
		
		if(this.isWestFrom(other) && other.getBorderWest() == Border.OPEN){
			return true;
		}
		
		if(this.isNorthFrom(other)&& other.getBorderNorth() == Border.OPEN){
			return true;
		}
		
		if(this.isSouthFrom(other)&& other.getBorderSouth()== Border.OPEN){
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
				value = 0;
				break;
			default:
				throw new IllegalArgumentException("Invalid border:  " + border);
		}
		
		borders = (byte)((borders & mask) | (value << shift));
	}
	
	public void setBorderEast(final Border border) {
		setBorder(2, border);
	}
	
	public void setBorderNorth(final Border border) {
		setBorder(0, border);
	}
	
	public void setBorderSouth(final Border border) {
		setBorder(4, border);
	}
	
	public void setBorderWest(final Border border) {
		setBorder(6, border);
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
		tile.borders = input.readByte();
		return tile;
	}
	
	@Override
	public String toString(){
		return ("("+this.getX()+","+this.getY()+")");
	}
	
	public void write(final DataOutput output) throws IOException {
		output.writeByte(getX());
		output.writeByte(getY());
		output.writeByte(borders);
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
	
	public void setExplored(){
		this.explored = true;
	}
	
	public boolean isExplored(){
		return this.explored;
	}
	
	public List<Tile> getNeighbors(){
		if(isExplored()){
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
		
		throw new IllegalStateException("This tile is not yet explored.");
		
	}
}