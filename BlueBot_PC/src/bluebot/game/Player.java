package bluebot.game;



/**
 * 
 * @author Ruben Feyen
 */
public class Player extends Entity {
	
	private float angle;
	private String id;
	private boolean item;
	private int number;
	
	
	public Player(final String id) {
		setId(id);
	}
	
	
	
	public float getAngle() {
		return angle;
	}
	
	public String getId() {
		return id;
	}
	
	public int getNumber() {
		return number;
	}
	
	public boolean hasItem() {
		return item;
	}
	
	private final void setAngle(final float angle) {
		this.angle = angle;
	}
	
	private final void setId(final String id) {
		this.id = id;
	}
	
	private final void setItem(final boolean item) {
		this.item = item;
	}
	
	private final void setNumber(final int number) {
		this.number = number;
	}
	
	private final void setPosition(final long x, final long y, final float angle) {
		setPosition(x, y);
		setAngle(angle);
	}
	
	public void update(final long x, final long y, final float angle,
			final int number, final boolean item) {
		setNumber(number);
		setPosition(x, y, angle);
		setItem(item);
	}
	
}
