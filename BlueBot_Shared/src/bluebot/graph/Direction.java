package bluebot.graph;
/**
 * Direction Enumerations.
 * 
 * @author  Incalza Dario
 *
 */
public enum Direction{
	UP(0),DOWN(2),LEFT(3),RIGHT(1);
	
	private int v;
	
	private Direction(int v){
		this.v = v;
	}
	
	private int getValue(){
		return v;
	}
	
	private Direction getDir(int v){
		for(Direction d : Direction.values()){
			if(d.getValue()==v){
				return d;
			}
		}
		
		throw new IllegalArgumentException("Direction value not supported.");
	}
	
	public Direction turnCWise(){
		return getDir((this.getValue()+1)%4);
	}
	
	public Direction turnCCWise(){
		return getDir((this.getValue()+3)%4);
	}
}