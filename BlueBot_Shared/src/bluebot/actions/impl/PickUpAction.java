package bluebot.actions.impl;

import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;

public class PickUpAction extends Action{
	
	private WhiteLineAction white = new WhiteLineAction();

	@Override
	protected void execute() throws ActionException, DriverException,
			InterruptedException {
		//this.resetHead();
		this.executeWhiteLine();
		this.getDriver().modifyOrientation();
		
		
		int speed = this.getDriver().getSpeed();
		this.getDriver().setSpeed(30);
		this.getDriver().moveForward();
		while(!getDriver().isPressed()){
			
		}
		this.getDriver().stop();
		//robot rijdt naar achter zodat hij vrij 180 graden kan draaien
		this.getDriver().moveBackward(100F,true);
		this.getDriver().turnLeft(180, true);
		this.executeWhiteLine();
		this.getDriver().setSpeed(speed);
		this.getDriver().moveForward(200F, true);
		this.getDriver().sendMQMessage("Got the package, to the choppa!");
	}

}
