package bluebot.maze;
/**
 * Represents the different tile types a maze can contain.
 * 
 * @author Incalza Dario
 *
 */
public enum TileType {
	
	STRAIGHT("Straight"),CORNER("Corner"),TSPLIT("T"),DEADEND("DeadEnd"),CROSS("Cross"),CLOSED("Closed"),SEESAW("Seesaw");
	
	private final String source;
	
	private TileType(final String v){
		this.source = v;
	}
	/**
	 * Get the source string by which a tile is represented.
	 * @return
	 */
	public String getSource(){
		return this.source;
	}
	/**
	 * Get a TileType for a given source String.
	 * @param source
	 * @return
	 */
	public static TileType getType(String source){
		for(TileType tt : TileType.values()){
			if(tt.getSource().equals(source)){
				return tt;
			}
		}
		
		throw new IllegalArgumentException("Given source is not a valid tile type.");
	}
	

}
