package bluebot.game;



/**
 * Represents an entity in the world
 * 
 * @author Ruben Feyen
 */
public abstract class Entity {
	
	private float x, y;
	
	
	public Entity() {
		this(0F, 0F);
	}
	public Entity(final float x, final float y) {
		setX(x);
		setY(y);
	}
	
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setPosition(final float x, final float y) {
		setX(x);
		setY(y);
	}
	
	public void setX(final float x) {
		this.x = x;
	}
	
	public void setY(final float y) {
		this.y = y;
	}
	
}
