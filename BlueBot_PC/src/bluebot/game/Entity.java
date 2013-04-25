package bluebot.game;



/**
 * Represents an entity in the world
 * 
 * @author Ruben Feyen
 */
public abstract class Entity {
	
	private long x, y;
	
	
	public Entity() {
		this(0L, 0L);
	}
	public Entity(final long x, final long y) {
		setX(x);
		setY(y);
	}
	
	
	
	public long getX() {
		return x;
	}
	
	public long getY() {
		return y;
	}
	
	public void setPosition(final long x, final long y) {
		setX(x);
		setY(y);
	}
	
	public void setX(final long x) {
		this.x = x;
	}
	
	public void setY(final long y) {
		this.y = y;
	}
	
}
