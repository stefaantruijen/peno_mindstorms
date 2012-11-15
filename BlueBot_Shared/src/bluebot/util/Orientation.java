package bluebot.util;



/**
 * 
 * @author Ruben Feyen
 */
public class Orientation extends Position {
	
	private float body, head;
	
	
	public Orientation() {
		this(0F, 0F, 0F, 0F);
	}
	public Orientation(final float x, final float y,
			final float body, final float head) {
		super(x, y);
		this.body = body;
		this.head = head;
	}
	
	
	
	public float getHeadingBody() {
		return body;
	}
	
	public float getHeadingHead() {
		return head;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("[")
		.append(super.toString())
		.append(", ")
		.append(getHeadingBody())
		.append(']')
		.toString();
	}
	
}