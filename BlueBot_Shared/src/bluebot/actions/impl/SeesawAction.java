package bluebot.actions.impl;

import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.sensors.CalibrationException;

public class SeesawAction extends Action{
	
	WhiteLineAction white = new WhiteLineAction();

	@Override
	protected void execute() throws ActionException, DriverException,
			InterruptedException {
		if(!this.getDriver().seeInfrared()){
			this.seesaw();
		}
	}
	
	private void seesaw() throws ActionException, DriverException, InterruptedException{
		int speed = this.getDriver().getSpeed();
		this.getDriver().setSpeed(20);
		this.getDriver().moveForward();
		while(this.getDriver().isMoving() && !this.getDriver().readsBlack());
		this.getDriver().stop();
		this.getDriver().moveForward(20, true);
		white.execute(this.getDriver());
		this.getDriver().moveBackward(20, true);
		this.getDriver().setSpeed(speed);
	}

}
