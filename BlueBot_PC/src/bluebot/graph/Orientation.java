package bluebot.graph;
/**
 *Enumerations of ori�ntations possible in the maze.
 * 
 * @author Incalza Dario
 *
 */
public enum Orientation {
	NORTH("N"),EAST("E"),SOUTH("S"),WEST("W");
	
	private final String source;
	
	private Orientation(String v){
		this.source = v;
	}
	
	/**
	 * Calculates the matching orientation for a given heading
	 * 
	 * @param heading - the heading (in degrees)
	 * 
	 * @return an {@link Orientation} object
	 */
	public static Orientation forHeading(final float heading) {
		if (heading >= 315F) {
			return Orientation.NORTH;
		}
		if (heading >= 225F) {
			return Orientation.WEST;
		}
		if (heading >= 135F) {
			return Orientation.SOUTH;
		}
		if (heading >= 45F) {
			return Orientation.EAST;
		}
		return Orientation.NORTH;
	}
	
	/**
	 * Get the source String representing this ori�ntation.
	 */
	public String getSource(){
		return this.source;
	}
	/**
	 * For a given source String get a Orientation value.
	 * @param source
	 * @return
	 */
	public static Orientation getOrientation(String source){
		for(Orientation o : Orientation.values()){
			if(o.getSource().equals(source)){
				return o;
			}
		}
		
		throw new IllegalArgumentException("The given source is not a valid orientation identifier.");
	}
	/**
	 * Get the opposite ori�ntation for this ori�ntation.
	 * 
	 */
	public Orientation getOpposite(){
		switch(this){
		case EAST:
			
			return WEST;
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		case WEST:
			return EAST;
		default:
			throw new UnsupportedOperationException();
			
		
		}
	}
	/**
	 * Get a ori�ntation when rotating this ori�ntation clockwise.
	 * @return
	 */
	public Orientation rotateCW(){
		switch(this){
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			throw new UnsupportedOperationException();
		
		}
	}
	/**
	 * Get a ori�ntation when rotating this ori�ntation counter clockwise.
	 * @return
	 */
	public Orientation rotateCCW(){
		switch(this){
		case EAST:
			return NORTH;
		case NORTH:
			return WEST;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		default:
			throw new UnsupportedOperationException();
		
		}
	}
	
}
