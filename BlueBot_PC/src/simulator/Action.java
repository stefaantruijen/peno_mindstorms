package simulator;

/**
 * Enum holding actions to perform with the simulator.
 * 
 * @author Dieter
 *
 */
public enum Action {
	
//	private static final long INF = Double.doubleToLongBits(Double.MAX_VALUE);
	
	//TODO: find and replace 'System.out.println(' with 'Debug.print('
	
	//Private abstract double progress in elke enum. met een functie 
	TRAVEL {
		public  void execute(Robot robot){
			double distance = robot.getCurrentArgument();
			robot.fireMessage("Simulator Robot begins traveling " + distance + " mm. This will take roughly " + distance/robot.getTravelSpeed() +" seconds.");
			try {
				if ((distance == Double.MAX_VALUE) || (distance == Double.MIN_VALUE)) {
					sleepUntilStopped(robot);
				} else {
					sleepAtLeast((long) (distance/robot.getTravelSpeed()), robot);
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
			robot.fireMessage("Simulator Robot begins rotating " + angle + " degrees. This will take roughly "+ angle/robot.getRotateSpeed() +" seconds.");
			try {
				if ((angle == Double.MAX_VALUE) || (angle == Double.MIN_VALUE)) {
					sleepUntilStopped(robot);	
				} else {
					sleepAtLeast((long) (angle/robot.getRotateSpeed()), robot);
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
