package simulator;

/**
 * Enum holding actions to perform with the simulator.
 * 
 * @author Dieter
 *
 */
public enum Action {
		
	//Private abstract double progress in elke enum. met een functie 
	TRAVEL {
		public  void execute(Robot robot){
			double distance = robot.getCurrentArgument();
			try {
				if ((distance == Double.MAX_VALUE) || (distance == Double.MIN_VALUE)) {
					robot.fireMessage("Simulator Robot begins traveling.");
					sleepUntilStopped(robot);
				} else {
					double time = Math.abs(distance/robot.getTravelSpeed()); // Time in seconds.
					robot.fireMessage("Simulator Robot starts traveling " + distance + " mm. This will take roughly " + time +" seconds.");
					sleepAtLeast((long) (1000*time), robot); //Time in milliseconds needed
				}
				//TODO: eventually draw it and/or update output values (traveled so far...)
				robot.fireMessage("Stopping travel now.");
				robot.endMove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	},
	ROTATE{
		public  void execute(Robot robot){
			double angle = robot.getCurrentArgument();
			try {
				if ((angle == Double.MAX_VALUE) || (angle == Double.MIN_VALUE)) {
					robot.fireMessage("Simulator Robot begins rotating.");
					sleepUntilStopped(robot);
				} else {
					double time = Math.abs(angle/robot.getRotateSpeed()); // Time in seconds.
					robot.fireMessage("Simulator Robot starts rotating "+ angle + " degrees. This will take roughly "+ time +" seconds.");
					sleepAtLeast((long) (1000*time), robot); // time in milliseconds needed
				}
				robot.fireMessage("Stopping rotation now");
				robot.endMove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	
	public abstract void execute(Robot robot);
	
	public void sleepAtLeast(long millis, Robot robot) throws InterruptedException {
		long t0 = System.currentTimeMillis();
		long millisLeft = millis;
		while(millisLeft >0){
			Thread.sleep(millisLeft);
			long t1 = System.currentTimeMillis();
			millisLeft =millis - (t1-t0);
			if(robot.getStopFlag()){
				//TODO: notice the progress so far!
//				robot.run();
				return;
			}
		}
	}
	
	public void sleepUntilStopped(Robot robot) throws InterruptedException {
		while (!robot.getStopFlag()) {
			Thread.sleep(10);
		}
	}
	
}
