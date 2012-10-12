package bluebot.core;



/**
 * Dummy implementation of the {@link Controller} interface
 * for debugging purposes
 * 
 * @author Ruben Feyen
 */
public class DummyController extends AbstractController {
	
	public static final Controller SINGLETON = new DummyController();
	
	
	private DummyController() {
		// hidden
	}
	
	
	
	public void doPolygon(final int corners, final float length) {
		System.out.println("POLYGON (" + corners + ", " + length + ")");
	}
	
	public void moveBackward() {
		System.out.println("MOVE BACKWARD");
	}
	
	public void moveBackward(final float distance) {
		System.out.println("MOVE BACKWARD (" + distance + ")");
	}
	
	public void moveForward() {
		System.out.println("MOVE FORWARD");
	}
	
	public void moveForward(final float distance) {
		System.out.println("MOVE FORWARD (" + distance + ")");
	}
	
	public void stop() {
		System.out.println("STOP");
	}
	
	public void turnLeft() {
		System.out.println("TURN LEFT");
	}
	
	public void turnLeft(final float degrees) {
		System.out.println("TURN LEFT (" + degrees + ")");
	}
	
	public void turnRight() {
		System.out.println("TURN RIGHT");
	}
	
	public void turnRight(final float degrees) {
		System.out.println("TURN RIGHT (" + degrees + ")");
	}
	
}