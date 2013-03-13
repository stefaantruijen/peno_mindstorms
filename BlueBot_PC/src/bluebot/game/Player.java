package bluebot.game;



/**
 * Represents a player of the game
 * 
 * @author Ruben Feyen
 */
public class Player {
	
	private String id;
	private float x, y, z;
	
	
	public Player(final String id) throws GameException {
		setId(id);
	}
	
	
	
	public float getHeading() {
		return z;
	}
	
	public String getId() {
		return id;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	protected boolean isValidId(final String id) {
		return ((id != null) && !id.isEmpty());
	}
	
	private final void setId(final String id) throws GameException {
		if (!isValidId(id)) {
			throw new GameException("Invalid player ID:  " + id);
		}
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("Player[%s]", getId());
	}
	
}
