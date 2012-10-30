package bluebot.util;



/**
 * 
 * @author Ruben Feyen
 */
public class Position {
	
	private float x, y;
	
	
	public Position(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("(")
		.append(getX())
		.append(", ")
		.append(getY())
		.append(')')
		.toString();
	}
	
}