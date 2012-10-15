package simulator;

/**
 * Enum holding actions to perform with the simulator.
 * 
 * @author Dieter
 *
 */
public enum Action {
	//TODO: find and replace 'System.out.println(' with 'Debug.print('
	
	//Private abstract double progress in elke enum. met een functie 
	TRAVEL {
		public  void execute(Robot robot){
			double distance = robot.getCurrentArgument();
			robot.fireMessage("Simulator Robot begins traveling " + distance + " mm. This will take roughly " + distance/robot.getTravelSpeed() +" seconds.");
			try {
				sleepAtLeast((long) (distance*1000/robot.getTravelSpeed()), robot);
				//TODO: eventually draw it and/or update output values (traveled so far...)
				robot.fireMessage("Stopping travel now.");
				robot.stop();
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
				sleepAtLeast((long) (angle*1000/robot.getRotateSpeed()), robot);
				robot.fireMessage("Stopping rotation now");
				robot.stop();
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
				robot.run();
			}
		}
	}
	
}
