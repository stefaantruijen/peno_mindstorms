package bluebot.actions.impl;

import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;

public class PickUpAction extends Action{
	
	private WhiteLineAction white = new WhiteLineAction();

	@Override
	protected void execute() throws ActionException, DriverException,
			InterruptedException {
		this.getDriver().turnRight(180, true);
		this.getDriver().moveForward(50F, true);
		white.execute(getDriver());
		this.getDriver().moveBackward();
		System.out.println("moving backwards");
		boolean press = false;
		while(this.getDriver().isMoving()){
			System.out.println("I am moving");
			press = this.getDriver().isPressed();
			if(press){
				System.out.println("I got pressed");
				this.getDriver().stop();
				System.out.println("I stopped");
				//this.getDriver().sendMessageMQ("Object gevonden!");
			}
		}
		System.out.println("ended while, starting white line");
		white.execute(getDriver());
		System.out.println("ended white line");
		this.getDriver().moveForward(200F, true);
		System.out.println("done");
	}

}
