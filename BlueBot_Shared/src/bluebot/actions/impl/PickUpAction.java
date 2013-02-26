package bluebot.actions.impl;

import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;

public class PickUpAction extends Action{
	
	private WhiteLineAction white = new WhiteLineAction();

	@Override
	protected void execute() throws ActionException, DriverException,
			InterruptedException {
		this.getDriver().moveForward(5F, true);
		white.execute(getDriver());
		this.getDriver().moveBackward(40F, true);
		this.getDriver().turnRight(180, true);
		this.getDriver().moveBackward();
		while(this.getDriver().isMoving()){
			if(this.getDriver().isPressed()){
				this.getDriver().stop();
				this.getDriver().sendMessageMQ("peno.blauw","wc-rol");
			}
		}
		
	}

}
