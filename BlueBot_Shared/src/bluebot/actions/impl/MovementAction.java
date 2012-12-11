package bluebot.actions.impl;


import static bluebot.io.protocol.impl.MovePacket.*;

import bluebot.actions.Action;



/**
 * Represents a movement {@link Action}
 * 
 * @author Ruben Feyen
 */
public class MovementAction extends Action {
	
	private int direction;
	private float quantity;
	
	
	public MovementAction(final int direction, final float quantity) {
		this.direction = direction;
		this.quantity = quantity;
	}
	
	
	
	public static MovementAction createBackward(final float distance) {
		return new MovementAction(MOVE_BACKWARD, distance);
	}
	
	public static MovementAction createForward(final float distance) {
		return new MovementAction(MOVE_FORWARD, distance);
	}
	
	public static MovementAction createLeft(final float angle) {
		return new MovementAction(TURN_LEFT, angle);
	}
	
	public static MovementAction createRight(final float angle) {
		return new MovementAction(TURN_RIGHT, angle);
	}
	
	protected void execute() throws InterruptedException {
		switch (direction) {
			case MOVE_BACKWARD:
				getDriver().moveBackward(quantity, false);
				break;
			case MOVE_FORWARD:
				getDriver().moveForward(quantity, false);
				break;
			case TURN_LEFT:
				getDriver().turnLeft(quantity, false);
				break;
			case TURN_RIGHT:
				getDriver().turnRight(quantity, false);
				break;
		}
		while (!isAborted() && getDriver().isMoving()) {
			Thread.sleep(10);
		}
	}
	
}