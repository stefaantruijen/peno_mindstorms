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
		System.out.println("STARTED SEESAW");
		this.getDriver().setSpeed(50);
		System.out.println("STARTED SEESAW 1");
		this.getDriver().moveForward();
		System.out.println("STARTED SEESAW 2");
		while(this.getDriver().isMoving() && !this.getDriver().readsGray());
		System.out.println("STARTED SEESAW 3");
		while(this.getDriver().isMoving() && !this.getDriver().readsBlack());
		System.out.println("STARTED SEESAW 4");
		this.getDriver().setSpeed(12);
		System.out.println("STARTED SEESAW 5");
		while(this.getDriver().isMoving() && !this.getDriver().readsGray());
		System.out.println("STARTED SEESAW 6");
		this.getDriver().stop();
		System.out.println("STARTED SEESAW 7");
		white.execute(this.getDriver());
		System.out.println("STARTED SEESAW 8");
		this.getDriver().moveForward(20, true);
		System.out.println("STARTED SEESAW 9");
		this.getDriver().setSpeed(speed);
		System.out.println("ENDED SEESAW");
	}

}
